package com.windhound.server.race;

import java.util.HashSet;

public class Boat extends Structured<Race, Competitor>
{
    private long   boatId;
    private String name;
    //private RaceClass class;

    private HashSet<Competitor> competitors;
    private HashSet<Race>       races;

    public Boat(long a_boatId, String a_name)
    {
        boatId = a_boatId;
        name   = a_name;

        competitors = new HashSet<>();
        races       = new HashSet<>();
    }

    /*
    public boolean hasCompetitor(Competitor competitor)
    {
        return hasSubordinate(competitor);
    }

    public boolean hasRace(Race race)
    {
        return hasSubordinate(race);
    }

    public void addCompetitor(Competitor competitor)
    {
        addSubordinate(competitor);
    }

    public void addRace(Race race)
    {
        addManager(race);
    }
    */
}
