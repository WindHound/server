package com.windhound.server.race;

import java.util.Calendar;
import java.util.HashSet;

public class Event extends ManageableElement<Race, Championship>
{
    private Calendar startDate;
    private Calendar endDate;
    //private Long longitude, latitude;

    public Event(Long          a_id,
                 String        a_name,
                 Calendar      a_startDate,
                 Calendar      a_endDate,
                 HashSet<Long> a_admins,
                 HashSet<Long> a_races,
                 HashSet<Long> a_championships)
    {
        super(a_id, a_name, a_admins, a_races, a_championships);

        startDate = a_startDate;
        endDate   = a_endDate;
    }
/*
    public static Event createEvent(Long          a_id,
                                    String        a_name,
                                    Calendar      a_startDate,
                                    Calendar      a_endDate,
                                    HashSet<Long> a_admins,
                                    HashSet<Long> a_races,
                                    HashSet<Long> a_championships)
    {
        Event event = StructureManager.getEvent(a_id);
        if (event == null)
        {
            event = new Event(a_id, a_name, a_startDate,  a_endDate, a_admins, a_races, a_championships);
            StructureManager.saveOrUpdateEvent(event);
        } else
            throw new ExceptionInInitializerError("Event already exists");

        return event;
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