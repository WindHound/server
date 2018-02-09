package com.windhound.server.race;

public class Race extends Structure
{
    private Race(Long a_id)//, String a_name)
    {
        super(a_id);//, a_name);
    }

    public static Race getInstance(Long a_id, String a_name)
    {
        Race race = StructureManager.getRace(a_id);
        if (race == null)
        {
            race = new Race(a_id);//, a_name);
            StructureManager.addRace(race);
        }

        return race;
    }
}