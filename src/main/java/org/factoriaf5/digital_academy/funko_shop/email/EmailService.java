package org.factoriaf5.digital_academy.funko_shop.email;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendSubscriptionEmail(String to, String unsubscribeLink) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("Funko Shop <postmaster@sandbox1ea7252baf014dd581369f6dc8cd4d89.mailgun.org>");
            helper.setTo(to);
            helper.setSubject("Subscription Confirmation");

            Context context = new Context();
            context.setVariable("unsubscribeLink", unsubscribeLink);

            String htmlContent = templateEngine.process("email/subscription-email", context);

            helper.setText(htmlContent, true);

            emailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}
