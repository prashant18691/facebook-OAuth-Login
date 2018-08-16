package com.devglan.security;

import com.devglan.model.User;
import com.devglan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    @Autowired
    private UserService userService;

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        user.setUsername(connection.getDisplayName());
        Facebook facebook = (Facebook) connection.getApi();
        String [] fields = { "id", "email",  "first_name", "last_name" };
        org.springframework.social.facebook.api.User userProfile = facebook.fetchObject("me", org.springframework.social
                .facebook.api.User.class, fields);
        user.setPassword(BCrypt.hashpw(userProfile.getFirstName()+userProfile.getLastName(), BCrypt.gensalt()));
        userService.save(user);
        return user.getUsername();
    }
}