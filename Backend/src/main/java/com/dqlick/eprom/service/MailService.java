package com.dqlick.eprom.service;

import com.dqlick.eprom.Shared.SharedObjectService;
import com.dqlick.eprom.domain.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final  SharedObjectService sharedObjectService ;



    public MailService(
            JHipsterProperties jHipsterProperties,
            JavaMailSender javaMailSender,
            MessageSource messageSource,
            SpringTemplateEngine templateEngine,
            SharedObjectService sharedObjectService
        ) {
            this.jHipsterProperties = jHipsterProperties;
            this.javaMailSender = javaMailSender;
            this.messageSource = messageSource;
            this.templateEngine = templateEngine;
            this.sharedObjectService = sharedObjectService;
        }
	@Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml,String username,String password) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );


    	Properties props = new Properties();
    	props.put("mail.smtp.auth", true);
    	props.put("mail.smtp.starttls.enable", true);
//    	props.put("mail.smtp.host", "smtp-mail.outlook.com");
        if (username.toLowerCase().endsWith("@gmail.com")) {
            props.put("mail.smtp.host", "smtp.gmail.com");
        } else {
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
        }
    	props.put("mail.smtp.port", "587");

    	Session session = Session.getInstance(props,
    	    new javax.mail.Authenticator() {
    	        protected PasswordAuthentication getPasswordAuthentication() {
    	            return new PasswordAuthentication(username, password);
    	        }
    	    }
    	);
    	MimeMessage mimeMessage = new MimeMessage(session);
    	try {

    	    Message message = new MimeMessage(session);
    	    message.setFrom(new InternetAddress(username)); 
    	    message.setSubject(subject);
    	    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

    	    MimeBodyPart messageBodyPart = new MimeBodyPart();
    	    messageBodyPart.setText(content, "UTF-8", isHtml ? "html" : "plain");

    	    Multipart multipart = new MimeMultipart();
    	    multipart.addBodyPart(messageBodyPart);

    	    message.setContent(multipart);

    	    Transport.send(message);
    	    System.out.println("Done");
    	} catch (MailException | MessagingException e) {
    	    log.warn("Email could not be sent to user '{}'", to, e);
    	}


    }
    @Async
    public void sendEmailWithAttachment(String to, String subject, String content, boolean isMultipart, boolean isHtml, String attachmentPath) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            message.addAttachment("Attachment", new File(attachmentPath));
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}' with attachment", to);
        } catch (MailException | MessagingException  e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }



    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey,String username,String password) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true,username,password);
    }

    @Async
    public void sendActivationEmail(User user,String username,String password) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title",username,password);
    }

    @Async
    public void sendCreationEmail(User user,String username,String password) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title",username,password);
    }

    @Async
    public void sendPasswordResetMail(User user,String username,String password) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title",username,password);
    }

    @Async
    public void sendConnectionEmail(User user,String username,String password) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/connectEmail", "email.connection.title",username,password);
    }

}
