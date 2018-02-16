package com.windhound.server.database;

import com.windhound.server.race.*;
import org.apache.commons.lang.text.StrSubstitutor;

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


    //
    //
    //
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

    private static JTable executeQuery(Connection connection, String queryString) {
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

    private static Object getValueAt(JTable table, int rowId, String columnName) {
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
        JTable table = executeQuery(connection, queryAllChampionships);

        ArrayList<Long> championships = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships.toArray(new Long[championships.size()]);
    }

    public static Long[] loadAllEvents(Connection connection) {
        JTable table = executeQuery(connection, queryAllEvents);

        ArrayList<Long> events = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events.toArray(new Long[events.size()]);
    }

    public static Long[] loadAllRaces(Connection connection) {
        JTable table = executeQuery(connection, queryAllRaces);

        ArrayList<Long> races = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races.toArray(new Long[races.size()]);
    }

    //
    // Get StructureElement objects
    //
    public static HashSet<Long> loadAdminsByEventTypeAndID(Connection connection, String stageType, Long stageID) {
        JTable table = executeQuery(connection, queryAdminsByStageType + "'" + stageType + "'"
                + " AND STAGE_ID=" + stageID);


        HashSet<Long> admins = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "USER_ID")).longValue();
            admins.add(id);
        }

        return admins;
    }

    public static StructureElement loadStructureElement(Class type, Long id) {
        Connection connection = getNewConnection();
        return loadStructureElement(connection, type, id);
    }

    public static StructureElement loadStructureElement(Connection connection, Class type, Long id) {
        if (type == Championship.class)
            return loadChampionshipByID(connection, id);
        if (type == Event.class)
            return loadEventByID(connection, id);
        if (type == Race.class)
            return loadRaceByID(connection, id);
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

    public static Race loadRaceByID(Connection connection, Long raceID) {
        JTable table = executeQuery(connection, queryRaceByID + raceID);

        Long   id   = ((BigDecimal)getValueAt(table, 0, "RACE_ID")).longValue();
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> boats = loadBoatsByRaceID(connection, raceID);
        HashSet<Long> events = loadEventsByRaceID(connection, raceID);
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Race", raceID);

        Race race = Race.createRace(
                id,
                name,
                admins,
                boats,
                events
        );

        return race;

    }

    public static Event loadEventByID(Connection connection, Long eventID) {
        JTable table = executeQuery(connection, queryEventByID + eventID);

        Long   id   = ((BigDecimal)getValueAt(table, 0, "EVENT_ID")).longValue();
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> races = loadRacesByEventID(connection, eventID);
        HashSet<Long> championships = loadChampionshipsByEventID(connection, eventID);
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Event", eventID);

        Event event = Event.createEvent(
                id,
                name,
                admins,
                races,
                championships
        );

        return event;
    }

    public static Championship loadChampionshipByID(Connection connection, Long championshipID) {
        JTable table = executeQuery(connection, queryChampionshipByID + championshipID);

        Long   id   = ((BigDecimal)getValueAt(table, 0, "CHAMPIONSHIP_ID")).longValue();
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> events = loadEventsByChampionshipID(connection, championshipID);
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Championship", championshipID);

        Championship championship = Championship.createChampionship(
                id,
                name,
                admins,
                events
        );

        return championship;
    }




    //
    // Get StructureElement relations
    //
    public static HashSet<Long> loadBoatsByCompetitorID(Connection connection, Long competitorID) {
        JTable table = executeQuery(connection, queryRelationBoatsByCompetitor + competitorID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    public static HashSet<Long> loadCompetitorsByBoatID(Connection connection, Long boatID) {
        JTable table = executeQuery(connection, queryRelationCompetitorsByBoat + boatID);

        HashSet<Long> competitors = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "USER_ID")).longValue();
            competitors.add(id);
        }

        return competitors;
    }

    private static HashSet<Long> loadBoatsByRaceID(Connection connection, Long raceID) {
        JTable table = executeQuery(connection, queryRelationBoatsByRace + raceID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    private static HashSet<Long> loadEventsByRaceID(Connection connection, Long raceID) {
        JTable table = executeQuery(connection, queryRelationEventsByRace + raceID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }

    private static HashSet<Long> loadChampionshipsByEventID(Connection connection, Long eventID) {
        JTable table = executeQuery(connection, queryRelationChampionshipsByEvent + eventID);

        HashSet<Long> championships = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships;
    }

    private static HashSet<Long> loadRacesByEventID(Connection connection, Long eventID) {
        JTable table = executeQuery(connection, queryRelationRacesByEvent + eventID);

        HashSet<Long> races = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races;
    }

    private static HashSet<Long> loadEventsByChampionshipID(Connection connection, Long championshipID) {
        JTable table = executeQuery(connection, queryRelationEventsByChampionship + championshipID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }


    //
    //----------< SAVE >----------
    //

    public static Long saveChampionship (Championship championship) {
        Map<String, String> valuesMap = new HashMap<>();

        //championship.getID() == null      <- Use for (update|add)
        //update rel_championship_event set CHAMPIONSHIP_ID=1 where EVENT_ID=1 and CHAMPIONSHIP_ID=2
        //^^^ example update query

        valuesMap.put("name"      , championship.getName());
        valuesMap.put("start_date", "1970-02-16 10:50:39.513000000");
        valuesMap.put("end_date"  , "1970-02-17 10:50:39.513000000");

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        String finalQuery = sub.replace(queryInsertChampionship);


        return new Long(-1);
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

    private static String queryBoatByID =
            "select * from BOAT where BOAT_ID=";
    private static String queryCompetitorByID =
            "select * from USERS where USER_ID=";
    private static String queryRaceByID =
            "select * from RACE where RACE_ID=";
    private static String queryEventByID =
            "select * from EVENT where EVENT_ID=";
    private static String queryChampionshipByID =
            "select * from CHAMPIONSHIP where CHAMPIONSHIP_ID=";
    private static String queryAdminsByStageType =
            "select * from ADMINS where STAGE_TYPE=";


    private static String queryRelationBoatsByCompetitor =
            "select * from REL_BOAT_USER where USER_ID=";
    private static String queryRelationCompetitorsByBoat =
            "select * from REL_BOAT_USER where USER_ID=";
    private static String queryRelationBoatsByRace =
            "select * from REL_RACE_BOAT where RACE_ID=";
    private static String queryRelationEventsByRace =
            "select * from REL_EVENT_RACE where RACE_ID=";
    private static String queryRelationRacesByEvent =
            "select * from REL_EVENT_RACE where EVENT_ID=";
    private static String queryRelationChampionshipsByEvent =
            "select * from REL_CHAMPIONSHIP_EVENT where EVENT_ID=";
    private static String queryRelationEventsByChampionship =
            "select * from REL_CHAMPIONSHIP_EVENT where CHAMPIONSHIP_ID=";

    private static String queryInsertChampionship =
            "INSERT INTO CHAMPIONSHIP (NAME, START_TIME, END_TIME) VALUES (" +
            "'${name}'                                                    ," +
            "TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF')    ," +
            "TO_TIMESTAMP('${end_date}'  , 'YYYY-MM-DD HH24:MI:SS.FF')    )";
}