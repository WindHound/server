package com.windhound.server;

import com.windhound.server.race.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class DBManager
{
    public static StructureElement loadStructureElement(Class type, Long id)
    {
        if (type == Championship.class)
            return loadChampionship(id);
        if (type == Event.class)
            return loadEvent(id);
        if (type == Race.class)
            return loadRace(id);
        if (type == Boat.class)
            return loadBoat(id);
        if (type == Competitor.class)
            return loadCompetitor(id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager");
    }

    public static HashSet<Long> loadAdmins(Long a_id)
    {
        Random        random       = new Random();
        int           count        = 1 + random.nextInt(1);
        HashSet<Long> subordinates = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            subordinates.add(new Long(i));

        return subordinates;
    }

    public static HashSet<Long> loadSubordinates(Long a_id)
    {
        Random        random       = new Random();
        int           count        = 1 + random.nextInt(2);
        HashSet<Long> subordinates = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            subordinates.add(new Long(i));

        return subordinates;
    }

    public static HashSet<Long> loadManagers(Long a_id)
    {
        Random        random   = new Random();
        int           count    = 1 + random.nextInt(2);
        HashSet<Long> managers = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            managers.add(new Long(i));

        return managers;
    }

    public static Championship loadChampionship(Long a_id)
    {
        Long          id           = a_id;
        String        name         = "championship_id" + id.toString();
        HashSet<Long> admins       = loadAdmins(id);
        HashSet<Long> events       = loadSubordinates(id);
        Championship  championship = Championship.createChampionship(id, name, admins, events);

        return championship;
    }

    public static Event loadEvent(Long a_id)
    {
        Long          id            = a_id;
        String        name          = "event_id" + id.toString();
        HashSet<Long> admins        = loadAdmins(id);
        HashSet<Long> races         = loadSubordinates(id);
        HashSet<Long> championships = loadManagers(id);
        Event         event         = Event.createEvent(id, name, admins, races, championships);

        return event;
    }
    
    public static Race loadRace(Long a_id)
    {
        Long          id     = a_id;
        String        name   = "race_id" + id.toString();
        HashSet<Long> admins = loadAdmins(id);
        HashSet<Long> boats  = loadSubordinates(id);
        HashSet<Long> events = loadManagers(id);
        Race          race   = Race.createRace(id, name, admins, boats, events);

        return race;
    }

    public static Boat loadBoat(Long a_id)
    {
        Long          id          = a_id;
        String        name        = "boat_id" + id.toString();
        HashSet<Long> admins      = loadAdmins(id);
        HashSet<Long> competitors = loadSubordinates(id);
        HashSet<Long> races       = loadManagers(id);
        Boat          boat        = Boat.createBoat(id, name, admins, competitors, races);

        return boat;
    }

    public static Competitor loadCompetitor(Long a_id)
    {
        Long          id         = a_id;
        String        name       = "competitor_id" + id.toString();
        HashSet<Long> boats      = loadManagers(id);
        Competitor    competitor = Competitor.createCompetitor(id, name, boats);

        return competitor;
    }
    //
    // Load all IDs
    //
    public static Long[] loadAllChampionships()
    {
        Random          random        = new Random();
        int             count         = random.nextInt(5);
        ArrayList<Long> championships = new ArrayList<>();

        for (int i = 1; i <= count; ++i)
            championships.add(new Long(i));

        return championships.toArray(new Long[championships.size()]);
    }

    public static Long[] loadAllEvents()
    {
        Random          random        = new Random();
        int             count         = random.nextInt(5);
        ArrayList<Long> events = new ArrayList<>();

        for (int i = 1; i <= count; ++i)
            events.add(new Long(i));

        return events.toArray(new Long[events.size()]);
    }

    public static Long[] loadAllRaces()
    {
        Random          random        = new Random();
        int             count         = random.nextInt(5);
        ArrayList<Long> races = new ArrayList<>();

        for (int i = 1; i <= count; ++i)
            races.add(new Long(i));

        return races.toArray(new Long[races.size()]);
    }

    public static Long[] loadAllBoats()
    {
        Random          random        = new Random();
        int             count         = random.nextInt(5);
        ArrayList<Long> boats = new ArrayList<>();

        for (int i = 1; i <= count; ++i)
            boats.add(new Long(i));

        return boats.toArray(new Long[boats.size()]);
    }
}
