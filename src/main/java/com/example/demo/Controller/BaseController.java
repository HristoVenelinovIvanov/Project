package com.example.demo.Controller;

import com.example.demo.Model.DAO.UserDao;
import com.example.demo.Model.POJO.ErrorMsg;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Utility.Exceptions.CategoryNotFoundException;
import com.example.demo.Model.Utility.Exceptions.NotAdminException;
import com.example.demo.Model.Utility.Exceptions.NotLoggedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
public abstract class BaseController {

    static Logger log = Logger.getLogger(BaseController.class.getName());
    @Autowired
    protected UserDao userDao;

    @ExceptionHandler({NotLoggedException.class, NotAdminException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg isLogged(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ErrorMsg isLoggedAdmin(Exception e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    }

    @ExceptionHandler({CategoryNotFoundException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ErrorMsg isCategoryValid() {
        return new ErrorMsg("The category entered doesn't exist", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    protected void validateLogin(HttpSession session, Exception e, User user) throws NotLoggedException{
        if(session.getAttribute("isLogged") == null){
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
