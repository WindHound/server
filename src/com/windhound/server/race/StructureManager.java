package com.windhound.server.race;

import java.util.HashMap;

public class StructureManager
{
    private static HashMap<Long, Championship> championships;
    private static HashMap<Long, Event>        events;
    private static HashMap<Long, Race>         races;
    private static HashMap<Long, Boat>         boats;
    private static HashMap<Long, Competitor>   competitors;

    static
    {
        championships = new HashMap<>();
        events        = new HashMap<>();
        races         = new HashMap<>();
        boats         = new HashMap<>();
        competitors   = new HashMap<>();
    }
    //
    // Get
    //
    public static Championship getChampionship(Long a_id)
    {
        return championships.get(a_id);
    }

    public static Event getEvent(Long a_id)
    {
        return events.get(a_id);
    }

    public static Race getRace(Long a_id)
    {
        return races.get(a_id);
    }

    public static Boat getBoat(Long a_id)
    {
        return boats.get(a_id);
    }

    public static Competitor getCompetitor(Long a_id)
    {
        return competitors.get(a_id);
    }
    //
    // Add
    //
    public static void addChampionship(Championship championship)
    {
        championships.put(championship.id, championship);
    }

    public static void addEvent(Event event)
    {
        events.put(event.id, event);
    }

    public static void addRace(Race race)
    {
        races.put(race.id, race);
    }

    public static void addBoat(Boat boat)
    {
        boats.put(boat.id, boat);
    }

    public static void addCompetitor(Competitor competitor)
    {
        competitors.put(competitor.id, competitor);
    }
}
