package com.windhound.server.database;

import com.windhound.server.race.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

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
            port = prop.getProperty("port");
            sid = prop.getProperty("sid");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
        } catch (IOException e)
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
                    "jdbc:oracle:thin:@" + hostname + ":" + port + ":" + sid, user, password);
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return connection;
    }

    static JTable executeLoadQuery(Connection connection, String queryString)
    {
        ResultSet rs = null;
        JTable table = null;

        try
        {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(queryString);

        } catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }

        try
        {
            table = new JTable(buildTableModel(rs));
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return table;
    }

    static Long executeSetQuery(Connection connection, String queryString)
    {
        Long state = new Long(1);

        try
        {
            Statement stmt = connection.createStatement();
            stmt.executeQuery(queryString);
        } catch (Exception e)
        {
            state = new Long(-1);
            System.out.println(e);
            System.exit(1);
        }

        return state;
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException
    {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++)
        {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next())
        {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
                row.add(rs.getObject(columnIndex));
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    static Object getValueAt(JTable table, int rowId, String columnName)
    {
        int columnId = -1;

        for (int i = 0; i < table.getColumnCount(); ++i)
            if (table.getColumnName(i).equals(columnName))
                columnId = i;

        return table.getValueAt(rowId, columnId);
    }

    //
    // Default DB functions
    //
    public static Long[] loadAllChampionships()
    {
        Connection connection = getNewConnection();
        return loadAllChampionships(connection);
    }

    public static Long[] loadAllEvents()
    {
        Connection connection = getNewConnection();
        return loadAllEvents(connection);
    }

    public static Long[] loadAllRaces()
    {
        Connection connection = getNewConnection();
        return loadAllRaces(connection);
    }

    public static StructureElement loadStructureElement(Class type, Long id)
    {
        Connection connection = getNewConnection();
        return loadStructureElement(connection, type, id);
    }

    public static Long saveOrUpdateStructureElement(Class type, StructureElement element)
    {
        Connection connection = getNewConnection();
        return  saveOrUpdateStructureElement(connection, type, element);
    }

    public static Long[] loadAllChampionships(Connection connection)
    {
        JTable table = executeLoadQuery(connection, queryAllChampionships);

        ArrayList<Long> championships = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships.toArray(new Long[championships.size()]);
    }

    public static Long[] loadAllEvents(Connection connection)
    {
        JTable table = executeLoadQuery(connection, queryAllEvents);

        ArrayList<Long> events = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events.toArray(new Long[events.size()]);
    }

    public static Long[] loadAllRaces(Connection connection)
    {
        JTable table = executeLoadQuery(connection, queryAllRaces);

        ArrayList<Long> races = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races.toArray(new Long[races.size()]);
    }

    public static StructureElement loadStructureElement(Connection connection, Class type, Long id)
    {
        if (type == Championship.class)
            return DBChampionship.loadChampionshipByID(connection, id);
        if (type == Event.class)
            return DBEvent.loadEventByID(connection, id);
        if (type == Race.class)
            return DBRace.loadRaceByID(connection, id);
        if (type == Boat.class)
            return DBBoat.loadBoatByID(connection, id);
        //if (type == Competitor.class)
        //    return DBCompetitor.loadCompetitorByID(connection, id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager different from Competitor");
    }

    public static Long saveOrUpdateStructureElement(Connection connection, Class type, StructureElement element)
    {
        if (type == Championship.class)
            return DBChampionship.saveOrUpdateChampionship(connection, (Championship)element);
        if (type == Event.class)
            return DBEvent.saveOrUpdateEvent(connection, (Event)element);
        if (type == Race.class)
            return DBRace.saveOrUpdateRace(connection, (Race)element);
        if (type == Boat.class)
            return DBBoat.saveOrUpdateBoat(connection, (Boat)element);
        //if (type == Competitor.class)
        //    return DBCompetitor.loadCompetitorByID(connection, id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager different from Competitor");
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
}