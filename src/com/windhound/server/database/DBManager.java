package com.windhound.server.database;

import com.windhound.server.race.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import static com.windhound.server.database.DBChampionship.loadChampionshipByID;

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


    public static Connection getNewConnection() {
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

    static JTable executeLoadQuery(Connection connection, String queryString) {
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

    static Long executeSetQuery(Connection connection, String queryString) {
        Long state = new Long(1);

        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery(queryString);
        }
        catch(Exception e) {
            state = new Long(-1);
            System.out.println(e);
            System.exit(1);
        }

        return state;
    }
    
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
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

    static Object getValueAt(JTable table, int rowId, String columnName) {
        int columnId = -1;

        for (int i = 0; i < table.getColumnCount(); ++i)
            if (table.getColumnName(i).equals(columnName))
                columnId = i;

        return table.getValueAt(rowId, columnId);
    }


    //
    //----------< LOAD >----------
    //

    //
    // Get all stage IDs
    //
    public static Long[] loadAllChampionships(Connection connection) {
        JTable table = executeLoadQuery(connection, queryAllChampionships);

        ArrayList<Long> championships = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships.toArray(new Long[championships.size()]);
    }

    public static Long[] loadAllEvents(Connection connection) {
        JTable table = executeLoadQuery(connection, queryAllEvents);

        ArrayList<Long> events = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events.toArray(new Long[events.size()]);
    }

    public static Long[] loadAllRaces(Connection connection) {
        JTable table = executeLoadQuery(connection, queryAllRaces);

        ArrayList<Long> races = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races.toArray(new Long[races.size()]);
    }

    public static StructureElement loadStructureElement(Class type, Long id) {
        Connection connection = getNewConnection();
        return loadStructureElement(connection, type, id);
    }

    public static StructureElement loadStructureElement(Connection connection, Class type, Long id) {
        if (type == Championship.class)
            return loadChampionshipByID(connection, id);
        if (type == Event.class)
            return DBEvent.loadEventByID(connection, id);
        if (type == Race.class)
            return DBRace.loadRaceByID(connection, id);
        if (type == Boat.class)
            return DBBoat.loadBoatByID(connection, id);
        if (type == Competitor.class)
            return loadCompetitorByID(connection, id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager");
    }

    public static Competitor loadCompetitorByID(Connection connection, Long competitorID) {
        JTable table = executeLoadQuery(connection, queryCompetitorByID + competitorID);

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
    public static HashSet<Long> loadBoatsByCompetitorID(Connection connection, Long competitorID) {
        JTable table = executeLoadQuery(connection, queryRelationBoatsByCompetitor + competitorID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }


    //
    // Query strings
    //
    private static String queryAllChampionships =
            "select (CHAMPIONSHIP_ID) from CHAMPIONSHIP";
    private static String queryAllEvents =
            "select (EVENT_ID) from EVENT";
    private static String queryAllRaces =
            "select (RACE_ID) from RACE";

    private static String queryCompetitorByID =
            "select * from USERS where USER_ID=";
    private static String queryRelationBoatsByCompetitor =
            "select * from REL_BOAT_USER where USER_ID=";


}