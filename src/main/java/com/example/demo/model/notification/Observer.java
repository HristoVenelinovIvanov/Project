package com.example.demo.model.notification;

import com.example.demo.model.repository.ProductRepository;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.enums.Notification;


@RestController
public class Observer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserValidator userValidator;


    static final String serverEmailAddress = "technomarket.project@gmail.com";

//    @RequestMapping(value = "/notify", method = RequestMethod.POST)
//    public void notify(@RequestBody Notification note, HttpSession session) throws TechnoMarketException {
//
//        User u = (User) session.getAttribute("userLogged");
//        if (userValidator.isAdmin(u)) {
//            switch (note) {
//                case DISCOUNT:
//                    sendEmails(notification.DISCOUNT);
//                    break;
//                case BLACK_FRIDAY:
//                    sendEmails(notification.BLACK_FRIDAY);
//                    break;
//                case CRAZY_DAYS:
//                    sendEmails(notification.CRAZY_DAYS);
//                    break;
//                case CYBER_MONDAY:
//                    sendEmails(notification.CYBER_MONDAY);
//                    break;
//            }
//        }
//    }
//
//    @Transactional
//    public void sendEmails(Enum eText){
//        new Thread(() -> {
//            try {
//                MailUtil.sendMail(serverEmailAddress, userRepository.getAllUsersByNotify(1).getEmail(),
//                        eText.toString(), eText.toString() +
//                                userRepository.getAllUsersByNotify(1).getUserId());
//            } catch (MessagingException e) {
//                //TODO Deal with email not sending AND make the method in transaction
//            }
//        }).start();
//    }

}
