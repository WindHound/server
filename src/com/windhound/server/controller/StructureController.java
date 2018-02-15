package com.windhound.server.controller;

import com.windhound.server.DBManager;
import com.windhound.server.race.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
public class StructureController
{
    // Championships
    @RequestMapping("/structure/all/championship/")
    public Long[] getAllChampionship()
    {
        return DBManager.loadAllChampionships();
    }

    @CrossOrigin
    @PostMapping("/structure/add/championship/")
    public String addChampionship(@RequestParam(value = "id")             Long       id,
                                  @RequestParam(value = "name")           String     name,
                                  @RequestParam(value = "admins[]")       List<Long> admins,
                                  @RequestParam(value = "subordinates[]") List<Long> subordinates)
    {
        Championship championship = new Championship(
                id,
                name,
                new HashSet<>(admins),
                new HashSet<>(subordinates)
        );
        StructureManager.saveOrUpdateChampionship(championship);

        return "accept";
    }

    @RequestMapping("/structure/get/championship/{id}")
    public Championship getChampionship(@PathVariable Long id)
    {
        return StructureManager.getOrLoadChampionship(id);
    }

    // Events
    @RequestMapping("/structure/all/event/")
    public Long[] getAllEvent()
    {
        return DBManager.loadAllEvents();
    }

    @CrossOrigin
    @PostMapping("/structure/add/event/")
    public String addEvent(@RequestParam(value = "id")             Long       id,
                           @RequestParam(value = "name")           String     name,
                           @RequestParam(value = "admins[]")       List<Long> admins,
                           @RequestParam(value = "subordinates[]") List<Long> subordinates,
                           @RequestParam(value = "managers[]")     List<Long> managers)
    {
        Event event = new Event(
                id,
                name,
                new HashSet<>(admins),
                new HashSet<>(subordinates),
                new HashSet<>(managers)
        );
        StructureManager.saveOrUpdateEvent(event);

        return "accept";
    }

    @RequestMapping("/structure/get/event/{id}")
    public Event getEvent(@PathVariable Long id)
    {
        return StructureManager.getOrLoadEvent(id);
    }

    // Races
    @RequestMapping("/structure/all/race/")
    public Long[] getAllRace()
    {
        return DBManager.loadAllRaces();
    }

    @CrossOrigin
    @PostMapping("/structure/add/race/")
    public String addRace(@RequestParam(value = "id")             Long       id,
                          @RequestParam(value = "name")           String     name,
                          @RequestParam(value = "admins[]")       List<Long> admins,
                          @RequestParam(value = "subordinates[]") List<Long> subordinates,
                          @RequestParam(value = "managers[]")     List<Long> managers)
    {
        Race race = new Race(
                id,
                name,
                new HashSet<>(admins),
                new HashSet<>(subordinates),
                new HashSet<>(managers)
        );
        StructureManager.saveOrUpdateRace(race);

        return "accept";
    }

    @RequestMapping("/structure/get/race/{id}")
    public Race getRace(@PathVariable Long id)
    {
        return StructureManager.getOrLoadRace(id);
    }

    // Boats
    @RequestMapping("/structure/all/boat/")
    public Long[] getAllBoat()
    {
        return DBManager.loadAllBoats();
    }

    @CrossOrigin
    @PostMapping("/structure/add/boat/")
    public String addBoat(@RequestParam(value = "id")             Long       id,
                          @RequestParam(value = "name")           String     name,
                          @RequestParam(value = "admins[]")       List<Long> admins,
                          @RequestParam(value = "subordinates[]") List<Long> subordinates,
                          @RequestParam(value = "managers[]")     List<Long> managers)
    {
        Boat boat = new Boat(
                id,
                name,
                new HashSet<>(admins),
                new HashSet<>(subordinates),
                new HashSet<>(managers)
        );
        StructureManager.saveOrUpdateBoat(boat);

        return "accept";
    }

    @RequestMapping("/structure/get/boat/{id}")
    public Boat getBoat(@PathVariable Long id)
    {
        return StructureManager.getOrLoadBoat(id);
    }

    // Competitors
    @CrossOrigin
    @PostMapping("/structure/add/competitor/")
    public String addCompetitor(@RequestParam(value = "id")         Long       id,
                                @RequestParam(value = "name")       String     name,
                                @RequestParam(value = "managers[]") List<Long> managers)
    {
        Competitor competitor = new Competitor(
                id,
                name,
                new HashSet<>(managers)
        );
        StructureManager.saveOrUpdateCompetitor(competitor);

        return "accept";
    }
    
    @RequestMapping("/structure/get/competitor/{id}")
    public Competitor getCompetitor(@PathVariable Long id)
    {
        return StructureManager.getOrLoadCompetitor(id);
    }
}