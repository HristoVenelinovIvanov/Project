package com.example.demo.utility.mail;

import net.bytebuddy.utility.RandomString;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public abstract class MailUtil {

    public static final String FORGOTTEN_PASSWORD = "Hello, \nPlease see your new password below. \n";
    public static final String PASSWORD_RESET = "Hello, \nyou have successfully reset your password! \n if that is not you, please contact us at theddy1337@icloud.com or hvivanov@abv.bg";
    public static final String CONFIRM_MESSAGE = "Hello, \nTo verify your account please visit \nhttp://localhost:1337/users/verify/";
    public static final String ORDER_CONFIRMATION_MESSAGE = "Hello, \nyou have successfully placed an order from our website.\nYou can check the order details attached below:\n";

    public static void sendMail(String from, String to, String subject, String content) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
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
