package com.windhound.server.database;

import com.windhound.server.race.Event;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;

public class DBEvent {

    public static HashSet<Long> loadAdminsByEventTypeAndID(Connection connection, String stageType, Long stageID) {
        JTable table = DBManager.executeLoadQuery(connection, queryAdminsByStageType + "'" + stageType + "'"
                + " AND STAGE_ID=" + stageID);


        HashSet<Long> admins = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "USER_ID")).longValue();
            admins.add(id);
        }

        return admins;
    }

    private static HashSet<Long> loadChampionshipsByEventID(Connection connection, Long eventID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationChampionshipsByEvent + eventID);

        HashSet<Long> championships = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships;
    }

    private static HashSet<Long> loadRacesByEventID(Connection connection, Long eventID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationRacesByEvent + eventID);

        HashSet<Long> races = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races;
    }

    public static Event loadEventByID(Connection connection, Long eventID) {
        JTable table = DBManager.executeLoadQuery(connection, queryEventByID + eventID);

        Long   id   = ((BigDecimal) DBManager.getValueAt(table, 0, "EVENT_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

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



    private static String queryEventByID =
            "select * from EVENT where EVENT_ID=";
    private static String queryAdminsByStageType =
            "select * from ADMINS where STAGE_TYPE=";
    private static String queryRelationRacesByEvent =
            "select * from REL_EVENT_RACE where EVENT_ID=";
    private static String queryRelationChampionshipsByEvent =
            "select * from REL_CHAMPIONSHIP_EVENT where EVENT_ID=";
}
