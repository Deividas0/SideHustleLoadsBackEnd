package com.example.demo.Utility.WebSocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
public class WebSocketController {

    private volatile int totalListings;

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void sendCurrentTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String data = dateTime.format(formatter);
        messagingTemplate.convertAndSend("/topic/time", data);
    }

    public void updateTotalListings(int totalListings) {
        this.totalListings = totalListings;
    }

    @Scheduled(fixedRate = 1000)
    public void sendTotalListings() {
        messagingTemplate.convertAndSend("/topic/totalListings", totalListings);
    }

}
