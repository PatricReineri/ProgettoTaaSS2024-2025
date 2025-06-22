package com.service.eventsmanagementservice.service;

import com.service.eventsmanagementservice.dto.EventDTO;
import com.service.eventsmanagementservice.model.Admin;
import com.service.eventsmanagementservice.model.Event;
import com.service.eventsmanagementservice.model.Partecipant;
import com.service.eventsmanagementservice.repository.AdminsRepository;
import com.service.eventsmanagementservice.repository.EventsRepository;
import com.service.eventsmanagementservice.repository.PartecipantsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EventGestorService {
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    AdminsRepository adminsRepository;
    @Autowired
    PartecipantsRepository partecipantsRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private WebClient userManagementWebClient;

    public String updateEventAdmins(ArrayList<String> admins, Long eventId, Long creatorId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if(event.getCreator().equals(creatorId)) {
            addAdmins(admins, eventId);
            return "Success";
        }else{
            return "Error";
        }
    }
    public String updateEventPartecipants(ArrayList<String> partecipants, Long eventId, Long creatorId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if(event.getCreator().equals(creatorId)) {
            addPartecipants(partecipants, eventId);
            return "Success";
        }else {
            return "Error";
        }
    }

    public EventDTO getEventInfo(Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        ArrayList<String> admins = new ArrayList<>();
        for (Admin admin : event.getAdmins()) {
            admins.add(admin.getUser().getEmail());
        }
        ArrayList<String> partecipants = new ArrayList<>();
        for (Partecipant partecipant : event.getPartecipants()) {
            partecipants.add(partecipant.getEmail());
        }
        return new EventDTO(
                event.getTitle(),
                event.getDescription(),
                event.getStarting(),
                event.getEnding(),
                event.getLocation(),
                event.getCreator(),
                admins,
                partecipants,
                event.getImage()
        );
    }

    @Transactional
    public Long create(EventDTO eventDTO, String creatorEmail) {
        Long creatorId = eventDTO.getCreator();
        partecipantsRepository.findById(creatorId)
            .orElseGet(() -> {
                Partecipant newP = new Partecipant();
                newP.setMagicEventTag(creatorId);
                newP.setEmail(creatorEmail);
                return partecipantsRepository.saveAndFlush(newP);
            });
        Event event = new Event(
                eventDTO.getTitle(),
                eventDTO.getDescription(),
                eventDTO.getStarting(),
                eventDTO.getEnding(),
                eventDTO.getLocation(),
                creatorId,
                eventDTO.getImage()
        );
        event = eventsRepository.save(event);
        addAdmins(eventDTO.getAdmins(), event.getEventId());
        return event.getEventId();
    }

    public List<Admin> addAdmins(List<String> admins, Long eventId){
        HashMap<Long, String> adminsWithId = geIdForEmails(admins);
        return addAdminsWithId(adminsWithId, eventId);
    }

    @Transactional
    public List<Admin> addAdminsWithId(HashMap<Long, String> admins, Long eventId){
        List<Partecipant> partecipantList = addPartecipantsWithId(admins, eventId);
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        ArrayList<Admin> newAdmins = new ArrayList<>();
        for (Partecipant partecipant : partecipantList) {
            Admin admin = adminsRepository.findById(partecipant.getMagicEventTag())
                    .orElseGet(() -> {
                        Admin newAdmin = new Admin();
                        newAdmin.setAdminId(partecipant.getMagicEventTag());
                        newAdmin.setUser(partecipant);
                        newAdmin.setEvents(new ArrayList<>());
                        return adminsRepository.saveAndFlush(newAdmin);
                    });
            if (!event.getAdmins().contains(admin)) {
                event.getAdmins().add(admin);
            }
            newAdmins.add(admin);
        }
        eventsRepository.save(event);
        return newAdmins;
    }

    public List<Partecipant> addPartecipants(List<String> partecipants, Long eventId) {
        HashMap<Long, String> partecipantsWithId = geIdForEmails(partecipants);
        return addPartecipantsWithId(partecipantsWithId, eventId);
    }

    @Transactional
    public List<Partecipant> addPartecipantsWithId(HashMap<Long, String> partecipantIds, Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        List<Partecipant> added = new ArrayList<>();
        for (Map.Entry<Long, String> partecipantEntry : partecipantIds.entrySet()) {
            Partecipant partecipant = partecipantsRepository.findById(partecipantEntry.getKey())
                    .orElseGet(() -> {
                        Partecipant newP = new Partecipant();
                        newP.setMagicEventTag(partecipantEntry.getKey());
                        newP.setEmail(partecipantEntry.getValue());
                        return partecipantsRepository.saveAndFlush(newP);
                    });
            if (!event.getPartecipants().contains(partecipant)) {
                event.getPartecipants().add(partecipant);
            }
            added.add(partecipant);
        }
        eventsRepository.save(event);
        return added;
    }

    public String modifyEvent(EventDTO eventDTO, Long creatorId, Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if(event.getCreator().equals(creatorId)) {
            event.setTitle(eventDTO.getTitle());
            event.setDescription(eventDTO.getDescription());
            event.setStarting(eventDTO.getStarting());
            event.setEnding(eventDTO.getEnding());
            event.setLocation(eventDTO.getLocation());
            event.setImage(eventDTO.getImage());
            eventsRepository.save(event);
            return "Success";
        }else {
            return "Error";
        }
    }

    public boolean isPartecipant(String email, Long eventId) {
        HashMap<Long, String> partecipantsId = geIdForEmails(List.of(email));
        Map<String, Long> emailToId = new HashMap<>();
        for (Map.Entry<Long, String> entry : partecipantsId.entrySet()) {
            emailToId.put(entry.getValue(), entry.getKey());
        }
        Long partecipantId = emailToId.get(email);
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        return event.getPartecipants().contains(partecipantId);
    }

    public boolean isAdmin(String email, Long eventId) {
        HashMap<Long, String> adminsId = geIdForEmails(List.of(email));
        Map<String, Long> emailToId = new HashMap<>();
        for (Map.Entry<Long, String> entry : adminsId.entrySet()) {
            emailToId.put(entry.getValue(), entry.getKey());
        }
        Long adminId = emailToId.get(email);
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        return event.getAdmins().contains(adminId) || event.getCreator().equals(adminId);
    }

    public boolean deleteEvent(Long eventId, Long creatorId) {
        try{
            Event event = eventsRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
            if(event.getCreator().equals(creatorId)) {
                List<Partecipant> partecipants = event.getPartecipants();
                for(Partecipant partecipant : partecipants) {
                    partecipant.getEvents().remove(event);
                }
                partecipantsRepository.saveAll(partecipants);
                List<Admin> admins = event.getAdmins();
                for(Admin admin : admins) {
                    admin.getEvents().remove(event);
                }
                adminsRepository.saveAll(admins);
                eventsRepository.deleteById(eventId);
                rabbitTemplate.convertAndSend(eventId);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public boolean isCreator(Long creatorId, Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        return event.getCreator().equals(creatorId);
    }

    public List<EventDTO> getEventsCreated(Long creatorId) {
        List<Event> myEvents = eventsRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : myEvents) {
            if(event.getCreator().equals(creatorId)) {
                EventDTO eventDTO = new EventDTO();
                eventDTO.setTitle(event.getTitle());
                eventDTO.setDescription(event.getDescription());
                eventDTO.setStarting(event.getStarting());
                eventDTO.setEnding(event.getEnding());
                eventDTO.setLocation(event.getLocation());
                eventDTOs.add(eventDTO);
            }
        }
        return eventDTOs;
    }

    public List<String> getAdminsForEvent(Long eventId, Long creatorId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if(event.getCreator().equals(creatorId)) {
            return event.getAdmins().stream().map( admin ->
                admin.getUser().getEmail()
            ).toList();
        }else{
            return null;
        }
    }

    public List<String> getPartecipantsForEvent(Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        return event.getPartecipants().stream().map( partecipant ->
                partecipant.getEmail()
        ).toList();
    }

    public List<Long> getEventId(Long creatorId, String title) {
       List<Event> events = eventsRepository.findAll();
       List<Long> eventIds = new ArrayList<>();
       for (Event event : events) {
           if(event.getCreator().equals(creatorId) && event.getTitle().equals(title)) {
               eventIds.add(event.getEventId());
           }
       }
       return eventIds;
    }

    public List<EventDTO> getEventPartecipated(Long partecipantId) {
        Partecipant partecipant = partecipantsRepository.findById(partecipantId)
                .orElseThrow(() -> new IllegalArgumentException("Partecipant not found: " + partecipantId));
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : partecipant.getEvents()) {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setTitle(event.getTitle());
            eventDTO.setDescription(event.getDescription());
            eventDTO.setStarting(event.getStarting());
            eventDTO.setEnding(event.getEnding());
            eventDTO.setLocation(event.getLocation());
            eventDTOs.add(eventDTO);
        }
        return eventDTOs;
    }

    public HashMap<Long, String> geIdForEmails(List<String> emails) {
        try {
            HashMap<Long, String> result = userManagementWebClient.post()
                    .uri("/info")
                    .bodyValue(emails)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), 
                        response -> response.bodyToMono(String.class)
                            .map(body -> new RuntimeException("Client error: " + body)))
                    .onStatus(status -> status.is5xxServerError(), 
                        response -> response.bodyToMono(String.class)
                            .map(body -> new RuntimeException("Server error: " + body)))
                    .bodyToMono(new ParameterizedTypeReference<HashMap<Long, String>>() {})
                    .block();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user IDs: " + e.getMessage(), e);
        }
    }

    public String annullEvent(Long eventId, Long creatorId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if(event.getCreator().equals(creatorId)) {
            event.setStatus("ANNULLED");
            eventsRepository.save(event);
            return "Success";
        }else{
            return "Error";
        }
    }
}
