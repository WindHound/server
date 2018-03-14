package com.windhound.server.race;

import java.util.HashSet;

public class Boat extends ManageableElement<Competitor, Race>
{
    private BoatInfo info;

    public Boat(Long          a_id,
                String        a_name,
                HashSet<Long> a_admins,
                HashSet<Long> a_competitors,
                HashSet<Long> a_races)
    {
        super(a_id, a_name, a_admins, a_competitors, a_races);
    }
/*
    public static Boat createBoat(Long          a_id,
                                  String        a_name,
                                  HashSet<Long> a_admins,
                                  HashSet<Long> a_competitors,
                                  HashSet<Long> a_races)
    {
        Boat boat = StructureManager.getBoat(a_id);
        if (boat == null)
        {
            boat = new Boat(a_id, a_name, a_admins, a_competitors, a_races);
            StructureManager.saveOrUpdateBoat(boat);
        } else
            throw new ExceptionInInitializerError("Boat already exists");

        return boat;
    }
*/
    public BoatInfo getBoatInfo() {
        return info;
    }
}