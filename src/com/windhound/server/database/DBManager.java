package com.windhound.server.database;

import com.windhound.server.race.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

public class DBManager
{
    private static String hostname;
    private static String sid;
    private static String user;
    private static String password;
    private static String port;

    static
    {
        Properties prop = new Properties();
        try
        {
            InputStream inputStream = DBManager.class.getClassLoader().
                    getResourceAsStream("config.properties");

            prop.load(inputStream);

            hostname = prop.getProperty("hostname");
            port     = prop.getProperty("port");
            sid      = prop.getProperty("sid");
            user     = prop.getProperty("user");
            password = prop.getProperty("password");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Connection getNewConnection()
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

    private static JTable executeQuery(Connection connection, String queryString)
    {
        ResultSet rs = null;
        JTable table = null;

        try
        {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(queryString);

        }
        catch(Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }

        try
        {
            table = new JTable(buildTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
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
    //
    // Get StructureElement objects
    //
    public static StructureElement loadStructureElement(Class type, Long id)
    {
        Connection connection = getNewConnection();
        return loadStructureElement(connection, type, id);
    }

    public static StructureElement loadStructureElement(Connection connection, Class type, Long id)
    {
        /*if (type == Championship.class)
            return loadChampionshipByID(connection, id);
        if (type == Event.class)
            return loadEventByID(connection, id);
        if (type == Race.class)
            return loadRaceByID(connection, id);*/
        if (type == Boat.class)
            return loadBoatByID(connection, id);
        if (type == Competitor.class)
            return loadCompetitorByID(connection, id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager");
    }

    public static Boat loadBoatByID(Connection connection, Long boatID) {
        JTable table = executeQuery(connection, queryBoatByID + boatID);

        Long   id   = ((BigDecimal)getValueAt(table, 0, "BOAT_ID")).longValue();
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> competitors = loadCompetitorsByBoatID(connection, boatID);

        Boat boat = Boat.createBoat(
                id,
                name,
                new HashSet<>(),
                competitors,
                new HashSet<>()
        );

        return boat;
    }

    public static Competitor loadCompetitorByID(Connection connection, Long competitorID) {
        JTable table = executeQuery(connection, queryCompetitorByID + competitorID);

        Long   id   = ((BigDecimal)getValueAt(table, 0, "USER_ID")).longValue();
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> boats = loadBoatsByCompetitorID(connection, competitorID);

        Competitor competitor = Competitor.createCompetitor(
                id,
                name,
                boats
        );

        return competitor;
    }
    //
    // Get StructureElement relations
    //
    public static HashSet<Long> loadBoatsByCompetitorID(Connection connection, Long competitorID)
    {
        JTable table = executeQuery(connection, queryRelationBoatsByCompetitor + competitorID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    public static HashSet<Long> loadCompetitorsByBoatID(Connection connection, Long boatID)
    {
        JTable table = executeQuery(connection, queryRelationCompetitorsByBoat + boatID);

        HashSet<Long> competitors = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "USER_ID")).longValue();
            competitors.add(id);
        }

        return competitors;
    }
    //
    // Query strings
    //
    private static String queryBoatByID =
            "select * from BOAT where BOAT_ID=";
    private static String queryCompetitorByID =
            "select * from USERS where user_id=";

    private static String queryRelationBoatsByCompetitor =
            "select * from REL_BOAT_USER where user_id=";
    private static String queryRelationCompetitorsByBoat =
            "select * from REL_BOAT_USER where boat_id=";
}