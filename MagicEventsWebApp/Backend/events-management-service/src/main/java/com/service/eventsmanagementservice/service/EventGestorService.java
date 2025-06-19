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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;

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

    public String updateEventAdmins(ArrayList<Long> admins, Long eventId, Long creatorId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        if(event.getCreator().equals(creatorId)) {
            addAdmins(admins, eventId);
            return "Success";
        }else{
            return "Error";
        }
    }
    public String updateEventPartecipants(ArrayList<Long> partecipants, Long eventId, Long creatorId) {
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
        ArrayList<Long> admins = new ArrayList<>();
        for (Admin admin : event.getAdmins()) {
            admins.add(admin.getAdminId());
        }
        ArrayList<Long> partecipants = new ArrayList<>();
        for (Partecipant partecipant : event.getPartecipants()) {
            partecipants.add(partecipant.getMagicEventTag());
        }
        return new EventDTO(
                event.getTitle(),
                event.getDescription(),
                event.getStarting(),
                event.getEnding(),
                event.getLocation(),
                event.getCreator(),
                admins,
                partecipants
        );
    }

    @Transactional
    public Long create(EventDTO eventDTO) {
        // Ensure creator exists as a Partecipant
        Long creatorId = eventDTO.getCreator();
        partecipantsRepository.findById(creatorId)
            .orElseGet(() -> {
                Partecipant newP = new Partecipant();
                newP.setMagicEventTag(creatorId);
                return partecipantsRepository.saveAndFlush(newP);
            });
        Event event = new Event(
                eventDTO.getTitle(),
                eventDTO.getDescription(),
                eventDTO.getStarting(),
                eventDTO.getEnding(),
                eventDTO.getLocation(),
                creatorId
        );
        event = eventsRepository.save(event);
        addAdmins(eventDTO.getAdmins(), event.getEventId());
        return event.getEventId();
    }

    @Transactional
    public List<Admin> addAdmins(List<Long> admins, Long eventId){
        List<Partecipant> partecipantList = addPartecipants(new ArrayList<>(admins), eventId);
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

    @Transactional
    public List<Partecipant> addPartecipants(List<Long> partecipantIds, Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        List<Partecipant> added = new ArrayList<>();
        for (Long id : partecipantIds) {
            Partecipant partecipant = partecipantsRepository.findById(id)
                    .orElseGet(() -> {
                        Partecipant newP = new Partecipant();
                        newP.setMagicEventTag(id);
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
            eventsRepository.save(event);
            return "Success";
        }else {
            return "Error";
        }
    }

    public boolean isPartecipant(Long magicEventsTag, Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        return event.getPartecipants().contains(magicEventsTag);
    }

    public boolean isAdmin(Long magicEventsTag, Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        return event.getAdmins().contains(magicEventsTag) || event.getCreator().equals(magicEventsTag);
    }

    public boolean deleteEvent(Long eventId) {
        try{
            eventsRepository.deleteById(eventId);
            rabbitTemplate.convertAndSend(eventId);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
