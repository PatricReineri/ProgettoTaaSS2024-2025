package com.service.guestgameservice.service;

import com.service.guestgameservice.dto.DecisionTreeDTO;
import com.service.guestgameservice.dto.GameRequestDTO;
import com.service.guestgameservice.dto.GuestInfoRequestDTO;
import com.service.guestgameservice.dto.TreeNodeDTO;
import com.service.guestgameservice.exception.UnauthorizedException;
import com.service.guestgameservice.model.Game;
import com.service.guestgameservice.model.GuestInfo;
import com.service.guestgameservice.repository.GameRepository;
import com.service.guestgameservice.repository.GuestGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.trees.J48;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private final GuestGameRepository guestGameRepository;
    private final GameRepository gameRepository;
    private final WebClient eventManagementWebClient;

    public GameService(GuestGameRepository guestGameRepository,
                       GameRepository gameRepository,
                       WebClient eventManagementWebClient) {
        this.guestGameRepository = guestGameRepository;
        this.gameRepository = gameRepository;
        this.eventManagementWebClient = eventManagementWebClient;
    }

    public void createGame(GameRequestDTO gameRequestDTO) {
        if (!authorizeAdmin(gameRequestDTO.getEventId(), gameRequestDTO.getUserMagicEventsTag())) {
            throw new UnauthorizedException("Not authorized to create game for event ID: " + gameRequestDTO.getEventId());
        }

        Game game = new Game();
        game.setEventId(gameRequestDTO.getEventId());
        game.setDescription(gameRequestDTO.getDescription());
        gameRepository.save(game);
    }

    public void deleteGame(Long eventId, Long userMagicEventsTag) {
        if (!authorizeAdmin(eventId, userMagicEventsTag)) {
            throw new UnauthorizedException("Not authorized to delete game for event ID: " + eventId);
        }

        Game game = gameRepository.findByEventId(eventId);
        if (game != null) {
            gameRepository.delete(game);
        }
    }

    public boolean gameExists(Long eventId) {
        return gameRepository.findByEventId(eventId) != null;
    }

    public void insertGuestInfo(GuestInfoRequestDTO guestInfoRequestDTO) {
        Long userTagAsLong = Long.valueOf(guestInfoRequestDTO.getUserMagicEventsTag());
        if (!authorizeParticipant(guestInfoRequestDTO.getGameId(), userTagAsLong)) {
            throw new UnauthorizedException("Not authorized to insert guest info for game ID: " + guestInfoRequestDTO.getGameId());
        }

        GuestInfo guestInfo = new GuestInfo();
        guestInfo.setIsMen(guestInfoRequestDTO.getIsMen());
        guestInfo.setAge(guestInfoRequestDTO.getAge());
        guestInfo.setIsHostFamilyMember(guestInfoRequestDTO.getIsHostFamilyMember());
        guestInfo.setIsHostAssociate(guestInfoRequestDTO.getIsHostAssociate());
        guestInfo.setHaveBeard(guestInfoRequestDTO.getHaveBeard());
        guestInfo.setIsBald(guestInfoRequestDTO.getIsBald());
        guestInfo.setHaveGlasses(guestInfoRequestDTO.getHaveGlasses());
        guestInfo.setHaveDarkHair(guestInfoRequestDTO.getHaveDarkHair());
        guestInfo.setUserMagicEventsTag(guestInfoRequestDTO.getUserMagicEventsTag());

        Game game = gameRepository.findByEventId(guestInfoRequestDTO.getGameId());
        game.setEventId(guestInfoRequestDTO.getGameId());
        guestInfo.setGame(game);

        guestGameRepository.save(guestInfo);
    }

    public DecisionTreeDTO createDecisionTree(Long eventId, Long userMagicEventsTag) {
        if (!authorizeParticipant(eventId, userMagicEventsTag)) {
            throw new UnauthorizedException("Not authorized to access decision tree for event ID: " + eventId);
        }

        try {
            Game game = gameRepository.findByEventId(eventId);
            if (game == null) {
                throw new RuntimeException("Game not found for event ID: " + eventId);
            }

            List<GuestInfo> guestInfoList = guestGameRepository.findByGame(game);

            if (guestInfoList.isEmpty()) {
                throw new RuntimeException("No guest data available to build decision tree");
            }

            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("isMen"));
            attributes.add(new Attribute("age"));
            attributes.add(new Attribute("isHostFamilyMember"));
            attributes.add(new Attribute("isHostAssociate"));
            attributes.add(new Attribute("haveBeard"));
            attributes.add(new Attribute("isBald"));
            attributes.add(new Attribute("haveGlasses"));
            attributes.add(new Attribute("haveDarkHair"));

            ArrayList<String> classValues = new ArrayList<>();
            for (GuestInfo guestInfo : guestInfoList) {
                String tag = guestInfo.getUserMagicEventsTag();
                if (!classValues.contains(tag)) {
                    classValues.add(tag);
                }
            }
            attributes.add(new Attribute("user_magic_events_tag", classValues));

            Instances dataset = getDatasetInstances(attributes, guestInfoList);

            J48 tree = getDecisionTree(dataset, classValues);

            double accuracy = getAccuracy(dataset, tree);

            String treeString = tree.toString();
            TreeNodeDTO rootNode = parseTreeFromString(treeString);

            return new DecisionTreeDTO(rootNode, accuracy, dataset.size());
        } catch (Exception e) {
            throw new RuntimeException("Error creating decision tree: " + e.getMessage(), e);
        }
    }

    private boolean authorizeAdmin(Long eventId, Long userMagicEventsTag) {
        try {
            Boolean isAdmin = eventManagementWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/gestion/isAdmin")
                            .queryParam("partecipantId", userMagicEventsTag)
                            .queryParam("eventId", eventId)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            //return Boolean.TRUE.equals(isAdmin);
        } catch (Exception e) {
            //return false;
        }
        return true; // Default to true for testing purposes
    }

    private boolean authorizeParticipant(Long eventId, Long userMagicEventsTag) {
        try {
            Boolean isParticipant = eventManagementWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/gestion/isParticipant")
                            .queryParam("partecipantId", userMagicEventsTag)
                            .queryParam("eventId", eventId)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            //return Boolean.TRUE.equals(isParticipant);
        } catch (Exception e) {
            //return false;
        }
        return true; // Default to true for testing purposes
    }

    private TreeNodeDTO parseTreeFromString(String treeString) {
        String[] lines = treeString.split("\n");

        int startLine = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty() && !line.startsWith("J48") && !line.startsWith("---") && !line.startsWith("=")) {
                startLine = i;
                break;
            }
        }

        return parseTreeNode(lines, startLine, new int[]{startLine});
    }

    private TreeNodeDTO parseTreeNode(String[] lines, int startLine, int[] currentLine) {
        if (currentLine[0] >= lines.length) {
            return null;
        }

        String line = lines[currentLine[0]];
        String trimmedLine = line.trim();

        if (trimmedLine.isEmpty()) {
            currentLine[0]++;
            return parseTreeNode(lines, startLine, currentLine);
        }

        currentLine[0]++;

        if (trimmedLine.contains(":") && !trimmedLine.endsWith(":")) {
            String[] parts = trimmedLine.split(":");
            if (parts.length >= 2) {
                String className = parts[1].trim();
                if (className.contains("(")) {
                    className = className.substring(0, className.indexOf("(")).trim();
                }
                return new TreeNodeDTO(className);
            }
        }

        String condition = extractCondition(trimmedLine);
        String question = formatQuestion(condition);

        TreeNodeDTO leftChild = parseTreeNode(lines, startLine, currentLine);

        TreeNodeDTO rightChild = parseTreeNode(lines, startLine, currentLine);

        return new TreeNodeDTO(question, leftChild, rightChild);
    }

    private String extractCondition(String line) {
        String condition = line.replaceAll("^[|\\s]+", "").trim();

        if (condition.endsWith(":")) {
            condition = condition.substring(0, condition.length() - 1).trim();
        }

        return condition;
    }

    private String formatQuestion(String condition) {
        condition = condition.replaceAll("^[|\\s]+", "").trim();

        if (condition.contains("<=")) {
            String[] parts = condition.split("<=");
            if (parts.length == 2) {
                String attribute = parts[0].trim();
                String value = parts[1].trim();
                return formatAttributeQuestion(attribute, "<=", value);
            }
        } else if (condition.contains(">")) {
            String[] parts = condition.split(">");
            if (parts.length == 2) {
                String attribute = parts[0].trim();
                String value = parts[1].trim();
                return formatAttributeQuestion(attribute, ">", value);
            }
        }

        return condition;
    }

    private String formatAttributeQuestion(String attribute, String operator, String value) {
        switch (attribute) {
            case "isMen":
                return "L'ospite è uomo?";
            case "age":
                if (operator.equals("<=")) {
                    return "L'ospite ha meno di " + (Integer.parseInt(value.split("\\.")[0]) + 1) + " anni?";
                } else {
                    return "L'ospite ha più di " + value.split("\\.")[0] + " anni?";
                }
            case "isHostFamilyMember":
                return "L'ospite è un membro della famiglia ospitante?";
            case "isHostAssociate":
                return "L'ospite è un associato dell'ospitante?";
            case "haveBeard":
                return "L'ospite ha la barba?";
            case "isBald":
                return "L'ospite NON ha i capelli?";
            case "haveGlasses":
                return "L'ospite porta gli occhiali?";
            case "haveDarkHair":
                return "L'ospite ha i capelli scuri?";
            default:
                return attribute + " " + operator + " " + value + "?";
        }
    }

    private static J48 getDecisionTree(Instances dataset, ArrayList<String> classValues) throws Exception {
        J48 tree = new J48();
        tree.setUnpruned(true);
        tree.setMinNumObj(1);
        tree.setConfidenceFactor(0.60f);
        tree.buildClassifier(dataset);

        return tree;
    }

    private static double getAccuracy(Instances dataset, J48 tree) throws Exception {
        int correct = 0;
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.instance(i);
            double classIndex = tree.classifyInstance(instance);
            String predictedClass = dataset.classAttribute().value((int) classIndex);
            String actualClass = instance.stringValue(dataset.classIndex());

            if (predictedClass.equals(actualClass)) {
                correct++;
            }
        }

        double accuracy = (double) correct / dataset.size() * 100;
        return accuracy;
    }

    private static Instances getDatasetInstances(ArrayList<Attribute> attributes, List<GuestInfo> guestInfoList) {
        Instances dataset = new Instances("GuestInfo", attributes, guestInfoList.size());
        dataset.setClassIndex(attributes.size() - 1);

        for (GuestInfo guestInfo : guestInfoList) {
            DenseInstance instance = new DenseInstance(attributes.size());
            instance.setValue(attributes.get(0), guestInfo.getIsMen() ? 1.0 : 0.0);
            instance.setValue(attributes.get(1), guestInfo.getAge());
            instance.setValue(attributes.get(2), guestInfo.getIsHostFamilyMember() ? 1.0 : 0.0);
            instance.setValue(attributes.get(3), guestInfo.getIsHostAssociate() ? 1.0 : 0.0);
            instance.setValue(attributes.get(4), guestInfo.getHaveBeard() ? 1.0 : 0.0);
            instance.setValue(attributes.get(5), guestInfo.getIsBald() ? 1.0 : 0.0);
            instance.setValue(attributes.get(6), guestInfo.getHaveGlasses() ? 1.0 : 0.0);
            instance.setValue(attributes.get(7), guestInfo.getHaveDarkHair() ? 1.0 : 0.0);

            instance.setValue(attributes.get(8), guestInfo.getUserMagicEventsTag());

            instance.setDataset(dataset);
            dataset.add(instance);
        }
        return dataset;
    }
}
