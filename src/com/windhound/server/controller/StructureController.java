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
    @RequestMapping("/structure/championship/{id}")
    public Championship getChampionship(@PathVariable Long id)
    {
        return StructureManager.getOrLoadChampionship(id);
    }

    @RequestMapping("/structure/event/{id}")
    public Event getEvent(@PathVariable Long id)
    {
        return StructureManager.getOrLoadEvent(id);
    }

    @RequestMapping("/structure/race/{id}")
    public Race getRace(@PathVariable Long id)
    {
        return StructureManager.getOrLoadRace(id);
    }

    @RequestMapping("/structure/boat/{id}")
    public Boat getBoat(@PathVariable Long id)
    {
        return StructureManager.getOrLoadBoat(id);
    }
    
    @RequestMapping("/structure/competitor/{id}")
    public Competitor getCompetitor(@PathVariable Long id)
    {
        return StructureManager.getOrLoadCompetitor(id);
    }
}