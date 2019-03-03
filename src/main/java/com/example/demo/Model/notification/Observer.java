package com.example.demo.Model.notification;

import com.example.demo.Model.Enums.Notification;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Repository.ProductRepository;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Mail.MailUtil;
import com.example.demo.Model.Utility.Validators.AdminValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@RestController
public class Observer {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private Notification notification;
    @Autowired
    private AdminValidator adminValidator;

    static final String serverEmailAddress = "technomarket.project@gmail.com";

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void notify(@RequestBody Notification note, HttpSession session) throws TechnoMarketException {

        User u = (User) session.getAttribute("userLogged");
        if (adminValidator.isAdmin(u)) {
            switch (note) {
                case DISCOUNT:
                    sendEmails(notification.DISCOUNT);
                    break;
                case BLACK_FRIDAY:
                    sendEmails(notification.BLACK_FRIDAY);
                    break;
                case CRAZY_DAYS:
                    sendEmails(notification.CRAZY_DAYS);
                    break;
                case CYBER_MONDAY:
                    sendEmails(notification.CYBER_MONDAY);
                    break;
            }
        }
    }

    @Transactional
    public void sendEmails(Enum eText){
        new Thread(() -> {
            try {
                MailUtil.sendMail(serverEmailAddress, userRepository.getAllUsersByNotify(1).getEmail(),
                        eText.toString(), eText.toString() +
                                userRepository.getAllUsersByNotify(1).getUserId());
            } catch (MessagingException e) {
                //TODO Deal with email not sending AND make the method in transaction
            }
        }).start();
    }

}
