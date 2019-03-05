package com.example.demo.model.notification;

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

import static com.example.demo.model.enums.Notification.*;


@RestController
public class Observer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserValidator userValidator;


    static final String serverEmailAddress = "technomarket.project@gmail.com";

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void notify(@RequestBody Notification note, HttpSession session) throws TechnoMarketException {

        User u = (User) session.getAttribute("userLogged");
        if (userValidator.isAdmin(u)) {
            switch (note) {
                case DISCOUNT:
                    eventNotify(DISCOUNT);
                    break;
                case BLACK_FRIDAY:
                    eventNotify(BLACK_FRIDAY);
                    break;
                case CRAZY_DAYS:
                    eventNotify(CRAZY_DAYS);
                    break;
                case CYBER_MONDAY:
                    eventNotify(CYBER_MONDAY);
                    break;
            }
        }
    }

    public void eventNotify(Enum eText){
        new Thread(() -> {
            for (User u : userRepository.findAllBySubscribe()){
                try {
                    sendMail(u, eText);
                } catch (MessagingException e) {
                    //TODO deal with MessegingExeption
                }
            }
        }).start();
    }

    private void sendMail(User u, Enum event) throws MessagingException{
        new Thread(() -> {
            try {
                MailUtil.sendMail(serverEmailAddress, u.getEmail(), event.name(), event.toString());
            } catch (MessagingException e) {
                //TODO deal with MessegingExeption
            }
        }).start();
    }

}
