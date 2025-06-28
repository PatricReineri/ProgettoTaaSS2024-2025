package com.service.eventsmanagementservice.service;

import com.service.eventsmanagementservice.model.Admin;
import com.service.eventsmanagementservice.model.Event;
import com.service.eventsmanagementservice.model.Partecipant;
import com.service.eventsmanagementservice.repository.AdminsRepository;
import com.service.eventsmanagementservice.repository.EventsRepository;
import com.service.eventsmanagementservice.repository.PartecipantsRepository;
import org.antlr.v4.runtime.misc.Triple;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class AckMessageListener {
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    AdminsRepository adminsRepository;
    @Autowired
    PartecipantsRepository partecipantsRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routing-key.delete-event-board}")
    private String deleteBoardRoutingKey;
    @Value("${spring.rabbitmq.routing-key.delete-event-gallery}")
    private String deleteGalleryRoutingKey;
    @Value("${spring.rabbitmq.routing-key.delete-event-guestgame}")
    private String deleteGuestgameRoutingKey;

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-ack}")
    @Transactional
    public void deleteEvent(Triple<Long, String, Boolean> eventDeleting) {
        try {
            Event event = eventsRepository.findById(eventDeleting.a)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventDeleting.a));
            switch (eventDeleting.b) {
                case "board":
                    if(!eventDeleting.c){
                        rabbitTemplate.convertAndSend(deleteBoardRoutingKey, eventDeleting.a);
                    }else{
                        event.setBoardEnabled(false);
                    }
                    break;
                case "gallery":
                    if(!eventDeleting.c){
                        rabbitTemplate.convertAndSend(deleteGalleryRoutingKey, eventDeleting.a);
                    }else {
                        event.setGalleryEnabled(false);
                    }
                    break;
                case "guest-game":
                    if(!eventDeleting.c){
                        rabbitTemplate.convertAndSend(deleteGuestgameRoutingKey, eventDeleting.a);
                    }else {
                        event.setGuestGameEnabled(false);
                    }
                    break;
            }
            if(!(event.getBoardEnabled() && event.getGalleryEnabled() && event.getGuestGameEnabled())) {
                List<Partecipant> partecipants = event.getPartecipants();
                for (Partecipant partecipant : partecipants) {
                    partecipant.getEvents().remove(event);
                }
                partecipantsRepository.saveAll(partecipants);
                List<Admin> admins = event.getAdmins();
                for (Admin admin : admins) {
                    admin.getEvents().remove(event);
                }
                adminsRepository.saveAll(admins);
                eventsRepository.deleteById(eventDeleting.a);
            }else{
                eventsRepository.save(event);
            }
        } catch (Exception e) {
        }
    }
}

