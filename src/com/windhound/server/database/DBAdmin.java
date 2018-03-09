package com.windhound.server.database;

import org.apache.commons.lang.text.StrSubstitutor;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class DBAdmin
{
    public static void addChampionshipAdmins(Connection connection, Long championshipID, Long[] adminIDs)
    {
        Map<String, String> adminMap = new HashMap<>();
        adminMap.put("stage_id", championshipID.toString());

        for (Long admin : adminIDs)
        {
            adminMap.put("user_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertChampionshipAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteChampionshipAdmins(Connection connection, Long championshipID)
    {
        Map<String, String> adminMap = new HashMap<>();

        adminMap.put("stage_id", championshipID.toString());
        StrSubstitutor adminSub = new StrSubstitutor(adminMap);
        String finalAdminQuery = adminSub.replace(queryDeleteChampionshipAdmins);

        DBManager.executeSetQuery(connection, finalAdminQuery);
    }

    //
    // Query strings
    //
    private static String queryInsertChampionshipAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (        " +
                    "'${user_id}'                                              ," +
                    "'Championship'                                            ," +
                    "'${stage_id}'                                             )";
    private static String queryDeleteChampionshipAdmins =
            "DELETE FROM ADMINS WHERE STAGE_ID=${stage_id} AND STAGE_TYPE='Championship'";
}
