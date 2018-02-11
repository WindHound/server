package com.windhound.db;

import com.windhound.server.race.Boat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;

public class Database {
    private static String hostname;
    private static String sid;
    private static String user;
    private static String password;
    private static String port;

    static {
        Properties prop = new Properties();
        try {
            InputStream inputStream = Database.class.getClassLoader().getResourceAsStream("config.properties");

            prop.load(inputStream);

            hostname = prop.getProperty("hostname");
            port = prop.getProperty("port");
            sid = prop.getProperty("sid");
            user = prop.getProperty("user");
            password = prop.getProperty("password");

            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JTable query(String query) {

        ResultSet rs = null;
        Connection con;
        JTable table = null;

        try{
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@"+ hostname + ":" + port +":"+ sid ,user ,password);

            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);

        }catch(Exception e){ System.out.println(e);}


        try {
            table = new JTable(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return table;
    }
    
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(rs.getObject(columnIndex));
            }
        }
        
        return new DefaultTableModel(data, columnNames);
    } 

    public static Boat getBoat(int boatID) {
        JTable table = query("select * from boat where boat_id=" + boatID);
        
        TableModel model = table.getModel();

        return null;
    }
}