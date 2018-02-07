package com.windhound.server.race;

public class Event extends Stage
{
    public Event(long a_id, String a_name)
    {
        super(a_id, a_name);
    }

    public boolean hasRace(Race race)
    {
        return hasChild(race);
    }

    public boolean hasChampionship(Championship championship)
    {
        return hasParent(championship);
    }

    public void addRace(Race race)
    {
        addChild(race);
    }

    public void addChampionship(Championship championship)
    {
        addParent(championship);
    }
}
