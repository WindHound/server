package com.windhound.server.database;

import com.windhound.server.race.Boat;
import com.windhound.server.race.Competitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;

public class DBManager
{
    private static String hostname;
    private static String sid;
    private static String user;
    private static String password;
    private static String port;

    static {
        Properties prop = new Properties();
        try {
            InputStream inputStream = DBManager.class.getClassLoader().getResourceAsStream("config.properties");

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

    public static Connection getConnection()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@"+ hostname + ":" + port +":"+ sid ,user ,password);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return connection;
    }

    private static JTable query(Connection connection, String query) {

        ResultSet rs = null;
        JTable table = null;

        try{
            Statement stmt = connection.createStatement();
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
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
                row.add(rs.getObject(columnIndex));
            data.add(row);
        }
        
        return new DefaultTableModel(data, columnNames);
    }

    private static Object getValueAt(JTable table, int rowId, String columnName)
    {
        int columnId = -1;

        for (int i = 0; i < table.getColumnCount(); ++i)
            if (table.getColumnName(i).equals(columnName))
                columnId = i;

        return table.getValueAt(rowId, columnId);
    }

    public static Boat getBoatByID(Connection connection, Long boatID) {
        JTable table = query(connection, "select * from boat where boat_id=" + boatID);

        Long   id   = new Long(((BigDecimal)getValueAt(table, 0, "BOAT_ID")).longValue());
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> competitors = getCompetitorsByBoatID(connection, boatID);

        Boat boat = Boat.createBoat(
                id,
                name,
                new HashSet<>(),
                competitors,
                new HashSet<>()
        );

        return boat;
    }

    public static Competitor getUserByID(Connection connection, Long competitorID) {
        JTable table = query(connection, "select * from user where user_id=" + competitorID);

        Long   id   = new Long(((BigDecimal)getValueAt(table, 0, "USER_ID")).longValue());
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> boats = getBoatsByCompetitorID(connection, competitorID);

        Competitor competitor = Competitor.createCompetitor(
                id,
                name,
                boats
        );

        return competitor;
    }

    public static HashSet<Long> getCompetitorsByBoatID(Connection connection, Long boatID)
    {
        JTable table = query(connection, "select * from REL_BOAT_USER where boat_id=" + boatID);

        HashSet<Long> competitors = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = new Long(((BigDecimal)getValueAt(table, i, "USER_ID")).longValue());
            competitors.add(id);
        }

        return competitors;
    }

    public static HashSet<Long> getBoatsByCompetitorID(Connection connection, Long competitorID)
    {
        JTable table = query(connection, "select * from REL_BOAT_USER where user_id=" + competitorID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = new Long(((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue());
            boats.add(id);
        }

        return boats;
    }
}