package com.example.demo.controller;

import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.mail.MailUtil;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.enums.Notification;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.demo.model.enums.Notification.*;


@RestController
public class ObserverControler extends BaseController {

    @Autowired
    private UserRepository userRepository;

    static final String serverEmailAddress = "technomarket.project@gmail.com";

    private static final int numberOfNotifiedSubscribers = 100;
    private static final int timeToWaitUntilNotifyOtherSubscribers = 1000*60*5;

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void notify(@RequestBody Notification note, HttpSession session) throws TechnoMarketException {

        if (validateAdminLogin(session)) {
            User u = (User) session.getAttribute("userLogged");
            switch (note) {
                case DISCOUNT:
                    sendEmails(DISCOUNT, u.getEmail());
                    break;
                case BLACK_FRIDAY:
                    sendEmails(BLACK_FRIDAY, u.getEmail());
                    break;
                case CRAZY_DAYS:
                    sendEmails(CRAZY_DAYS, u.getEmail());
                    break;
                case CYBER_MONDAY:
                    sendEmails(CYBER_MONDAY, u.getEmail());
                    break;
            }
        }
    }

    @Transactional
    public synchronized void sendEmails(Enum eText, String email){
        new Thread(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            for (User u : userRepository.findAllBySubscribedEquals()){
                notifySubscribed(u, eText);
                counter.getAndIncrement();
                if (counter.get() % numberOfNotifiedSubscribers == 0){
                    System.out.println(counter.get());
                    try {
                        Thread.sleep(timeToWaitUntilNotifyOtherSubscribers);//sleeps for 5 min before sending emails again
                    } catch (InterruptedException e) {
                        log.log(Priority.WARN, e.getMessage(), e);
                        for (User user : userRepository.findAllByUserRoleAdministrator()){
                            try {
                                MailUtil.sendMail(serverEmailAddress, user.getEmail(), "Error sedning mails",
                                        "Emails for subscribing users not send because" + e.getMessage());
                            } catch (MessagingException e1) {
                                log.log(Priority.WARN, e1.getMessage(), e1);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void notifySubscribed(User u, Enum en){
        new Thread(() -> {
            try {
                MailUtil.sendMail(serverEmailAddress, u.getEmail(),
                    en.name(), en.toString());
            } catch (MessagingException e) {
                log.log(Priority.WARN, e.getMessage(), e);
            }
        }).start();
    }
}
