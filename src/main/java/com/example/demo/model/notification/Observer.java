package com.example.demo.model.notification;

import com.example.demo.controller.BaseController;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.mail.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.enums.Notification;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import static com.example.demo.model.enums.Notification.*;

@RestController
public class Observer extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/notify/{note}", method = RequestMethod.POST)
    public void notify(@PathVariable Notification note, HttpSession session) throws TechnoMarketException {


        if (validateAdminLogin(session)) {

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
            for (User u : userRepository.findAll()){
                if (u.getSubscribed() == 1) {
                    sendMail(u, eText);
                }
            }
        }).start();
    }

    private void sendMail(User u, Enum event){
        new Thread(() -> {
            try {
                MailUtil.sendMail(serverEmailAddress, u.getEmail(), event.name(), event.toString());
            } catch (MessagingException e) {
                //TODO deal with Messaging Exception
            }
        }).start();
    }

}
