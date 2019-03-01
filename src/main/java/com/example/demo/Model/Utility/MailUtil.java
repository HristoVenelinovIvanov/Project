package com.example.demo.Model.Utility;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public abstract class MailUtil {

    public static final String FORGOTEN_PASSWORD = "Please click the link below to reset your password. \n http://localhost:1337/resetpassword/";
    public static final String PASSWORD_RESET = "Hello, you have just successfully reset your password! \n if that is not you, please contact us at theddy1337@icloud.com or hvivanov@abv.bg";
    public static final String CONFIRM_MESSAGE = "Hello, please do a backflip to verify your account!";

    public static void sendMail(String from, String to, String subject, String content) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("technomarket.project@gmail.com", "Technomarket123");
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, true));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subject);
        msg.setText(content);
        msg.setSentDate(new Date());

        Transport.send(msg);
    }
}
