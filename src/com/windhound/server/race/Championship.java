package com.windhound.server.race;

import java.util.Calendar;

import java.util.HashSet;

public class Championship extends ManageableElement<Event, Championship>
{
    private Calendar startDate;
    private Calendar endDate;

    public Championship(Long          a_id,
                        String        a_name,
                        Calendar      a_startDate,
                        Calendar      a_endDate,
                        HashSet<Long> a_admins,
                        HashSet<Long> a_events)
    {
        super(a_id, a_name, a_admins, a_events, new HashSet<>());

        startDate = a_startDate;
        endDate   = a_endDate;
    }
/*
    public static Championship createChampionship(Long          a_id,
                                                  String        a_name,
                                                  Calendar      a_startDate,
                                                  Calendar      a_endDate,
                                                  HashSet<Long> a_admins,
                                                  HashSet<Long> a_events)
    {
        Championship championship = StructureManager.getChampionship(a_id);
        if (championship == null)
        {
            championship = new Championship(a_id, a_name, a_startDate, a_endDate, a_admins, a_events);
            StructureManager.saveOrUpdateChampionship(championship);
        } else
            throw new ExceptionInInitializerError("Championship already exists");

        return championship;
    }
*/
    public Calendar getStartDate()
    {
        return startDate;
    }

    public Calendar getEndDate()
    {
        return endDate;
    }
}