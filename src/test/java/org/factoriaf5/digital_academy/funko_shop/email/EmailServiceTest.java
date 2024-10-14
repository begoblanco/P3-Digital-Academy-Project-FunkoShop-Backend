package org.factoriaf5.digital_academy.funko_shop.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
    }

    @Test
    void sendSubscriptionEmail_SendsEmailSuccessfully() throws MessagingException {
        String to = "test@example.com";
        String unsubscribeLink = "http://example.com/unsubscribe";
        String htmlContent = "<html><body>Subscription email</body></html>";

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("email/subscription-email"), any(Context.class))).thenReturn(htmlContent);

        emailService.sendSubscriptionEmail(to, unsubscribeLink);

        verify(emailSender).send(any(MimeMessage.class));
        verify(templateEngine).process(eq("email/subscription-email"), any(Context.class));
    }

    @Test
    void sendSubscriptionEmail_CatchesMessagingException() {
        String to = "test@example.com";
        String unsubscribeLink = "http://example.com/unsubscribe";
        String htmlContent = "<html><body>Subscription email</body></html>";  

        RuntimeException exception = new RuntimeException("Test Runtime Exception");

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("email/subscription-email"), any(Context.class))).thenReturn(htmlContent);
        doThrow(exception).when(emailSender).send(any(MimeMessage.class));

        try {
            emailService.sendSubscriptionEmail(to, unsubscribeLink);
        } catch (RuntimeException e) {
            assert e.getMessage().contains("Test Runtime Exception");
        }

        verify(emailSender).send(any(MimeMessage.class));  
    }
}
