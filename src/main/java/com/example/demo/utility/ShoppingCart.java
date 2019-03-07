package com.example.demo.utility;

import com.example.demo.model.pojo.Product;
import com.example.demo.utility.exceptions.ProductExceptions.NoProductsInBasketException;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Component
@NoArgsConstructor
public class ShoppingCart {

    private List<Product> shoppingCart = new ArrayList<>();
    private double orderTotal;

    public int getItemCount() {
        return shoppingCart.size();
    }

    public Map<String, List<Product>> viewProducts() throws NoProductsInBasketException {

        if (!shoppingCart.isEmpty()) {
            calculateTotalPrice();
            HashMap<String, List<Product>> priceAndProducts = new HashMap<>();
            priceAndProducts.put(calculateTotalPrice(), shoppingCart);

            return priceAndProducts;
        }
        throw new NoProductsInBasketException();
    }


    public void removeProductFromCart(int index, HttpServletResponse response) throws IOException, ProductDoesNotExistException {

        if (index > shoppingCart.size()) {
            throw new ProductDoesNotExistException("You are trying to access an index that is out of bounds of the shopping cart!");
        }

        if (index > 0) {
            if (shoppingCart.size() == 1) {
                shoppingCart.remove(0);
                response.getWriter().append("Product successfully removed from the basket");
            }
            else {
                if (shoppingCart.remove(index) != null) {
                    response.getWriter().append("Product successfully removed from the basket");
                } else {
                    response.getWriter().append("There is no such product in the basket!");
                }
            }
        }
        else {
            response.getWriter().append("Invalid index given!");
        }
    }

    public String addProductToCart(Product product) {

        return shoppingCart.add(product) ? "Product " +
                                            product.getProductName() + " with ID " +
                                            product.getProductId() + " has been added to the basket" :
                                            "Sorry, item could not be added to the basket";
    }

    public String calculateTotalPrice() {
        this.orderTotal = 0.00;

        for (Product p : shoppingCart) {
            this.orderTotal += p.getPrice();
        }
        return "You have " + shoppingCart.size() + " in the basket, the total sum is: " + orderTotal;
    }


}
