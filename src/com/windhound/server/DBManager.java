package com.windhound.server;

import com.windhound.server.race.*;

import com.windhound.server.race.*;

import java.util.HashSet;
import java.util.Random;

public class DBManager
{
    public static HashSet<Long> getSubordinates(Long a_id)
    {
        Random random = new Random();
        int count     = random.nextInt(5);
        HashSet<Long> subordinates = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            subordinates.add(new Long(i));

        return subordinates;
    }

    public static HashSet<Long> getManagers(Long a_id)
    {
        Random random = new Random();
        int count     = random.nextInt(5);
        HashSet<Long> managers = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            managers.add(new Long(i));

        return managers;
    }

    public static Championship loadChampionship(Long a_id)
    {
        Long          id           = a_id;
        String        name         = "race_id" + id.toString();
        HashSet<Long> subordinates = getSubordinates(id);
        HashSet<Long> managers     = new HashSet<>();//getManagers(id);
        Championship championship  = Championship.createChampionship(id, name, subordinates, managers);

        return championship;
    }

    public static Event loadEvent(Long a_id)
    {
        Long          id           = a_id;
        String        name         = "race_id" + id.toString();
        HashSet<Long> subordinates = getSubordinates(id);
        HashSet<Long> managers     = getManagers(id);
        Event event  = Event.createEvent(id, name, subordinates, managers);

        return event;
    }
    
    public static Race loadRace(Long a_id)
    {
        Long          id           = a_id;
        String        name         = "race_id" + id.toString();
        HashSet<Long> subordinates = getSubordinates(id);
        HashSet<Long> managers     = getManagers(id);
        Race race  = Race.createRace(id, name, subordinates, managers);

        return race;
    }

    public static Boat loadBoat(Long a_id)
    {
        Long          id           = a_id;
        String        name         = "race_id" + id.toString();
        HashSet<Long> subordinates = getSubordinates(id);
        HashSet<Long> managers     = getManagers(id);
        Boat boat  = Boat.createBoat(id, name, subordinates, managers);

        return boat;
    }

    public static Competitor loadCompetitor(Long a_id)
    {
        Long          id           = a_id;
        String        name         = "race_id" + id.toString();
        HashSet<Long> subordinates = new HashSet<>(); //getSubordinates(id);
        HashSet<Long> managers     = getManagers(id);
        Competitor competitor  = Competitor.createCompetitor(id, name, subordinates, managers);

        return competitor;
    }
}
