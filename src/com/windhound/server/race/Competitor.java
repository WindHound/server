package com.windhound.server.race;

import java.util.HashSet;

public class Competitor extends Structured<Boat, Competitor>
{
    private long   userId;
    private String name;

    private HashSet<Boat> boats;

    public Competitor(long a_userId, String a_name)
    {
        userId = a_userId;
        name   = a_name;

        boats = new HashSet<>();
    }

    /*
    public boolean hasBoat(Boat boat)
    {
        return hasManager(boat);
    }

    public void addBoat(Boat boat)
    {
        addManager(boat);
    }
    //*/
}
