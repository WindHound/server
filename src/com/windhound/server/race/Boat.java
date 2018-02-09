package com.windhound.server.race;

import java.util.HashSet;

public class Boat extends Structure
{
    private Boat(Long          a_id,
                 String        a_name,
                 HashSet<Long> a_subordinates,
                 HashSet<Long> a_managers)
    {
        super(a_id, a_name, a_subordinates, a_managers);
    }

    public static Boat createBoat(Long          a_id,
                                  String        a_name,
                                  HashSet<Long> a_subordinates,
                                  HashSet<Long> a_managers)
    {
        Boat boat = StructureManager.getBoat(a_id);
        if (boat == null)
        {
            boat = new Boat(a_id, a_name, a_subordinates, a_managers);
            StructureManager.addBoat(boat);
        } else
            throw new ExceptionInInitializerError("Boat already exists");

        return boat;
    }
}