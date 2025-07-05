package com.service.eventsmanagementservice.service;

import com.service.eventsmanagementservice.dto.EventDeletionAckDTO;
import com.service.eventsmanagementservice.model.Admin;
import com.service.eventsmanagementservice.model.Event;
import com.service.eventsmanagementservice.model.Partecipant;
import com.service.eventsmanagementservice.repository.AdminsRepository;
import com.service.eventsmanagementservice.repository.EventsRepository;
import com.service.eventsmanagementservice.repository.PartecipantsRepository;
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

    @Value("${spring.rabbitmq.exchange.event}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routing-key.delete-event-board}")
    private String deleteBoardRoutingKey;
    @Value("${spring.rabbitmq.routing-key.delete-event-gallery}")
    private String deleteGalleryRoutingKey;
    @Value("${spring.rabbitmq.routing-key.delete-event-guestgame}")
    private String deleteGuestgameRoutingKey;

    @RabbitListener(queues = "${spring.rabbitmq.queue.delete-ack}")
    @Transactional
    public void deleteEvent(EventDeletionAckDTO eventDeleting) {
        try {
            Event event = eventsRepository.findById(eventDeleting.getEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventDeleting.getEventId()));
            switch (eventDeleting.getServiceType()) {
                case "board":
                    if(!eventDeleting.getIsSuccess()){
                        rabbitTemplate.convertAndSend(exchangeName, deleteBoardRoutingKey, eventDeleting.getEventId());
                    }else{
                        System.out.println("board deleted for event: " + eventDeleting.getEventId());
                        event.setBoardEnabled(false);
                    }
                    break;
                case "gallery":
                    if(!eventDeleting.getIsSuccess()){
                        rabbitTemplate.convertAndSend(exchangeName, deleteGalleryRoutingKey, eventDeleting.getEventId());
                    }else {
                        System.out.println("gallery deleted for event: " + eventDeleting.getEventId());
                        event.setGalleryEnabled(false);
                    }
                    break;
                case "guest-game":
                    if(!eventDeleting.getIsSuccess()){
                        rabbitTemplate.convertAndSend(exchangeName, deleteGuestgameRoutingKey, eventDeleting.getEventId());
                    }else {
                        System.out.println("guest-game deleted for event: " + eventDeleting.getEventId());
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
                System.out.println("deleting event: " + eventDeleting.getEventId() + "...");
                eventsRepository.deleteById(eventDeleting.getEventId());
            }else{
                eventsRepository.save(event);
            }
        } catch (Exception e) {
        }
    }
}

