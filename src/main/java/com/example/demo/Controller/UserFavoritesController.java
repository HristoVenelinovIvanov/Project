package com.example.demo.Controller;

import com.example.demo.Model.DAO.ProductDao;
import com.example.demo.Model.DAO.UserFavoritesDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.Utility.Exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.AlreadyFavoritedException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UserFavoritesController extends BaseController {

    @Autowired
    private UserFavoritesDao userFavoritesDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserRepository userRepository;


    //Adding a product to favorites
    @RequestMapping(value = "/products/favorites/add/{productId}", method = RequestMethod.GET)
    public void addToFavorites(@PathVariable("productId") long productId, HttpSession session, HttpServletResponse response) throws Exception {

        long userId = (long) session.getAttribute("userId");

        if (validateLogin(session) ) {

            if (!userFavoritesDao.checkIfLiked(userId, productId)) {
                if (productDao.productExists(productId)) {
                    userFavoritesDao.addToFavorites(userId, productId);
                    response.getWriter().append("Product ID: " + productId + " has been added to favorites");
                }
                else {
                    throw new ProductDoesNotExistException("The product does not exist, sorry :(");
                }
            }
            else {
                throw new AlreadyFavoritedException();
            }
        }
        else {
            throw new NotLoggedException("Your session has expired, log in and try again");
        }

    }

    //Listing all favorites for the specific user that is logged in, could be optimized
    @RequestMapping(value = "/products/favorites/get", method = RequestMethod.GET)
    public List<Product> getFavorites(HttpSession session) throws Exception{

        if(validateLogin(session)) {
            User user = userRepository.findByUserId((long) session.getAttribute("userId"));
            if (user.getFavorites().size() == 0) {
                throw new ProductDoesNotExistException("Your favorites are empty");
            }
            return user.getFavorites();
        }
        throw new NotLoggedException("Your session has expired, log in and try again");
    }

    //Removing a product from favorites for the user that is logged
    @RequestMapping(value = "/products/favorites/remove/{productId}", method = RequestMethod.GET)
    public void removeFromFavorites(@PathVariable("productId") long productId, HttpSession session, HttpServletResponse response) throws Exception {

        if(validateLogin(session)) {

            User user = userRepository.findByUserId((long) session.getAttribute("userId"));

            if (user.getFavorites().size() == 0) {
                throw new ProductDoesNotExistException("Your favorites are empty");
            }
            else {
                if (productDao.productExists(productId)) {
                    userFavoritesDao.removeFromFavorites(user.getUserId(), productId);
                    response.getWriter().append("Product ID: " + productId + " has been removed from your favorites");
                }
                else {
                    throw new ProductDoesNotExistException("The product does not exist, sorry :(");
                }
            }

        }
        else {
            throw new NotLoggedException("Your session has expired, log in and try again");
        }
    }

}
