package com.windhound.server.database;

import com.windhound.server.race.Race;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;

public class DBRace {

    public static Race loadRaceByID(Connection connection, Long raceID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRaceByID + raceID);

        Long   id   = ((BigDecimal) DBManager.getValueAt(table, 0, "RACE_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

        HashSet<Long> boats = loadBoatsByRaceID(connection, raceID);
        HashSet<Long> events = loadEventsByRaceID(connection, raceID);
        HashSet<Long> admins = DBEvent.loadAdminsByEventTypeAndID(connection, "Race", raceID);

        Race race = Race.createRace(
                id,
                name,
                admins,
                boats,
                events
        );

        return race;

    }

    private static HashSet<Long> loadBoatsByRaceID(Connection connection, Long raceID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationBoatsByRace + raceID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    private static HashSet<Long> loadEventsByRaceID(Connection connection, Long raceID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationEventsByRace + raceID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }

    public static Long saveOrUpdateRace (Connection connection, Race race) {
        //TODO: implement saveOrUpdateRace


        return new Long(-1);
    }

    public static Long deleteRace (Connection connection, Long raceID) {
        //TODO: implement deleteRace


        return new Long(-1);
    }



    private static String queryRaceByID =
            "select * from RACE where RACE_ID=";
    private static String queryRelationBoatsByRace =
            "select * from REL_RACE_BOAT where RACE_ID=";
    private static String queryRelationEventsByRace =
            "select * from REL_EVENT_RACE where RACE_ID=";
}
