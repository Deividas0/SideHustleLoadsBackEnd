package com.example.demo.Emails;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class EmailsRepository {

    private final SendGrid sendGrid;

    public EmailsRepository(@Value("${sendgrid.api.key}") String apiKey) {
        this.sendGrid = new SendGrid(apiKey);
    }

    public Response sendEmail(String from, String fromName, String to, String subject, String content) throws IOException {
        Email fromEmail = new Email(from, fromName);
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(fromEmail, subject, toEmail, emailContent);


        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            return sendGrid.api(request);
        } catch (IOException ex) {
            throw new IOException("Error sending email", ex);
        }
    }
}
