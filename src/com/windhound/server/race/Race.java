package com.windhound.server.race;

import oracle.sql.TIMESTAMP;

import java.util.HashSet;

public class Race extends ManageableElement<Boat, Event>
{
    private TIMESTAMP start_date, end_date;
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

    public TIMESTAMP getStartDate() {
        return start_date;
    }

    public void setStartDate(TIMESTAMP start_date) {
        this.start_date = start_date;
    }

    public TIMESTAMP getEndDate() {
        return end_date;
    }

    public void setEndDate(TIMESTAMP end_date) {
        this.end_date = end_date;
    }
}