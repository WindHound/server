package com.windhound.server.controller;

import com.windhound.server.race.*;
import com.windhound.server.database.DBManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.time.Instant;

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
/*
    @RequestMapping("/structure/championship/all/dto/")
    public ChampionshipDTO[] getAllChampionshipsDTOs()
    {
        List<ChampionshipDTO> dtos = new ArrayList<>();
        Long[] ids = DBManager.loadAllChampionships();

        for (Long id : ids)
        {
            Championship championship = StructureManager.getOrLoadChampionship(id);
            ChampionshipDTO dto = championship.toDTO();
            dtos.add(dto);
        }

        return dtos.toArray(new ChampionshipDTO[dtos.size()]);
    }

    @RequestMapping("/structure/championship/all/structures/")
    public Championship[] getAllChampionshipsStructures()
    {
        List<Championship> structures = new ArrayList<>();
        Long[] ids = DBManager.loadAllChampionships();

        for (Long id : ids)
        {
            Championship championship = StructureManager.getOrLoadChampionship(id);
            structures.add(championship);
        }

        return structures.toArray(new Championship[structures.size()]);
    }
//*/
    @CrossOrigin
    @PostMapping("/structure/championship/add")
    public String addChampionship(@RequestBody ChampionshipDTO dto)
    {
        Championship championship = new Championship(dto);
        Long newID = DBManager.saveOrUpdateStructureElement(Championship.class, championship);

        return newID.toString();
    }

    @RequestMapping("/structure/championship/get/{id}")
    public ChampionshipDTO getChampionship(@PathVariable Long id)
    {
        Championship championship = StructureManager.getOrLoadChampionship(id);
        ChampionshipDTO dto = championship.toDTO();

        return dto;
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
    public String addEvent(@RequestBody EventDTO dto)
    {
        Event event = new Event(dto);
        Long newID = DBManager.saveOrUpdateStructureElement(Event.class, event);

        return newID.toString();
    }

    @RequestMapping("/structure/event/get/{id}")
    public EventDTO getEvent(@PathVariable Long id)
    {
        Event event = StructureManager.getOrLoadEvent(id);
        EventDTO dto = event.toDTO();

        return dto;
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
    public String addRace(@RequestBody RaceDTO dto)
    {
        Race race = new Race(dto);
        Long newID = DBManager.saveOrUpdateStructureElement(Race.class, race);

        return newID.toString();
    }

    @RequestMapping("/structure/race/get/{id}")
    public RaceDTO getRace(@PathVariable Long id)
    {
        Race race = StructureManager.getOrLoadRace(id);
        RaceDTO dto = race.toDTO();

        return dto;
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
    public String addBoat(@RequestBody BoatDTO dto)
    {
        Boat boat = new Boat(dto);
        Long newID = DBManager.saveOrUpdateStructureElement(Boat.class, boat);

        return newID.toString();
    }

    @RequestMapping("/structure/boat/get/{id}")
    public BoatDTO getBoat(@PathVariable Long id)
    {
        Boat boat = StructureManager.getOrLoadBoat(id);
        BoatDTO dto = boat.toDTO();

        return dto;
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