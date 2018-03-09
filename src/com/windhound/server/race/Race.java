package com.windhound.server.race;

import java.util.Calendar;
import java.util.HashSet;

public class Race extends ManageableElement<Boat, Event>
{
    private Calendar startDate;
    private Calendar endDate;
    //private ArrayList<Courses/Classes> courses;

    public Race(Long          a_id,
                String        a_name,
                Calendar      a_startDate,
                Calendar      a_endDate,
                HashSet<Long> a_admins,
                HashSet<Long> a_boats,
                HashSet<Long> a_events)
    {
        super(a_id, a_name, a_admins, a_boats, a_events);

        startDate = a_startDate;
        endDate   = a_endDate;
    }
/*
    public static Race createRace(Long          a_id,
                                  String        a_name,
                                  Calendar      a_startDate,
                                  Calendar      a_endDate,
                                  HashSet<Long> a_admins,
                                  HashSet<Long> a_boats,
                                  HashSet<Long> a_events)
    {
        Race race = StructureManager.getRace(a_id);
        if (race == null)
        {
            race = new Race(a_id, a_name, a_startDate, a_endDate, a_admins, a_boats, a_events);
            StructureManager.saveOrUpdateRace(race);
        } else
            throw new ExceptionInInitializerError("Race already exists");

        return race;
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