package com.windhound.server.race;

public class Boat extends Structure
{
    private Boat(Long a_id)//, String a_name)
    {
        super(a_id);//, a_name);
    }

    public static Boat getInstance(Long a_id)//, String a_name)
    {
        Boat boat = StructureManager.getBoat(a_id);
        if (boat == null)
        {
            boat = new Boat(a_id);//, a_name);
            StructureManager.addBoat(boat);
        }

        return boat;
    }
}