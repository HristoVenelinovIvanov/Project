package com.example.demo.utility.validators;

import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.exceptions.UserExceptions.NotAdminException;
import com.example.demo.utility.exceptions.UserExceptions.UserNotFoundExeption;
import com.example.demo.utility.exceptions.UserExceptions.UsersNotAvailableException;
import com.example.demo.utility.exceptions.ValidationExceptions.EmailNotValidException;
import com.example.demo.utility.exceptions.ValidationExceptions.InvalidCredentinalsException;
import com.example.demo.utility.exceptions.ValidationExceptions.PasswordTooShortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    private static final int USER_ROLE_ADMINISTRATOR = 1;

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

    public boolean isAdmin(User user) throws TechnoMarketException {

        if (user == null) {
            if (validateEmptyFields(user)) {
                if (user.getUserRoleId() == USER_ROLE_ADMINISTRATOR) {
                    return true;
                }
                throw new NotAdminException("You are not admin!");
            }
            throw new UsersNotAvailableException();
        }
        throw new UserNotFoundExeption();
    }
}
