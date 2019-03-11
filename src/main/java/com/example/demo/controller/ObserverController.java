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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import static com.example.demo.model.enums.Notification.*;

@RestController
public class ObserverController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    private static final int numberOfNotifiedSubscribers = 100;
    private static final int timeToWaitUntilNotifyOtherSubscribers = 1000*60*5; // Sleep for 5 minutes.

    @RequestMapping(value = "/notify/{note}", method = RequestMethod.GET)
    public void notify(@PathVariable Notification note, HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException {

        if (validateAdminLogin(session)) {

            switch (note) {
                case DISCOUNT:
                    sendEmails(DISCOUNT);
                    break;
                case BLACK_FRIDAY:
                    sendEmails(BLACK_FRIDAY);
                    break;
                case CRAZY_DAYS:
                    sendEmails(CRAZY_DAYS);
                    break;
                case CYBER_MONDAY:
                    sendEmails(CYBER_MONDAY);
                    break;
            }
            response.getWriter().append("Users notified for " + note.name());
        }
    }

    @Transactional
    public synchronized void sendEmails(Enum eText){
        new Thread(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            for (User u : userRepository.findAllBySubscribed()){
                notifySubscribed(u, eText);
                counter.getAndIncrement();
                if (counter.get() % numberOfNotifiedSubscribers == 0){
                    try {
                        Thread.sleep(timeToWaitUntilNotifyOtherSubscribers);//sleeps for 5 min before sending emails again
                    } catch (InterruptedException e) {
                        log.log(Priority.WARN, e.getMessage(), e);
                        for (User user : userRepository.findAllByUserRoleAdministrator()){
                            try {
                                MailUtil.sendMail(serverEmailAddress, user.getEmail(), "Error sending mails",
                                        "Emails for subscribing users not sent because, " + e.getMessage());
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
