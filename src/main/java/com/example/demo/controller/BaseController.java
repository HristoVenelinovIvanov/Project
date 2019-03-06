package com.example.demo.controller;

import com.example.demo.model.pojo.ErrorMsg;
import com.example.demo.model.pojo.User;
import com.example.demo.utility.exceptions.ProductExceptions.NoProductsInBasketException;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.exceptions.UserExceptions.NotAdminException;
import com.example.demo.utility.exceptions.UserExceptions.NotLoggedException;
import com.example.demo.utility.exceptions.UserExceptions.UserNotFoundException;
import com.example.demo.utility.exceptions.UserExceptions.UsersNotAvailableException;
import com.example.demo.utility.validators.UserValidator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
public abstract class BaseController {

    @Autowired
    protected UserValidator userValidator;

    static Logger log = Logger.getLogger(BaseController.class.getName());
    protected static final String serverEmailAddress = "technomarket.project@gmail.com";

    @ExceptionHandler({TechnoMarketException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg invalidCredentials(Exception e) {
        log.log(Priority.WARN, e.getMessage(), e);
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorMsg handleMyErrors(Exception e) {
        log.log(Priority.WARN, e.getMessage(), e);
        return new ErrorMsg(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
    }

    @ExceptionHandler({MessagingException.class})
    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    protected ErrorMsg handleEmailError(Exception e) {
        log.log(Priority.WARN, e.getMessage(), e);
        return new ErrorMsg(e.getMessage(), HttpStatus.REQUEST_TIMEOUT.value(), LocalDateTime.now());
    }

    @ExceptionHandler({NoProductsInBasketException.class})
    @ResponseStatus(value = HttpStatus.OK)
    protected ErrorMsg handleNoProductsInBasket(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.OK.value(), LocalDateTime.now());
    }

    protected boolean validateLogin(HttpSession session) throws NotLoggedException{
        if(session.getAttribute("userLogged") == null ||
                session.getAttribute("userLogged").equals(false) ||
                session.isNew()){
            throw new NotLoggedException("You are not logged in!");
        }
        return true;
    }

    protected boolean validateAdminLogin(HttpSession session) throws TechnoMarketException {

        validateLogin(session);

        User user = (User) session.getAttribute("userLogged");

        if (user != null) {
            if (userValidator.validateLoginFields(user)) {
                if (user.getUserRoleId() == User.USER_ROLE_ADMINISTRATOR) {
                    return true;
                }
                throw new NotAdminException("You are not admin!");
            }
            throw new UsersNotAvailableException();
        }
        throw new UserNotFoundException();
    }

}
