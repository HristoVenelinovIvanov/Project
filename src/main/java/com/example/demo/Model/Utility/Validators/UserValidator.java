package com.example.demo.Model.Utility.Validators;

import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserNotFoundExeption;
import com.example.demo.Model.Utility.Exceptions.ValidationExceptions.EmailNotValidException;
import com.example.demo.Model.Utility.Exceptions.ValidationExceptions.InvalidCredentinalsException;
import com.example.demo.Model.Utility.Exceptions.ValidationExceptions.PasswordTooShortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    public boolean validateEmptyFields(User user) throws TechnoMarketException {
        if (user == null) {
            throw new InvalidCredentinalsException("Invalid credentials");
        }
        if (user.getFirstName().isEmpty() || user.getFirstName() == null) {
            throw new InvalidCredentinalsException("First name field must not be empty!");
        }
        if (user.getLastName().isEmpty() || user.getLastName() == null) {
            throw new InvalidCredentinalsException("Last name field must not be empty!");
        }
        if (user.getPassword().isEmpty() || user.getPassword() == null ||
                user.getPassword().length() < 5 || user.getPassword().contains(" ")) {
            throw new PasswordTooShortException();
        }
        if (isValidEmailAddress(user.getEmail())) {
            return true;
        }
        throw new EmailNotValidException();
    }

    private boolean isValidEmailAddress(String email) throws EmailNotValidException{

        if (!(email == null && email.isEmpty())) {
                return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        }
        throw new EmailNotValidException();
    }

    public boolean validateLoginFields(User user) throws TechnoMarketException {

        user.setPassword(user.getPassword().trim());

        if (isValidEmailAddress(user.getEmail())) {

            if (!(user.getPassword().isEmpty() || user.getPassword() == null || userRepository.findByEmail(user.getEmail()) == null)) {
                return true;
            }
        }
        throw new UserNotFoundExeption();
    }

    public boolean validateLoginFields(String email, String password) throws TechnoMarketException {

        password = password.trim();
        if (isValidEmailAddress(email)) {
            if (!(password.isEmpty() || password == null)) {
                return true;
            }
        }
        throw new UserNotFoundExeption();
    }
}
