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

    private static JTable executeLoadQuery(Connection connection, String queryString) {
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

    private static Long executeSetQuery(Connection connection, String queryString) {
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

    //
    // Get StructureElement objects
    //
    public static HashSet<Long> loadAdminsByEventTypeAndID(Connection connection, String stageType, Long stageID) {
        JTable table = executeLoadQuery(connection, queryAdminsByStageType + "'" + stageType + "'"
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
        JTable table = executeLoadQuery(connection, queryBoatByID + boatID);

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

    public static Race loadRaceByID(Connection connection, Long raceID) {
        JTable table = executeLoadQuery(connection, queryRaceByID + raceID);

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
        JTable table = executeLoadQuery(connection, queryEventByID + eventID);

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
        JTable table = executeLoadQuery(connection, queryChampionshipByID + championshipID);

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

        championship.setStartDate((oracle.sql.TIMESTAMP) getValueAt(table, 0, "START_TIME"));
        championship.setEndDate((oracle.sql.TIMESTAMP) getValueAt(table, 0, "END_TIME"));

        return championship;
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

    public static HashSet<Long> loadCompetitorsByBoatID(Connection connection, Long boatID) {
        JTable table = executeLoadQuery(connection, queryRelationCompetitorsByBoat + boatID);

        HashSet<Long> competitors = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "USER_ID")).longValue();
            competitors.add(id);
        }

        return competitors;
    }

    private static HashSet<Long> loadBoatsByRaceID(Connection connection, Long raceID) {
        JTable table = executeLoadQuery(connection, queryRelationBoatsByRace + raceID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    private static HashSet<Long> loadEventsByRaceID(Connection connection, Long raceID) {
        JTable table = executeLoadQuery(connection, queryRelationEventsByRace + raceID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }

    private static HashSet<Long> loadChampionshipsByEventID(Connection connection, Long eventID) {
        JTable table = executeLoadQuery(connection, queryRelationChampionshipsByEvent + eventID);

        HashSet<Long> championships = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships;
    }

    private static HashSet<Long> loadRacesByEventID(Connection connection, Long eventID) {
        JTable table = executeLoadQuery(connection, queryRelationRacesByEvent + eventID);

        HashSet<Long> races = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races;
    }

    private static HashSet<Long> loadEventsByChampionshipID(Connection connection, Long championshipID) {
        JTable table = executeLoadQuery(connection, queryRelationEventsByChampionship + championshipID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }


    //
    //----------< SAVE|UPDATE >----------
    //

    private static void listIterateAndSQLExecute(
        Connection connection, List<Long> list, String iterateField, String query) {

        Map<String, String> map = new HashMap<>();

        for (Long listItem : list) {
            map.put(iterateField, listItem.toString());
            StrSubstitutor sub = new StrSubstitutor(map);
            String finalQuery = sub.replace(query);

            executeSetQuery(connection, finalQuery);
        }
    }

    public static Long saveOrUpdateChampionship (Connection connection, Championship championship) {
        Long championshipID = null;

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> adminMap = new HashMap<>();
        Map<String, String> eventMap = new HashMap<>();

        valuesMap.put("name"      , championship.getName());
        valuesMap.put("start_date", championship.getStartDate().toString());
        valuesMap.put("end_date"  , championship.getEndDate().toString());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (championship.getID() != null) {
            valuesMap.put("id", championship.getID().toString());
            String finalQuery = sub.replace(queryUpdateChampionship);
            executeSetQuery(connection, finalQuery);

            championshipID = championship.getID();

            //Delete admins
            adminMap.put("stage_id", championshipID.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryDeleteChampionshipAdmins);

            executeSetQuery(connection, finalAdminQuery);


            //Delete events
            eventMap.put("championship_id", championshipID.toString());
            StrSubstitutor eventSub = new StrSubstitutor(eventMap);
            String finalEventQuery = eventSub.replace(queryDeleteChampionshipEventsRelation);

            executeSetQuery(connection, finalEventQuery);

        }
        //INSERT
        else {
            String finalQuery = sub.replace(queryInsertChampionship);
            executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestChampionshipByName);
            JTable table   = executeLoadQuery(connection, idQuery);
            championshipID = Long.valueOf(table.getValueAt(0,0).toString());

        }

        //Save admins
        List<Long> admins = championship.getAdmins();
        adminMap.put("stage_id", championshipID.toString());

        for (Long admin : admins) {
            adminMap.put("user_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertChampionshipAdmins);

            executeSetQuery(connection, finalAdminQuery);
        }

        //Save events
        List<Long> events = championship.getSubordinates();
        eventMap.put("championship_id", championshipID.toString());

        for (Long event : events) {
            eventMap.put("event_id", event.toString());
            StrSubstitutor eventSub = new StrSubstitutor(eventMap);
            String finalEventQuery = eventSub.replace(queryInsertChampionshipEventsRelation);

            executeSetQuery(connection, finalEventQuery);
        }



        return championshipID;
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
    private static String queryUpdateChampionship =
            "UPDATE CHAMPIONSHIP SET                                               " +
            "NAME='${name}',                                                       " +
            "START_TIME=TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF'), " +
            "END_TIME=TO_TIMESTAMP('${end_date}'  , 'YYYY-MM-DD HH24:MI:SS.FF')    " +
            "WHERE CHAMPIONSHIP_ID=${id}                                           ";

    private static String queryInsertChampionshipAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (" +
            "'${user_id}'                                              ," +
            "'Championship'                                            ," +
            "'${stage_id}'                                             )";
    private static String queryDeleteChampionshipAdmins =
            "DELETE FROM ADMINS WHERE STAGE_ID=${stage_id} AND STAGE_TYPE='Championship'";
    private static String queryDeleteChampionshipEventsRelation =
            "DELETE FROM REL_CHAMPIONSHIP_EVENT WHERE CHAMPIONSHIP_ID=${championship_id}";
    private static String queryLatestChampionshipByName =
            "SELECT (CHAMPIONSHIP_ID) FROM CHAMPIONSHIP WHERE NAME='${name}' ORDER BY CHAMPIONSHIP_ID DESC";
    private static String queryInsertChampionshipEventsRelation =
            "INSERT INTO REL_CHAMPIONSHIP_EVENT (CHAMPIONSHIP_ID, EVENT_ID) VALUES (" +
            "'${championship_id}'                                                  ," +
            "'${event_id}'                                                         )";
}