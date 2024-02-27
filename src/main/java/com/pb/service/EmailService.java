package com.pb.service;

import com.pb.config.EmailConfig;
import com.pb.model.EmailTemplateEntity;
import com.pb.repository.EmailTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {
    
    @Autowired
    private EmailConfig emailConfig;

    static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";
    
//    static final String BODY = String.join(
//            System.getProperty("line.separator"),
//            "<h1>Amazon SES SMTP Email Test</h1>",
//            "<p>This email was sent with Amazon SES using the ",
//            "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
//            " for <a href='https://www.java.com'>Java</a>."
//    );
    
    public void sendEmail(String to,String subject,String body) throws MessagingException, UnsupportedEncodingException {

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", emailConfig.getPort());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailConfig.getFromEmail(),""));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(body,"text/html");

        Transport transport = session.getTransport();
        try
        {
            transport.connect(emailConfig.getHost(),emailConfig.getUsername(),emailConfig.getPassword());
            transport.sendMessage(msg, msg.getAllRecipients());

            log.info("Email Send Successfully.");
        }
        catch (Exception ex) {
            log.error("The email was not sent.");
            log.error("Error message: " + ex.getMessage());
        }
        finally
        {
            transport.close();
        }
    }
}
