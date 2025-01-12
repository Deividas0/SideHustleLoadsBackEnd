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
                        "find loads to deliver or place your loads for others.\n\n" +
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
    public void successfulTopUpEmail(String recipientEmail, String username, int amount) {
        String from = "info@webdevpro.site";
        String fromName = "Side Hustle Loads";
        String subject = "Your Top-Up Was Successful!";
        String content = String.format(
                "Hi %s,\n\n" +
                        "We’re excited to let you know that your top-up of %d EUR was successful!\n\n" +
                        "Your account has been credited, and your updated balance is ready to use. " +
                        "Make the most of your account by exploring everything Side Hustle Loads has to offer!\n\n" +
                        "Here’s a summary of your top-up:\n" +
                        "---------------------------------\n" +
                        "Amount Topped Up: %d EUR\n" +
                        "---------------------------------\n\n" +
                        "If you have any questions or need assistance, feel free to reach out to our support team at support@webdevpro.site.\n\n" +
                        "Thank you for choosing Side Hustle Loads!\n\n" +
                        "Best regards,\n" +
                        "The Side Hustle Loads Team",
                username, amount, amount);

        try {
            emailsRepository.sendEmail(from, fromName, recipientEmail, subject, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
