package com.windhound.server.race;

public class Event extends Stage<Championship, Race>
{
    public Event(long a_id, String a_name)
    {
        super(a_id, a_name);
    }

    /*
    public boolean hasRace(Race race)
    {
        return hasSubordinate(race);
    }

    public boolean hasChampionship(Championship championship)
    {
        return hasManager(championship);
    }

    public void addRace(Race race)
    {
        addSubordinate(race);
    }

    public void addChampionship(Championship championship)
    {
        addManager(championship);
    }
    //*/
}
