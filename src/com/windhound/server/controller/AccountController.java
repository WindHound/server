package com.windhound.server.controller;

import com.windhound.server.database.DBManager;
import com.windhound.server.security.LoginDTO;
import com.windhound.server.security.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class AccountController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //TODO: prevent duplicate accounts

    @CrossOrigin
    @PostMapping("/user/add")
    public void addUser(@RequestBody UserDTO dto)

    {
        Connection con = DBManager.getNewConnection();
        String insertUserString = "INSERT INTO USERS (NAME, USERNAME, PASSWORD, EMAIL, ENABLED) " +
                "VALUES (?, ?, ?, ?, 1)";

        try {
            PreparedStatement insertUser = con.prepareStatement(insertUserString);

            insertUser.setString(1, dto.getName());
            insertUser.setString(2, dto.getUsername());
            insertUser.setString(3, passwordEncoder.encode(dto.getPassword()));
            insertUser.setString(4, dto.getEmail());

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


    @CrossOrigin
    @PostMapping("/user/login")
    public Long checkLogin(@RequestBody LoginDTO dto) {
        Connection con = DBManager.getNewConnection();
        String checkLoginString = "SELECT (USER_ID) FROM USERS WHERE USERNAME=? AND PASSWORD =?";
        Long returnID = new Long(-1);

        try {
            PreparedStatement insertUser = con.prepareStatement(checkLoginString);

            insertUser.setString(1, dto.getUsername());
            insertUser.setString(2, passwordEncoder.encode(dto.getPassword()));


            ResultSet rs = insertUser.executeQuery();

            if(rs.next()) {
                returnID = rs.getLong("USER_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnID;
    }

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
