package com.example.demo.Model.Validators;

import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserNotFoundExeption;
import com.example.demo.Model.Utility.Exceptions.ValidationExceptions.EmailNotValidException;
import com.example.demo.Model.Utility.Exceptions.ValidationExceptions.InvalidCredentinalsException;
import com.example.demo.Model.Utility.Exceptions.ValidationExceptions.PasswordTooShortException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public boolean validateEmptyFields(User user) throws TechnoMarketException {

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
            if (!(user.getPassword().isEmpty() || user.getPassword() == null)) {
                return true;
            }
        }
        throw new UserNotFoundExeption();
    }
}
