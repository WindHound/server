/*
package com.windhound.server.database;


import com.windhound.server.race.Competitor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;

public class DBCompetitor
{
    public static Competitor loadCompetitorByID(Connection connection, Long competitorID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryCompetitorByID + competitorID);

        Long id = ((BigDecimal) DBManager.getValueAt(table, 0, "USER_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

        HashSet<Long> boats = loadBoatsByCompetitorID(connection, competitorID);

        Competitor competitor = Competitor.createCompetitor(
                id,
                name,
                boats
        );

        return competitor;
    }

    public static HashSet<Long> loadBoatsByCompetitorID(Connection connection, Long competitorID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationBoatsByCompetitor + competitorID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    //
    // Query strings
    //
    private static String queryCompetitorByID =
            "select * from USERS where USER_ID=";
    private static String queryRelationBoatsByCompetitor =
            "select * from REL_BOAT_USER where USER_ID=";
}
*/