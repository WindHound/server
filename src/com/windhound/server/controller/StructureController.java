package com.windhound.server.controller;

import com.windhound.server.DBManager;
import com.windhound.server.race.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StructureController
{
    // Championships
    @RequestMapping("/structure/championship/all")
    public Long[] getAllChampionship()
    {
        return DBManager.getAllChampionships();
    }

    @RequestMapping("/structure/championship/{id}")
    public Championship getChampionship(@PathVariable Long id)
    {
        return StructureManager.getOrLoadChampionship(id);
    }

    // Events
    @RequestMapping("/structure/event/all")
    public Long[] getAllEvent()
    {
        return DBManager.getAllEvents();
    }

    @RequestMapping("/structure/event/{id}")
    public Event getEvent(@PathVariable Long id)
    {
        return StructureManager.getOrLoadEvent(id);
    }

    // Races
    @RequestMapping("/structure/race/all")
    public Long[] getAllRace()
    {
        return DBManager.getAllRaces();
    }

    @RequestMapping("/structure/race/{id}")
    public Race getRace(@PathVariable Long id)
    {
        return StructureManager.getOrLoadRace(id);
    }

    // Boats
    @RequestMapping("/structure/boat/all")
    public Long[] getAllBoat()
    {
        return DBManager.getAllBoats();
    }

    @RequestMapping("/structure/boat/{id}")
    public Boat getBoat(@PathVariable Long id)
    {
        return StructureManager.getOrLoadBoat(id);
    }

    // Competitors
    
    @RequestMapping("/structure/competitor/{id}")
    public Competitor getCompetitor(@PathVariable Long id)
    {
        return StructureManager.getOrLoadCompetitor(id);
    }
}