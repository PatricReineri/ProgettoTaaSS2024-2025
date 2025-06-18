package com.service.eventsmanagementservice.controller;

import com.service.eventsmanagementservice.dto.EventDTO;
import com.service.eventsmanagementservice.service.EventGestorService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/gestion")
public class EventGestorController {
    private final EventGestorService eventGestorService;

    public EventGestorController(EventGestorService eventGestorService) {
        this.eventGestorService = eventGestorService;
    }

    @PostMapping("/create")
    public Long createEvent(@RequestBody EventDTO eventDTO) {
        // TODO: check if end of event is after start else return "Error"
        return eventGestorService.create(eventDTO);
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
}
