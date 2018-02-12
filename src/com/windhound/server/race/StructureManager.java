package com.windhound.server.race;

import com.windhound.server.DBManager;

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
    public static Championship getChampionship(Long id)
    {
        return championships.get(id);
    }

    public static Event getEvent(Long id)
    {
        return events.get(id);
    }

    public static Race getRace(Long id)
    {
        return races.get(id);
    }

    public static Boat getBoat(Long id)
    {
        return boats.get(id);
    }

    public static Competitor getCompetitor(Long id)
    {
        return competitors.get(id);
    }

    public static StructureElement getStructure(Class type, Long id)
    {
        if (type == Championship.class)
            return getChampionship(id);
        if (type == Event.class)
            return getEvent(id);
        if (type == Race.class)
            return getRace(id);
        if (type == Boat.class)
            return getBoat(id);
        if (type == Competitor.class)
            return getCompetitor(id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager");
    }
    //
    // Get or load
    //
    public static Championship getOrLoadChampionship(Long id)
    {
        Championship championship = championships.get(id);
        if (championship != null)
            return championship;

        championship = DBManager.loadChampionship(id);

        return championship;
    }

    public static Event getOrLoadEvent(Long id)
    {
        Event event = events.get(id);
        if (event != null)
            return event;

        event = DBManager.loadEvent(id);

        return event;
    }

    public static Race getOrLoadRace(Long id)
    {
        Race race = races.get(id);
        if (race != null)
            return race;

        race = DBManager.loadRace(id);

        return race;
    }

    public static Boat getOrLoadBoat(Long id)
    {
        Boat boat = boats.get(id);
        if (boat != null)
            return boat;

        boat = DBManager.loadBoat(id);

        return boat;
    }

    public static Competitor getOrLoadCompetitor(Long id)
    {
        Competitor competitor = competitors.get(id);
        if (competitor != null)
            return competitor;

        competitor = DBManager.loadCompetitor(id);

        return competitor;
    }

    public static StructureElement getOrLoadStructure(Class type, Long id)
    {
        if (type == Championship.class)
            return getOrLoadChampionship(id);
        if (type == Event.class)
            return getOrLoadEvent(id);
        if (type == Race.class)
            return getOrLoadRace(id);
        if (type == Boat.class)
            return getOrLoadBoat(id);
        if (type == Competitor.class)
            return getOrLoadCompetitor(id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager");
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
