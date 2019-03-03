package com.example.demo.Model.Utility.Validators;

import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.NotAdminException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserNotFoundExeption;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UsersNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminValidator {

    @Autowired
    private AdminValidator adminValidator;

    @Autowired
    private UserValidator userValidator;

    public boolean isAdmin(User user) throws TechnoMarketException {
        if (user == null) {
            if (userValidator.validateEmptyFields(user)) {
                if (user.getUserRoleId() == 1) {
                    return true;
                }
                throw new NotAdminException("You are not admin!");
            }
            throw new UsersNotAvailableException();
        }
        throw new UserNotFoundExeption();
    }
}
