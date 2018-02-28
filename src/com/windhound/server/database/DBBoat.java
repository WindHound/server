package com.windhound.server.database;

import com.windhound.server.race.Boat;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;

public class DBBoat {

    public static Boat loadBoatByID(Connection connection, Long boatID) {
        JTable table = DBManager.executeLoadQuery(connection, queryBoatByID + boatID);

        Long   id   = ((BigDecimal) DBManager.getValueAt(table, 0, "BOAT_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

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

    public static HashSet<Long> loadCompetitorsByBoatID(Connection connection, Long boatID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationCompetitorsByBoat + boatID);

        HashSet<Long> competitors = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "USER_ID")).longValue();
            competitors.add(id);
        }

        return competitors;
    }

    public static Long saveOrUpdateBoat (Connection connection, Boat boat) {
        //TODO: implement saveOrUpdateBoat


        return new Long(-1);
    }

    public static Long deleteBoat (Connection connection, Long boatID) {
        //TODO: implement deleteBoat


        return new Long(-1);
    }


    private static String queryBoatByID =
            "select * from BOAT where BOAT_ID=";
    private static String queryRelationCompetitorsByBoat =
            "select * from REL_BOAT_USER where USER_ID=";
}
