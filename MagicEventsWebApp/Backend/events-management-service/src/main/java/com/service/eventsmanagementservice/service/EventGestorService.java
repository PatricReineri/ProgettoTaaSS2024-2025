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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventGestorService {
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    AdminsRepository adminsRepository;
    @Autowired
    PartecipantsRepository partecipantsRepository;

    public String updateEventAdmins(ArrayList<Long> admins, Long eventId, Long creatorId) {
        // TODO: check if the request is send by creatorId else return "Error"
        addAdmins(admins, eventId);
        return "Success";
    }
    public String updateEventPartecipants(ArrayList<Long> partecipants, Long eventId, Long creatorId) {
        // TODO: check if the request is send by creatorId else return "Error"
        addPartecipants(partecipants, eventId);
        return "Success";
    }

    public EventDTO getEventInfo(Long eventId) {
        Event event = eventsRepository.findById(eventId).orElse(null);
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
        Event event = new Event(
                eventDTO.getTitle(),
                eventDTO.getDescription(),
                eventDTO.getStarting(),
                eventDTO.getEnding(),
                eventDTO.getLocation(),
                eventDTO.getCreator()
        );
        event = eventsRepository.save(event);
        List<Admin> admins = addAdmins(eventDTO.getAdmins(), event.getEventId());

        event.setAdmins(admins);

        eventsRepository.save(event);

        return event.getEventId();
    }

    // Correctly build of list of admins
    public List<Admin> addAdmins(List<Long> admins, Long eventId){
        List<Partecipant> partecipantList = addPartecipants(new ArrayList<>(admins), eventId);
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        ArrayList<Admin> saveAdmin = new ArrayList<>();
        for (Partecipant adminAccount : partecipantList) {
            ArrayList<Event> events = new ArrayList<>();
            if(adminsRepository.existsByUser(adminAccount)){
                Admin adminUpdate = adminsRepository.findByUser(adminAccount);
                events = new ArrayList<>(adminUpdate.getEvents());
                events.add(event);
                adminUpdate.setEvents(events);
                saveAdmin.add(adminUpdate);
            }else{
                events.add(event);
                Long newAdminId = adminAccount.getMagicEventTag() + eventId;
                Admin newAdmin = new Admin(
                        newAdminId,
                        adminAccount,
                        events
                );
                saveAdmin.add(newAdmin);
            }
        }
        adminsRepository.saveAll(saveAdmin);
        return saveAdmin;
    }

    // Correctly build list of partecipants and update join table via Event owning side
    @Transactional
    public List<Partecipant> addPartecipants(List<Long> partecipantIds, Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        List<Partecipant> added = new ArrayList<>();
        for (Long id : partecipantIds) {
            Partecipant p = partecipantsRepository.findById(id)
                    .orElseGet(() -> {
                        Partecipant newP = new Partecipant();
                        newP.setMagicEventTag(id);
                        return partecipantsRepository.save(newP);
                    });
            if (!event.getPartecipants().contains(p)) {
                event.getPartecipants().add(p);
            }
            added.add(p);
        }
        eventsRepository.save(event);
        return added;
    }
}
