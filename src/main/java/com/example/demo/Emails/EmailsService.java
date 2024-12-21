package com.example.demo.Emails;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailsService {

    private final EmailsRepository emailsRepository;

    public EmailsService(EmailsRepository emailsRepository) {
        this.emailsRepository = emailsRepository;
    }

    public void registrationEmail(String recipientEmail, String username) {
        String from = "info@webdevpro.site";
        String fromName = "Welcome!";
        String subject = "Welcome to Side Hustle Loads!";
        String content = String.format(
                        "Welcome to Side Hustle Loads!\n\n" +
                        "We’re thrilled to have you join our community where you can " +
                        "connect with people worldwide to request, offer, and exchange services.\n\n" +
                        "Feel free to explore and get started with your journey.\n\n" +
                        "Best regards,\n" +
                        "The Side Hustle Loads Team");

        try {
            emailsRepository.sendEmail(from, fromName, recipientEmail, subject, content);
            System.out.println("Welcome email sent to: " + recipientEmail);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to send welcome email: " + e.getMessage());
        }
    }
}
