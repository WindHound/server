package com.windhound.server;

//import com.windhound.server.race.*;

import com.windhound.server.race.*;

import java.util.ArrayList;
import java.util.Random;

public class DBManager
{
    public static ArrayList<Long> getSubordinates(Long a_id)
    {
        Random random = new Random();
        int count     = random.nextInt(5);
        ArrayList<Long> subordinates = new ArrayList<>();

        for (int i = 1; i <= count; ++i)
            subordinates.add(new Long(i));

        return subordinates;
    }

    public static ArrayList<Long> getManagers(Long a_id)
    {
        Random random = new Random();
        int count     = random.nextInt(5);
        ArrayList<Long> managers = new ArrayList<>();

        for (int i = 1; i <= count; ++i)
            managers.add(new Long(i));

        return managers;
    }

    public static Championship loadChampionship(Long a_id)
    {
        Long id                   = a_id;
        String name               = "race_id" + id.toString();
        Championship championship = Championship.createChampionship(id, name);

        return championship;
    }

    public static Event loadEvent(Long a_id)
    {
        Long id     = a_id;
        String name = "race_id" + id.toString();
        Event event = Event.createEvent(id, name);

        return event;
    }
    
    public static Race loadRace(Long a_id)
    {
        Long id     = a_id;
        String name = "race_id" + id.toString();
        Race race   = Race.createRace(id, name);

        return race;
    }

    public static Boat loadBoat(Long a_id)
    {
        Long id     = a_id;
        String name = "boat_id" + id.toString();
        Boat boat   = Boat.createBoat(id, name);

        return boat;
    }

    public static Competitor loadCompetitor(Long a_id)
    {
        Long id               = a_id;
        String name           = "competitor_id" + id.toString();
        Competitor competitor = Competitor.createCompetitor(id, name);

        return competitor;
    }
}
