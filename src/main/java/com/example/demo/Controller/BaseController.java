package com.example.demo.Controller;

import com.example.demo.Model.ErrorMsg;
import com.example.demo.Model.User;
import com.example.demo.Model.Utility.Exceptions.NotAdminException;
import com.example.demo.Model.Utility.Exceptions.NotLoggedException;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
public abstract class BaseController {

    static Logger log = Logger.getLogger(BaseController.class.getName());

    @ExceptionHandler({NotLoggedException.class, NotAdminException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg isLogged(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg isLoggedAdmin(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }
//
//    @ExceptionHandler({NotLoggedException.class, NotAdminException.class})
//    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
//    public ErrorMsg handleNotLogged(Exception e) {
//        ErrorMsg errorMsg = new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
//        return errorMsg;
//    }
//
//    @ExceptionHandler({NotLoggedException.class, NotAdminException.class})
//    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
//    public ErrorMsg handleNotLogged(Exception e) {
//        ErrorMsg errorMsg = new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
//        return errorMsg;
//    }

//    protected boolean validateLogin(HttpSession session) {
//        if (session.isNew() || session != null) {
//
//        }
//    }

}
