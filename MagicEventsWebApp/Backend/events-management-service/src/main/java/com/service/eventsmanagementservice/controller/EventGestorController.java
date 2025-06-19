package com.service.eventsmanagementservice.controller;

import com.service.eventsmanagementservice.dto.EventDTO;
import com.service.eventsmanagementservice.model.Partecipant;
import com.service.eventsmanagementservice.service.EventGestorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
        for(Long partecipantId : eventDTO.getPartecipants()){
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
            @RequestParam("admins") ArrayList<Long> admins,
            @RequestParam("eventId") Long eventId,
            @RequestParam("creatorId") Long creatorId
    ) {
        return eventGestorService.updateEventAdmins(admins, eventId, creatorId);
    }

    @GetMapping("/addpartecipants")
    public String addNewPartecipants(
            @RequestParam("partecipants") ArrayList<Long> partecipants,
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

    @GetMapping("/isPartecipant")
    public boolean isPartecipant(@RequestParam("partecipantId") Long magicEventsTag, @RequestParam("eventId") Long eventId) {
        return eventGestorService.isPartecipant(magicEventsTag, eventId);
    }

    @GetMapping("/isAdmin")
    public boolean isAdmin(@RequestParam("partecipantId") Long magicEventsTag, @RequestParam("eventId") Long eventId){
        return eventGestorService.isAdmin(magicEventsTag, eventId);
    }

    @GetMapping("/delete")
    public boolean deleteEvent(@RequestParam("eventId") Long eventId){
        return eventGestorService.deleteEvent(eventId);
    }
}
