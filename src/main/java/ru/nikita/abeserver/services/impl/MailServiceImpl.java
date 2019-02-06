package ru.nikita.abeserver.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.services.MailService;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {
    private static String USERNAME;
    private static String PASSWORD;

    @PostConstruct
    private void init(){

        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            prop.load(resourceAsStream);
            USERNAME = prop.getProperty("mail.login");
            PASSWORD = prop.getProperty("mail.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SendFileForUser(User user, File file){
        String html = "<h1>Вас приветствует сервис ABE SERVER</h1>\n" +
                "<p>Вам предоставлен доступ к файлу:</p>\n" +
                "<p>"+file.getName() + ". Guid для скачки: " +file.getGuid()+"</p>\n" +
                "<br/>\n" +
                "<p>Благодарим за использование нашего сервиса.</p>";


        sendHtmlMail(user.getEmail(), "ABE SERVER" , html);
    }

    @Override
    public void sendTextMail(String to, String subject, String text){
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.yandex.ru");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(USERNAME, PASSWORD);
                        }
                    });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendHtmlMail(String to, String subject, String html){
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.yandex.ru");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(USERNAME, PASSWORD);
                        }
                    });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(html, "UTF-8", "html");

            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }





}
