package com.service.eventsmanagementservice.controller;

import com.service.eventsmanagementservice.dto.EventDTO;
import com.service.eventsmanagementservice.dto.ServicesDTO;
import com.service.eventsmanagementservice.service.EventGestorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.service.eventsmanagementservice.exception.UnauthorizedException;
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
    public ResponseEntity<Long> createEvent(
            @Valid @RequestBody EventDTO eventDTO,
            @RequestParam("creatorEmail") String creatorEmail
    ) {
        for(String partecipantEmail : eventDTO.getPartecipants()){
            if(eventDTO.getAdmins().contains(partecipantEmail)){
                eventDTO.getPartecipants().remove(partecipantEmail);
            }
        }
        if(eventDTO.getEnding().isAfter(eventDTO.getStarting())) {
            Long eventId = eventGestorService.create(eventDTO, creatorEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventId);
        }else{
            /// Invalid event ending value
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }

    @PutMapping("/annullevent")
    public ResponseEntity<String> annullEvent(
            @RequestParam("eventId") Long eventId,
            @RequestParam("magicEventsTag") Long creatorId
    ) {
        String response = eventGestorService.annullEvent(eventId, creatorId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("/activeservices")
    public ResponseEntity<String> activeServicesEvent(
            @RequestParam("eventId") Long eventId,
            @RequestParam("magicEventsTag") Long creatorId,
            @Valid @RequestBody ServicesDTO servicesDTO
    ) {
        String response = eventGestorService.activeServicesEvent(eventId, creatorId, servicesDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("/de-annullevent")
    public ResponseEntity<String> activeEvent(
            @RequestParam("eventId") Long eventId,
            @RequestParam("magicEventsTag") Long creatorId
    ) {
        String response = eventGestorService.activeEvent(eventId, creatorId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("/updateadmins")
    public String addNewAdmins(
            @RequestParam("admins") ArrayList<String> admins,
            @RequestParam("eventId") Long eventId,
            @RequestParam("magicEventsTag") Long creatorId
    ) {
        return eventGestorService.updateEventAdmins(admins, eventId, creatorId);
    }

    @PutMapping("/addpartecipants")
    public String addNewPartecipants(
            @RequestParam("partecipants") ArrayList<String> partecipants,
            @RequestParam("eventId") Long eventId,
            @RequestParam("magicEventsTag") Long creatorId
    ) {
        return eventGestorService.updateEventPartecipants(partecipants, eventId, creatorId);
    }

    @GetMapping("/geteventinfo")
    public EventDTO getEventInfo(@RequestParam("eventId") Long eventId) {
        return eventGestorService.getEventInfo(eventId);
    }

    @PutMapping("/modify")
    public String modifyEvent(
            @RequestParam("eventId") Long eventId,
            @RequestParam("magicEventsTag") Long creatorId,
            @Valid @RequestBody EventDTO eventDTO
    ) {
        return eventGestorService.modifyEvent(eventDTO, creatorId, eventId);
    }

    @GetMapping("/ispartecipant")
    public boolean isPartecipant(@RequestParam("partecipantId") Long magicEventsTag, @RequestParam("eventId") Long eventId) {
        return eventGestorService.isPartecipant(magicEventsTag, eventId);
    }

    @GetMapping("/isadmin")
    public boolean isAdmin(@RequestParam("partecipantId") Long magicEventsTag, @RequestParam("eventId") Long eventId){
        return eventGestorService.isAdmin(magicEventsTag, eventId);
    }

    @GetMapping("/iscreator")
    public boolean isCreator(@RequestParam("creatorId") Long creatorId, @RequestParam("eventId") Long eventId){
        return eventGestorService.isCreator(creatorId, eventId);
    }

    @GetMapping("/geteventslistc")
    public List<EventDTO> getEventsCreated(@RequestParam("creatorId") Long creatorId){
        return eventGestorService.getEventsCreated(creatorId);
    }

    @GetMapping("/geteventslistp")
    public List<EventDTO> getEventPartecipated(@RequestParam("partecipantId") Long partecipantId){
        return eventGestorService.getEventPartecipated(partecipantId);
    }

    @GetMapping("/geteventid")
    public List<Long> getEventId(@RequestParam("creatorId") Long creatorId, @RequestParam("title") String title){
        return eventGestorService.getEventId(creatorId, title);
    }

    @GetMapping("/getadminsforevent")
    public List<String> getAdmins(@RequestParam("eventId") Long eventId, @RequestParam("magicEventsTag") Long creatorId){
        return eventGestorService.getAdminsForEvent(eventId, creatorId);
    }

    @GetMapping("/getpartecipantsforevent")
    public List<String> getPartecipants(@RequestParam("eventId") Long eventId){
        return eventGestorService.getPartecipantsForEvent(eventId);
    }

    @DeleteMapping("/delete")
    public boolean deleteEvent(@RequestParam("eventId") Long eventId, @RequestParam("magicEventsTag") Long creatorId){
        return eventGestorService.delete(eventId, creatorId);
    }

    @GetMapping("/getEventEnabledServices")
    public ResponseEntity<ServicesDTO> getEventEnabledServices(
            @RequestParam("eventId") Long eventId,
            @RequestParam("magicEventsTag") Long magicEventsTag
    ) {
        try {
            ServicesDTO services = eventGestorService.getEventEnabledServices(eventId, magicEventsTag);
            if (services != null) {
                return ResponseEntity.ok(services);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}



