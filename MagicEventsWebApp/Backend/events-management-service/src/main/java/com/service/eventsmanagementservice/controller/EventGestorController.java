package com.service.eventsmanagementservice.controller;

import com.service.eventsmanagementservice.dto.EventDTO;
import com.service.eventsmanagementservice.model.Admin;
import com.service.eventsmanagementservice.model.Partecipant;
import com.service.eventsmanagementservice.service.EventGestorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gestion")
public class EventGestorController {
    private final EventGestorService eventGestorService;

    public EventGestorController(
            EventGestorService eventGestorService
    ) {
        this.eventGestorService = eventGestorService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createEvent(@RequestBody EventDTO eventDTO) {
        ArrayList<Long> partecipantIds = eventGestorService.getPartecipantsId(eventDTO.getPartecipants());
        for(Long partecipantId : partecipantIds){
            if(eventDTO.getAdmins().contains(partecipantId)){
                eventDTO.getPartecipants().remove(partecipantId);
            }
        }
        if(eventDTO.getEnding().isAfter(eventDTO.getStarting())) {
            Long eventId = eventGestorService.create(eventDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventId);
        }else{
            /** Code for internal error: invalid event ending value */
            return ResponseEntity.status(510).body(-1L);
        }
    }

    @GetMapping("/updateadmins")
    public String addNewAdmins(
            @RequestParam("admins") ArrayList<String> admins,
            @RequestParam("eventId") Long eventId,
            @RequestParam("creatorId") Long creatorId
    ) {
        return eventGestorService.updateEventAdmins(admins, eventId, creatorId);
    }

    @GetMapping("/addpartecipants")
    public String addNewPartecipants(
            @RequestParam("partecipants") ArrayList<String> partecipants,
            @RequestParam("eventId") Long eventId,
            @RequestParam("creatorId") Long creatorId
            ) {
        return eventGestorService.updateEventPartecipants(partecipants, eventId, creatorId);
    }

    @PostMapping("/geteventinfo")
    public EventDTO getEventInfo(@RequestParam("eventId") Long eventId) {
        return eventGestorService.getEventInfo(eventId);
    }

    @PostMapping("/modify")
    public String modifyEvent(
            @RequestParam("eventId") Long eventId,
            @RequestParam("creatorId") Long creatorId,
            @RequestBody EventDTO eventDTO
    ) {
        return eventGestorService.modifyEvent(eventDTO, creatorId, eventId);
    }

    @GetMapping("/ispartecipant")
    public boolean isPartecipant(@RequestParam("partecipantId") String partecipant, @RequestParam("eventId") Long eventId) {
        return eventGestorService.isPartecipant(partecipant, eventId);
    }

    @GetMapping("/isadmin")
    public boolean isAdmin(@RequestParam("partecipantId") String partecipant, @RequestParam("eventId") Long eventId){
        return eventGestorService.isAdmin(partecipant, eventId);
    }

    @GetMapping("/iscreator")
    public boolean isCreator(@RequestParam("creatorId") Long creatorId, @RequestParam("eventId") Long eventId){
        return eventGestorService.isCreator(creatorId, eventId);
    }

    @PostMapping("/geteventslistc")
    public List<EventDTO> getEventsCreated(@RequestParam("creatorId") Long creatorId){
        return eventGestorService.getEventsCreated(creatorId);
    }

    @PostMapping("/geteventslistp")
    public List<EventDTO> getEventPartecipated(@RequestParam("partecipantId") Long partecipantId){
        return eventGestorService.getEventPartecipated(partecipantId);
    }

    @PostMapping("/geteventid")
    public List<Long> getEventId(@RequestParam("creatorId") Long creatorId, @RequestParam("title") String title){
        return eventGestorService.getEventId(creatorId, title);
    }

    @PostMapping("/getadminsforevent")
    public List<String> getAdmins(@RequestParam("eventId") Long eventId, @RequestParam("creatorId") Long creatorId){
        return eventGestorService.getAdminsForEvent(eventId, creatorId);
    }

    @PostMapping("/getpartecipantsforevent")
    public List<String> getPartecipants(@RequestParam("eventId") Long eventId){
        return eventGestorService.getPartecipantsForEvent(eventId);
    }

    @GetMapping("/delete")
    public boolean deleteEvent(@RequestParam("eventId") Long eventId, @RequestParam("creatorId") Long creatorId){
        return eventGestorService.deleteEvent(eventId, creatorId);
    }
}
