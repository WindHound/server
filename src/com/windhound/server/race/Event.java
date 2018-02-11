package com.windhound.server.race;

import java.util.HashSet;

public class Event extends ManageableElement<Race, Championship>
{
    //private Date     date;
    //private Location location;

    private Event(Long          a_id,
                  String        a_name,
                  HashSet<Long> a_admins,
                  HashSet<Long> a_races,
                  HashSet<Long> a_championships)
    {
        super(a_id, a_name, a_admins, a_races, a_championships);
    }

    public static Event createEvent(Long          a_id,
                                    String        a_name,
                                    HashSet<Long> a_admins,
                                    HashSet<Long> a_races,
                                    HashSet<Long> a_championships)
    {
        Event event = StructureManager.getEvent(a_id);
        if (event == null)
        {
            event = new Event(a_id, a_name, a_admins, a_races, a_championships);
            StructureManager.addEvent(event);
        } else
            throw new ExceptionInInitializerError("Event already exists");

        return event;
    }
}