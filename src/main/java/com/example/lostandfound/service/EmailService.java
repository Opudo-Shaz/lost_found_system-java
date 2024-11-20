package com.example.lostandfound.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendEmail(String recipientEmail, String subject, String body) {
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            throw new IllegalArgumentException("Recipient email is missing or invalid.");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("opudosharon6@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


    public void sendClaimNotificationEmail(
            String finderEmail,
            String claimedBy,
            String itemName,
            String claimerContact,
            String claimerNote,
            String claimerImages) {

        String subject = "Claim Placed for Your Found Item";

        String body = String.format("""
                Hello,

                A claim has been placed for your found item: '%s' by %s.

                Claimer's Contact: %s
                Claimer's Note: %s

                Please review the claim and take the necessary actions. Below are the details to help you verify the claim:
                
                Claimer's Images: %s

                Best regards,
                Patika Lost & Found Team
                """,
                itemName,
                claimedBy,
                claimerContact,
                claimerNote,
                claimerImages);

        sendEmail(finderEmail, subject, body);
    }

    public void sendClaimantNotificationEmail(
            String claimantEmail,
            String itemName,
            String finderContact,
            String locationFound) {

        String subject = "Your Claim Submission Details";

        String body = String.format("""
            Hello,

            Thank you for submitting a claim for the item: '%s'.

            The finder has been notified and will review your claim. 
            Here are the details of the finder to help you follow up:

            Finder's Contact: %s
            Found Item Location: %s

            Please wait for the finder to contact you or feel free to reach out to them directly if needed.

            Best regards,
            Patika Lost & Found Team
            """, itemName, finderContact, locationFound);

        sendEmail(claimantEmail, subject, body);
    }

}
