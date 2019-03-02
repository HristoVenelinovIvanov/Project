package com.example.demo.Controller;

import com.example.demo.Model.DAO.UserFavoritesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserFavoritesController extends BaseController {

    @Autowired
    private UserFavoritesDao userFavoritesDao;

    @RequestMapping(value = "/products/{productId}/addToFavorites", method = RequestMethod.GET)
    public void addToFavorites(@PathVariable("productId") long productId, HttpSession session)  {
        //TODO Validate login and get userID from session
        long userId = (long) session.getAttribute("userId");

        System.out.println("USER ID IS: " + userId);
        System.out.println("PRODUCT ID IS: " + productId);
        userFavoritesDao.addToFavorites(userId, productId);


    }
}
