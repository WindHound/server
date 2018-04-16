package com.windhound.server.controller;

import com.windhound.server.database.DBManager;
import com.windhound.server.security.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class AccountController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //TODO: prevent duplicate accounts

    @CrossOrigin
    @PostMapping("/user/add/")
    public void addUser(@RequestBody UserDTO dto)

    {
        Connection con = DBManager.getNewConnection();
        String insertUserString = "INSERT INTO USERS (NAME, USERNAME, PASSWORD, EMAIL, TELNO, ENABLED) " +
                "VALUES (?, ?, ?, ?, ?, 1)";

        try {
            PreparedStatement insertUser = con.prepareStatement(insertUserString);

            insertUser.setString(1, dto.getName());
            insertUser.setString(2, dto.getUsername());
            insertUser.setString(3, passwordEncoder.encode(dto.getPassword()));
            insertUser.setString(4, dto.getEmail());
            insertUser.setString(5, dto.getTelNo());

            insertUser.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    @CrossOrigin
//    @PostMapping("/user/add/")
//    public void addUser(@RequestParam(value = "name") String name,
//                        @RequestParam(value = "username") String username,
//                        @RequestParam(value = "password") String password,
//                        @RequestParam(value = "email") String email,
//                        @RequestParam(value = "telNo") String telNo)
//
//    {
//        Connection con = DBManager.getNewConnection();
//        String insertUserString = "INSERT INTO USERS (NAME, USERNAME, PASSWORD, EMAIL, TELNO, ENABLED) " +
//                "VALUES (?, ?, ?, ?, ?, 1)";
//
//        try {
//            PreparedStatement insertUser = con.prepareStatement(insertUserString);
//
//            insertUser.setString(1, name);
//            insertUser.setString(2, username);
//            insertUser.setString(3, passwordEncoder.encode(password));
//            insertUser.setString(4, email);
//            insertUser.setString(5, telNo);
//
//            insertUser.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    //TODO: implement getAllUsers
    @RequestMapping("/user/get/{id}")
    public Long[] getAllUsers() {

        Long[] list = {new Long(0)};
        return list;
    }

    //TODO: implement getUser
    @RequestMapping
    public Long[] getUser() {

        Long[] list = {new Long(0)};
        return list;
    }


}
