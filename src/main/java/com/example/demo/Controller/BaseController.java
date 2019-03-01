package com.example.demo.Controller;

import com.example.demo.Model.DAO.UserDao;
import com.example.demo.Model.POJO.ErrorMsg;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.NotLoggedException;
import org.apache.log4j.Logger;
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

    static Logger log = Logger.getLogger(BaseController.class.getName());
    protected static final String serverEmailAddress = "technomarket.project@gmail.com";


    @ExceptionHandler({TechnoMarketException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg invalidCredentials(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    protected ErrorMsg handleMyErrors(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.I_AM_A_TEAPOT.value(), LocalDateTime.now());
    }

    @ExceptionHandler({MessagingException.class})
    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    protected ErrorMsg handleEmailError(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.REQUEST_TIMEOUT.value(), LocalDateTime.now());
    }


    protected void validateLogin(HttpSession session, Exception e) throws NotLoggedException{
        if(session.getAttribute("userLogged") == null ||
                session.getAttribute("userLogged").equals(false) ||
                session.isNew()){
            throw new NotLoggedException(e.getMessage());
        }
    }


    //
//    protected void validateLoginAdmin(HttpSession session, Exception e) throws NotAdminException, NotLoggedException {
//        if (session.getAttribute("loggedUser") == null) {
//            throw new NotLoggedException(e.getMessage());
//        } else {
//            User logged = (User) (session.getAttribute("loggedUser"));
//            if (!logged.isAdmin()) {
//                throw new NotAdminException(e.getMessage());
//            }
//        }
//    }


//

}
