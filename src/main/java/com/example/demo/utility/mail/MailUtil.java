package com.example.demo.utility.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public abstract class MailUtil {

    public static final String FORGOTTEN_PASSWORD_FIRST_PART = "Hello, \n If you want to reset your password, please click on the link below:\n";
    public static final String FORGOTTEN_PASSWORD_SECOND_PART = "\nIf this is not you, please ignore this email";
    public static final String USERS_VERIFY_MESSAGE = "Hello, \nTo verify your account please visit \nhttp://localhost:1337/users/verify/";
    public static final String ORDER_CONFIRMATION_MESSAGE = "Hello, \nyou have successfully placed an order from our website.\nYou can check the order details attached below:\n";
    public static final String NEW_FORGOTTEN_PASSWORD = "\nYour new password is: ";
    public static final String FORGOTTEN_PASSWORD_LINK = "\n Please click the lick below to reset your password \nhttp://localhost:1337/users/";
    public static final String RESET_PASSWORD_URL = "/resetPassword/";

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
