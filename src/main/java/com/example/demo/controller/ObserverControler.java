package com.example.demo.controller;

import com.example.demo.controller.BaseController;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.mail.MailUtil;
import com.example.demo.utility.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.enums.Notification;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.demo.model.enums.Notification.*;


@RestController
public class ObserverControler extends BaseController {

    @Autowired
    private UserRepository userRepository;

    static final String serverEmailAddress = "technomarket.project@gmail.com";

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void notify(@RequestBody Notification note, HttpSession session) throws TechnoMarketException {

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
        }
    }

    @Transactional
    public synchronized void sendEmails(Enum eText){
        new Thread(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            for (User u : userRepository.findAllBySubscribedEquals()){
                notifySubscribed(u, eText);
                counter.getAndIncrement();
                if (counter.get()%100 == 0){
                    System.out.println(counter.get());
                    try {
                        Thread.sleep(1000*60*5);//sleeps for 5 min before sending emails again
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                //TODO Deal with email not sending AND make the method in transaction
            }
        }).start();
    }
}
