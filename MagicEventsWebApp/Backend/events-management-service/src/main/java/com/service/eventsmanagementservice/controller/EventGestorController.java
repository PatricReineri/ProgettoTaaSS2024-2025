package com.service.eventsmanagementservice.controller;

import com.service.eventsmanagementservice.dto.EventDTO;
import com.service.eventsmanagementservice.service.EventGestorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestion")
public class EventGestorController {
    private final EventGestorService eventGestorService;

    public EventGestorController(EventGestorService eventGestorService) {
        this.eventGestorService = eventGestorService;
    }

    @PostMapping("/create")
    public Long createEvent(@RequestBody EventDTO eventDTO) {
        return eventGestorService.create(eventDTO);
    }

    /*@GetMapping("/addadmins")
    public String addNewAdmins(@RequestBody List<String> admins) {
        return eventGestorService.addAdmins(admins);
    }*/

    /*@PostMapping("/geteventinfo")
    public EventDTO getEventInfo(@RequestParam("eventId") Long eventId) {
        return eventGestorService.getEventInfo(admins);
    }*/
}
