package com.windhound.server.security;

import com.windhound.server.database.DBManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.*;
import java.sql.Connection;

public class CustomUserDetailsService implements UserDetailsService{

    public UserDetails loadUserByUsername(String username) {
        UserDTO userDTO = findUserByUsername(username);
        User.UserBuilder builder;

        if (userDTO != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            builder.roles(userDTO.getRoles());
        } else {
            throw new UsernameNotFoundException("UserDTO not found.");
        }

        return builder.build();
    }


    private UserDTO findUserByUsername(String user) {
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

        return new UserDTO(username, password, "USER");
    }
}
