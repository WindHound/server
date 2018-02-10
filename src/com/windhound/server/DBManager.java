package com.windhound.server;

import com.windhound.server.race.*;

import java.util.HashSet;
import java.util.Random;

public class DBManager
{
    public static HashSet<Long> getAdmins(Long a_id)
    {
        Random        random       = new Random();
        int           count        = 1 + random.nextInt(2);
        HashSet<Long> subordinates = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            subordinates.add(new Long(i));

        return subordinates;
    }

    public static HashSet<Long> getSubordinates(Long a_id)
    {
        Random        random       = new Random();
        int           count        = random.nextInt(5);
        HashSet<Long> subordinates = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            subordinates.add(new Long(i));

        return subordinates;
    }

    public static HashSet<Long> getManagers(Long a_id)
    {
        Random        random   = new Random();
        int           count    = random.nextInt(5);
        HashSet<Long> managers = new HashSet<>();

        for (int i = 1; i <= count; ++i)
            managers.add(new Long(i));

        return managers;
    }

    public static Championship loadChampionship(Long a_id)
    {
        Long          id           = a_id;
        String        name         = "championship_id" + id.toString();
        HashSet<Long> admins       = getAdmins(id);
        HashSet<Long> events       = getSubordinates(id);
        Championship  championship = Championship.createChampionship(id, name, admins, events);

        return championship;
    }

    public static Event loadEvent(Long a_id)
    {
        Long          id            = a_id;
        String        name          = "event_id" + id.toString();
        HashSet<Long> admins        = getAdmins(id);
        HashSet<Long> races         = getSubordinates(id);
        HashSet<Long> championships = getManagers(id);
        Event         event         = Event.createEvent(id, name, admins, races, championships);

        return event;
    }
    
    public static Race loadRace(Long a_id)
    {
        Long          id     = a_id;
        String        name   = "race_id" + id.toString();
        HashSet<Long> admins = getAdmins(id);
        HashSet<Long> boats  = getSubordinates(id);
        HashSet<Long> events = getManagers(id);
        Race          race   = Race.createRace(id, name, admins, boats, events);

        return race;
    }

    public static Boat loadBoat(Long a_id)
    {
        Long          id          = a_id;
        String        name        = "boat_id" + id.toString();
        HashSet<Long> admins      = getAdmins(id);
        HashSet<Long> competitors = getSubordinates(id);
        HashSet<Long> races       = getManagers(id);
        Boat          boat        = Boat.createBoat(id, name, admins, competitors, races);

        return boat;
    }

    public static Competitor loadCompetitor(Long a_id)
    {
        Long          id         = a_id;
        String        name       = "competitor_id" + id.toString();
        HashSet<Long> boats      = getManagers(id);
        Competitor    competitor = Competitor.createCompetitor(id, name, boats);

        return competitor;
    }
}
