package com.service.guestgameservice.service;

import com.service.guestgameservice.dto.DecisionTreeDTO;
import com.service.guestgameservice.dto.GameRequestDTO;
import com.service.guestgameservice.dto.GuestInfoRequestDTO;
import com.service.guestgameservice.dto.TreeNodeDTO;
import com.service.guestgameservice.model.Game;
import com.service.guestgameservice.model.GuestInfo;
import com.service.guestgameservice.repository.GameRepository;
import com.service.guestgameservice.repository.GuestGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.trees.J48;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GuestGameRepository guestGameRepository;
    private final GameRepository gameRepository;

    public void createGame(GameRequestDTO gameRequestDTO) {
        Game game = new Game();
        game.setEventId(gameRequestDTO.getEventId());
        game.setDescription(gameRequestDTO.getDescription());
        gameRepository.save(game);
    }

    public void deleteGame(Long eventId) {
        Game game = gameRepository.findByEventId(eventId);
        if (game != null) {
            gameRepository.delete(game);
        } else {
            log.warn("Game with event ID {} not found", eventId);
        }
    }


    public boolean gameExists(Long eventId) {
        return gameRepository.findByEventId(eventId) != null;
    }

    public void insertGuestInfo(GuestInfoRequestDTO guestInfoRequestDTO) {
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

    public DecisionTreeDTO createDecisionTree() {
        try {
            // Fetch guest info data
            List<GuestInfo> guestInfoList = guestGameRepository.findAll();

            if (guestInfoList.isEmpty()) {
                throw new RuntimeException("No guest data available to build decision tree");
            }

            // Define attributes for the decision tree (features)
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("isMen"));
            attributes.add(new Attribute("age"));
            attributes.add(new Attribute("isHostFamilyMember"));
            attributes.add(new Attribute("isHostAssociate"));
            attributes.add(new Attribute("haveBeard"));
            attributes.add(new Attribute("isBald"));
            attributes.add(new Attribute("haveGlasses"));
            attributes.add(new Attribute("haveDarkHair"));

            // Define nominal class attribute using unique user_magic_events_tag values
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
            log.info("Training accuracy: {}%", accuracy);

            // Parse tree structure from toString() method
            String treeString = tree.toString();
            TreeNodeDTO rootNode = parseTreeFromString(treeString);

            return new DecisionTreeDTO(rootNode, accuracy, dataset.size());
        } catch (Exception e) {
            log.error("Error creating decision tree", e);
            throw new RuntimeException("Error creating decision tree: " + e.getMessage(), e);
        }
    }

    private TreeNodeDTO parseTreeFromString(String treeString) {
        log.debug("Tree string:\n{}", treeString);
        String[] lines = treeString.split("\n");
        
        // Skip header lines (J48 unpruned tree, ---, empty lines)
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
        
        // Skip empty lines
        if (trimmedLine.isEmpty()) {
            currentLine[0]++;
            return parseTreeNode(lines, startLine, currentLine);
        }

        currentLine[0]++;
        
        // Check if it's a leaf node (contains colon followed by class name)
        if (trimmedLine.contains(":") && !trimmedLine.endsWith(":")) {
            String[] parts = trimmedLine.split(":");
            if (parts.length >= 2) {
                String className = parts[1].trim();
                // Remove count information if present
                if (className.contains("(")) {
                    className = className.substring(0, className.indexOf("(")).trim();
                }
                return new TreeNodeDTO(className);
            }
        }

        // It's an internal node - extract the condition and format as question
        String condition = extractCondition(trimmedLine);
        String question = formatQuestion(condition);
        
        // Parse left child (YES branch - condition is true)
        TreeNodeDTO leftChild = parseTreeNode(lines, startLine, currentLine);
        
        // Parse right child (NO branch - condition is false)  
        TreeNodeDTO rightChild = parseTreeNode(lines, startLine, currentLine);

        return new TreeNodeDTO(question, leftChild, rightChild);
    }

    private String extractCondition(String line) {
        // Remove leading pipe symbols and whitespace
        String condition = line.replaceAll("^[|\\s]+", "").trim();
        
        // If the line ends with a colon (like "age <= 24:"), remove it
        if (condition.endsWith(":")) {
            condition = condition.substring(0, condition.length() - 1).trim();
        }
        
        return condition;
    }

    private String formatQuestion(String condition) {
        // Remove any leading symbols like |, spaces, etc.
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
                return operator.equals("<=") ? "Guest is female?" : "Guest is male?";
            case "age":
                if (operator.equals("<=")) {
                    return "Guest is less than " + (Integer.parseInt(value.split("\\.")[0]) + 1) + " years old?";
                } else {
                    return "Guest is more than " + value.split("\\.")[0] + " years old?";
                }
            case "isHostFamilyMember":
                return operator.equals("<=") ? "Guest is not host family member?" : "Guest is host family member?";
            case "isHostAssociate":
                return operator.equals("<=") ? "Guest is not host associate?" : "Guest is host associate?";
            case "haveBeard":
                return operator.equals("<=") ? "Guest doesn't have beard?" : "Guest has beard?";
            case "isBald":
                return operator.equals("<=") ? "Guest is not bald?" : "Guest is bald?";
            case "haveGlasses":
                return operator.equals("<=") ? "Guest doesn't have glasses?" : "Guest has glasses?";
            case "haveDarkHair":
                return operator.equals("<=") ? "Guest doesn't have dark hair?" : "Guest has dark hair?";
            default:
                return attribute + " " + operator + " " + value + "?";
        }
    }

    private static J48 getDecisionTree(Instances dataset, ArrayList<String> classValues) throws Exception {
        // Build decision tree
        J48 tree = new J48();
        tree.setUnpruned(true);
        tree.setMinNumObj(1); // Allow single instances as leaves
        tree.setConfidenceFactor(0.60f);
        tree.buildClassifier(dataset);

        // Log tree structure
        log.info("Decision tree built with {} instances and {} unique classes",
                dataset.size(), classValues.size());
        log.info("Tree structure:\n{}", tree.toString());
        return tree;
    }

    private static double getAccuracy(Instances dataset, J48 tree) throws Exception {
        // Test classification on training data
        int correct = 0;
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.instance(i);
            double classIndex = tree.classifyInstance(instance);
            String predictedClass = dataset.classAttribute().value((int) classIndex);
            String actualClass = instance.stringValue(dataset.classIndex());

            if (predictedClass.equals(actualClass)) {
                correct++;
            }
            log.debug("Instance {}: Predicted={}, Actual={}", i, predictedClass, actualClass);
        }

        double accuracy = (double) correct / dataset.size() * 100;
        return accuracy;
    }

    private static Instances getDatasetInstances(ArrayList<Attribute> attributes, List<GuestInfo> guestInfoList) {
        // Create dataset
        Instances dataset = new Instances("GuestInfo", attributes, guestInfoList.size());
        dataset.setClassIndex(attributes.size() - 1); // Last attribute is the class

        // Add instances to dataset
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

            // Set class value (target) - this is what we want to classify
            instance.setValue(attributes.get(8), guestInfo.getUserMagicEventsTag());

            instance.setDataset(dataset);
            dataset.add(instance);
        }
        return dataset;
    }
}
