package com.windhound.db;

import com.windhound.server.race.Boat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;

public class Database {

    private static String hostname, sid, user, password, port;


    static {
        Properties prop = new Properties();
        InputStream input = getResourceAsStream("config.properties");

        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        hostname = prop.getProperty("hostname");
        port = prop.getProperty("port");
        sid = prop.getProperty("sid");
        user = prop.getProperty("user");
        password = prop.getProperty("password");
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


