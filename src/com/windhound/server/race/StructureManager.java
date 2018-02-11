package com.windhound.server.race;

import com.windhound.server.DBManager;

import java.util.HashMap;
import java.util.List;

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

    /*
    public static void loadStructureElement(Championship championship)
    {
        List<Long> eventIds = championship.getSubordinates();
        for (Long eventId : eventIds)
        {
            Event event = getEvent(eventId);
            if (event == null)
            {
                event = DBManager.loadEvent(eventId);
                loadStructureElement(event);
            }
        }
    }

    public static void loadStructureElement(Event event)
    {
        List<long> raceIds
        for (Long raceId : event.getSubordinates())
        {
            Race race = getRace(raceId);
            if (race == null)
            {
                race = DBManager.loadRace(raceId);
                loadStructureElement(race);
            }
        }

        for (Long championshipId : event.getManagers())
        {
            Championship championship = getChampionship(championshipId);
            if (championship == null)
            {
                championship = DBManager.loadChampionship(championshipId);
                loadStructureElement(championship);
            }
        }
    }

    public static void loadStructureElement(Race race)
    {
        for (Long boatId : race.getSubordinates())
        {
            Boat boat = getBoat(boatId);
            if (boat == null)
            {
                boat = DBManager.loadBoat(boatId);
                loadStructureElement(boat);
            }
        }

        for (Long eventId : race.getManagers())
        {
            Event event = getEvent(eventId);
            if (event == null)
            {
                event = DBManager.loadEvent(eventId);
                loadStructureElement(event);
            }
        }
    }

    public static void loadStructureElement(Boat boat)
    {
        for (Long boatId : boat.getSubordinates())
        {
            Boat boat = getBoat(boatId);
            if (boat == null)
            {
                boat = DBManager.loadBoat(boatId);
                loadStructureElement(boat);
            }
        }

        for (Long eventId : boat.getManagers())
        {
            Event event = getEvent(eventId);
            if (event == null)
            {
                event = DBManager.loadEvent(eventId);
                loadStructureElement(event);
            }
        }
    }
    //*/


    //
    // Get specific
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
    // Get structure
    //
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
