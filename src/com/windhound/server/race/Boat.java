package com.windhound.server.race;

import java.util.HashSet;

public class Boat extends ManageableElement
{
    //private BoatInfo info;

    private Boat(Long          a_id,
                 String        a_name,
                 HashSet<Long> a_admins,
                 HashSet<Long> a_competitors,
                 HashSet<Long> a_boats)
    {
        super(a_id, a_name, a_admins, a_competitors, a_boats);
    }

    public static Boat createBoat(Long          a_id,
                                  String        a_name,
                                  HashSet<Long> a_admins,
                                  HashSet<Long> a_competitors,
                                  HashSet<Long> a_boats)
    {
        Boat boat = StructureManager.getBoat(a_id);
        if (boat == null)
        {
            boat = new Boat(a_id, a_name, a_admins, a_competitors, a_boats);
            StructureManager.addBoat(boat);
        } else
            throw new ExceptionInInitializerError("Boat already exists");

        return boat;
    }
}