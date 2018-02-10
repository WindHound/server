package com.windhound.server.race;

import java.util.HashSet;

public class Race extends ManageableElement
{
    //private Date     date;
    //private ArrayList<Courses/Classes> courses;

    private Race(Long          a_id,
                 String        a_name,
                 HashSet<Long> a_admins,
                 HashSet<Long> a_boats,
                 HashSet<Long> a_events)
    {
        super(a_id, a_name, a_admins, a_boats, a_events);
    }

    public static Race createRace(Long          a_id,
                                  String        a_name,
                                  HashSet<Long> a_admins,
                                  HashSet<Long> a_boats,
                                  HashSet<Long> a_events)
    {
        Race race = StructureManager.getRace(a_id);
        if (race == null)
        {
            race = new Race(a_id, a_name, a_admins, a_boats, a_events);
            StructureManager.addRace(race);
        } else
            throw new ExceptionInInitializerError("Race already exists");

        return race;
    }
}