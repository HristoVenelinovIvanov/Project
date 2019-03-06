package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.dao.UserFavoritesDao;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.exceptions.UserExceptions.AlreadyFavoritedException;
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

        validateLogin(session);

        long userId = (long) session.getAttribute("userId");

            if (!userFavoritesDao.isFavorite(userId, productId)) {
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

    //Listing all favorites for the specific user that is logged in, could be optimized
    @RequestMapping(value = "/products/favorites/get", method = RequestMethod.GET)
    public List<Product> getFavorites(HttpSession session) throws Exception{

        validateLogin(session);
        User user = userRepository.findByUserId((long) session.getAttribute("userId"));

            if (user.getFavorites().isEmpty()) {
                throw new ProductDoesNotExistException("Your favorites are empty.");
            }
            return user.getFavorites();

    }

    //Removing a product from favorites
    @RequestMapping(value = "/products/favorites/remove/{productId}", method = RequestMethod.GET)
    public String removeFromFavorites(@PathVariable("productId") long productId, HttpSession session) throws Exception {

        validateLogin(session);
        User user = userRepository.findByUserId((long) session.getAttribute("userId"));

        if(!userFavoritesDao.isFavorite(user.getUserId(), productId)) {
            return "The product you are trying to remove is not in the favorite list!";
        }
        else {
            if (user.getFavorites().isEmpty()) {
                throw new ProductDoesNotExistException("Your favorites are empty.");
            }
            else {
                if (productDao.productExists(productId)) {
                    userFavoritesDao.removeFromFavorites(user.getUserId(), productId);
                    return "Product ID: " + productId + " has been removed from your favorites";
                }
                else {
                    throw new ProductDoesNotExistException("The product does not exist, sorry :(");
                }
            }
        }
    }

}
