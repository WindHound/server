package com.windhound.server.controller;

import com.windhound.server.race.*;
import com.windhound.server.database.DBManager;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

@RestController
public class StructureController
{
    //
    // Championships
    //
    @RequestMapping("/structure/championship/all")
    public Long[] getAllChampionships()
    {
        return DBManager.loadAllChampionships();
    }

    @CrossOrigin
    @PostMapping("/structure/championship/add")
    public String addChampionship(@RequestParam(value = "id")             Long id,
                                  @RequestParam(value = "name")           String name,
                                  @RequestParam(value = "startDate")      Long startDateMilli,
                                  @RequestParam(value = "endDate")        Long endDateMilli,
                                  @RequestParam(value = "admins[]")       List<Long> admins,
                                  @RequestParam(value = "subordinates[]") List<Long> subordinates)
    {
        Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar endDate   = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        startDate.setTimeInMillis(startDateMilli);
        endDate.setTimeInMillis(endDateMilli);

        Championship championship = new Championship(
                id,
                name,
                startDate,
                endDate,
                new HashSet<>(admins),
                new HashSet<>(subordinates)
        );

        Long newID = DBManager.saveOrUpdateStructureElement(Championship.class, championship);

        return newID.toString();
    }

    @RequestMapping("/structure/championship/get/{id}")
    public Championship getChampionship(@PathVariable Long id)
    {
        return StructureManager.getOrLoadChampionship(id);
    }

    //
    // Events
    //
    @RequestMapping("/structure/event/all")
    public Long[] getAllEvents()
    {
        return DBManager.loadAllEvents();
    }

    @CrossOrigin
    @PostMapping("/structure/event/add")
    public String addEvent(@RequestParam(value = "id")             Long id,
                           @RequestParam(value = "name")           String name,
                           @RequestParam(value = "startDate")      Long startDateMilli,
                           @RequestParam(value = "endDate")        Long endDateMilli,
                           @RequestParam(value = "admins[]")       List<Long> admins,
                           @RequestParam(value = "subordinates[]") List<Long> subordinates,
                           @RequestParam(value = "managers[]")     List<Long> managers)
    {
        Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar endDate   = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        startDate.setTimeInMillis(startDateMilli);
        endDate.setTimeInMillis(endDateMilli);

        Event event = new Event(
                id,
                name,
                startDate,
                endDate,
                new HashSet<>(admins),
                new HashSet<>(subordinates),
                new HashSet<>(managers)
        );

        Long newID = DBManager.saveOrUpdateStructureElement(Event.class, event);

        return newID.toString();
    }

    @RequestMapping("/structure/event/get/{id}")
    public Event getEvent(@PathVariable Long id)
    {
        return StructureManager.getOrLoadEvent(id);
    }

    //
    // Races
    //
    @RequestMapping("/structure/race/all")
    public Long[] getAllRaces()
    {
        return DBManager.loadAllRaces();
    }

    @CrossOrigin
    @PostMapping("/structure/race/add")
    public String addRace(@RequestParam(value = "id")             Long id,
                          @RequestParam(value = "name")           String name,
                          @RequestParam(value = "startDate")      Long startDateMilli,
                          @RequestParam(value = "endDate")        Long endDateMilli,
                          @RequestParam(value = "admins[]")       List<Long> admins,
                          @RequestParam(value = "subordinates[]") List<Long> subordinates,
                          @RequestParam(value = "managers[]")     List<Long> managers)
    {
        Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar endDate   = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        startDate.setTimeInMillis(startDateMilli);
        endDate.setTimeInMillis(endDateMilli);

        Race race = new Race(
                id,
                name,
                startDate,
                endDate,
                new HashSet<>(admins),
                new HashSet<>(subordinates),
                new HashSet<>(managers)
        );

        Long newID = DBManager.saveOrUpdateStructureElement(Race.class, race);

        return newID.toString();
    }

    @RequestMapping("/structure/race/get/{id}")
    public Race getRace(@PathVariable Long id)
    {
        return StructureManager.getOrLoadRace(id);
    }

    //
    // Boats
    //
    @RequestMapping("/structure/boat/all")
    public Long[] getAllBoats()
    {
        return DBManager.loadAllBoats();
    }

    @CrossOrigin
    @PostMapping("/structure/boat/add")
    public String addBoat(@RequestParam(value = "id") Long id,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "admins[]") List<Long> admins,
                          @RequestParam(value = "subordinates[]") List<Long> subordinates,
                          @RequestParam(value = "managers[]") List<Long> managers)
    {
        Boat boat = new Boat(
                id,
                name,
                new HashSet<>(admins),
                new HashSet<>(subordinates),
                new HashSet<>(managers)
        );

        Long newID = DBManager.saveOrUpdateStructureElement(Boat.class, boat);

        return newID.toString();
    }

    @RequestMapping("/structure/boat/get/{id}")
    public Boat getBoat(@PathVariable Long id)
    {
        return StructureManager.getOrLoadBoat(id);
    }

    //
    // Competitors
    //
    /*
    @CrossOrigin
    @PostMapping("/structure/add/competitor")
    public String addCompetitor(@RequestParam(value = "id") Long id,
                                @RequestParam(value = "name") String name,
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
    */
    /*
    @RequestMapping("/structure/competitor/get/{id}")
    public Competitor getCompetitor(@PathVariable Long id)
    {
        return StructureManager.getOrLoadCompetitor(id);
    }*/
}