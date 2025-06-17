package com.service.eventsmanagementservice.service;

import com.service.eventsmanagementservice.dto.EventDTO;
import com.service.eventsmanagementservice.model.Admin;
import com.service.eventsmanagementservice.model.Event;
import com.service.eventsmanagementservice.model.User;
import com.service.eventsmanagementservice.repository.AdminsRepository;
import com.service.eventsmanagementservice.repository.EventsRepository;
import com.service.eventsmanagementservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventGestorService {
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    AdminsRepository adminsRepository;
    @Autowired
    UserRepository userRepository;

    /*public String addAdmins(List<String> admins, Long eventId) {
    }*/

    /*public EventDTO getEventInfo(Long eventId) {
    }*/

    public Long create(EventDTO eventDTO) {
        User user = userRepository.findById(eventDTO.getCreator()).orElse(null);
        Event event = new Event(
                eventDTO.getTitle(),
                eventDTO.getDescription(),
                eventDTO.getStart(),
                eventDTO.getEnd(),
                eventDTO.getLocation(),
                user,
                null
        );
        Event eventCreated = eventsRepository.save(event);
        List<Admin> admins = updateEventAdmins(eventDTO.getAdmins(), eventCreated.getEventId());
        eventCreated.setAdmins(admins);
        eventsRepository.save(eventCreated);
        return eventCreated.getEventId();
    }

    public List<Admin> updateEventAdmins(List<Long> admins, Long eventId){
        List<User> adminsAccount = userRepository.findAllById(admins);
        Event event = eventsRepository.findById(eventId).orElse(null);
        List<Admin> saveAdmin = List.of();
        for (User adminAccount : adminsAccount) {
            List<Event> events = List.of();
            if(adminsRepository.existsByUser(adminAccount)){
                Admin adminUpdate = adminsRepository.findByUser(adminAccount);
                List<Event> currentEventsList = adminUpdate.getEvent();
                currentEventsList.add(event);
                adminUpdate.setEvent(currentEventsList);
                adminsRepository.save(adminUpdate);
                saveAdmin.add(adminUpdate);
            }else{
                events.add(event);
                String newAdminId = adminAccount.getMagicEventTag() + "-" + eventId;
                Admin newAdmin = new Admin(
                        newAdminId,
                        adminAccount,
                        events
                );
                saveAdmin.add(newAdmin);
            }
        }
        return saveAdmin;
    }
}
