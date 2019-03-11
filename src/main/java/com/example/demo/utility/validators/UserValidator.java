package com.example.demo.utility.validators;

import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.exceptions.UserExceptions.UserNotFoundException;
import com.example.demo.utility.exceptions.ValidationExceptions.EmailNotValidException;
import com.example.demo.utility.exceptions.ValidationExceptions.InvalidCredentinalsException;
import com.example.demo.utility.exceptions.ValidationExceptions.PasswordTooShortException;
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
        if (user.getFirstName().isEmpty() || user.getFirstName() == null || user.getFirstName().contains(" ")) {
            throw new InvalidCredentinalsException("First name field is empty or is containing intervals!");
        }
        if (user.getLastName().isEmpty() || user.getLastName() == null || user.getLastName().contains(" ")) {
            throw new InvalidCredentinalsException("Last name field is empty or is containing intervals!");
        }
        if (user.getPassword().isEmpty() || user.getPassword() == null ||
                user.getPassword().length() < 6 || user.getPassword().contains(" ")) {
            throw new PasswordTooShortException();
        }
        if (isValidEmailAddress(user.getEmail())) {
            return true;
        }
        throw new EmailNotValidException();
    }

    private boolean isValidEmailAddress(String email) throws EmailNotValidException{

        if (!(email == null || email.isEmpty() || email.contains(" "))) {
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
        throw new UserNotFoundException();
    }

}
