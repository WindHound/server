package com.windhound.server.race;

import java.util.HashSet;

public class Race extends Structure
{
    private Race(Long          a_id,
                 String        a_name,
                 HashSet<Long> a_subordinates,
                 HashSet<Long> a_managers)
    {
        super(a_id, a_name, a_subordinates, a_managers);
    }

    public static Race createRace(Long          a_id,
                                  String        a_name,
                                  HashSet<Long> a_subordinates,
                                  HashSet<Long> a_managers)
    {
        Race race = StructureManager.getRace(a_id);
        if (race == null)
        {
            race = new Race(a_id, a_name, a_subordinates, a_managers);
            StructureManager.addRace(race);
        } else
            throw new ExceptionInInitializerError("Race already exists");

        return race;
    }
}