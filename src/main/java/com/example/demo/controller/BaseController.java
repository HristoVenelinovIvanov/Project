package com.example.demo.controller;

import com.example.demo.model.pojo.ErrorMsg;
import com.example.demo.utility.exceptions.ProductExceptions.NoProductsInBasketException;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.exceptions.UserExceptions.NotLoggedException;
import com.example.demo.utility.validators.UserValidator;
import com.github.lambdaexpression.annotation.EnableRequestBodyParam;
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
@EnableRequestBodyParam
public abstract class BaseController {

    @Autowired
    protected UserValidator userValidator;

    static Logger log = Logger.getLogger(BaseController.class.getName());
    static final String serverEmailAddress = "technomarket.project@gmail.com";

    @ExceptionHandler({TechnoMarketException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg invalidCredentials(Exception e) {
        log.log(Priority.WARN, e.getMessage(), e);
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    protected ErrorMsg handleMyErrors(Exception e) {
        log.log(Priority.WARN, e.getMessage(), e);
        return new ErrorMsg(e.getMessage(), HttpStatus.I_AM_A_TEAPOT.value(), LocalDateTime.now());
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

}
