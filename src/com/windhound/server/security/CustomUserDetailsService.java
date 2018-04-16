package com.windhound.server.security;

import com.windhound.server.database.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomUserDetailsService implements UserDetailsService{

    public UserDetails loadUserByUsername(String username) {
        com.windhound.server.security.User user = findUserByUsername(username);
        User.UserBuilder builder;

        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
            builder.roles(user.getRoles());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }


    private com.windhound.server.security.User findUserByUsername(String user) {
        Connection con = DBManager.getNewConnection();
//        String getUserString = "SELECT USERNAME, PASSWORD FROM USERS WHERE USERNAME = ?";
        String getUserString = "SELECT USERNAME, PASSWORD FROM USERS WHERE USERNAME = '" + user + "'";
        String finalQuery = null;


//        try {
//            PreparedStatement getUser = con.prepareStatement(getUserString);
//
//            getUser.setString(1, user);
//
//            finalQuery = getUser.toString();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        JTable table = DBManager.executeLoadQuery(con, finalQuery);

        JTable table = DBManager.executeLoadQuery(con, getUserString);

        String username = (String) DBManager.getValueAt(table, 0, "USERNAME");
        String password = (String) DBManager.getValueAt(table, 0, "PASSWORD");

        System.out.println(username + " : " + password);

        return new com.windhound.server.security.User(username, password, "USER");
    }
}
