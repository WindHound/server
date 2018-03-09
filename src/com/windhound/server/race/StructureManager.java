package com.windhound.server.race;

import java.util.HashMap;

import com.windhound.server.database.DBManager;
import com.windhound.server.database.DBRace;

public class StructureManager
{
    private static HashMap<Long, Championship> championships;
    private static HashMap<Long, Event> events;
    private static HashMap<Long, Race> races;
    private static HashMap<Long, Boat> boats;
    private static HashMap<Long, Competitor> competitors;

    static
    {
        championships = new HashMap<>();
        events = new HashMap<>();
        races = new HashMap<>();
        boats = new HashMap<>();
        competitors = new HashMap<>();
    }

    //
    // Get
    //
    public static Championship getChampionship(Long id)
    {
        return championships.get(id);
    }

    public static Event getEvent(Long id)
    {
        return events.get(id);
    }

    public static Race getRace(Long id)
    {
        return races.get(id);
    }

    public static Boat getBoat(Long id)
    {
        return boats.get(id);
    }

    public static Competitor getCompetitor(Long id)
    {
        return competitors.get(id);
    }

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
    // Get or load
    //
    public static Championship getOrLoadChampionship(Long id)
    {
        Championship championship = championships.get(id);
        if (championship != null)
            return championship;

        championship = (Championship) DBManager.loadStructureElement(Championship.class, id);

        return championship;
    }

    public static Event getOrLoadEvent(Long id)
    {
        Event event = events.get(id);
        if (event != null)
            return event;

        event = (Event) DBManager.loadStructureElement(Event.class, id);

        return event;
    }

    public static Race getOrLoadRace(Long id)
    {
        Race race = races.get(id);
        if (race != null)
            return race;

        race = (Race) DBManager.loadStructureElement(Race.class, id);

        return race;
    }

    public static Boat getOrLoadBoat(Long id)
    {
        Boat boat = boats.get(id);
        if (boat != null)
            return boat;

        boat = (Boat) DBManager.loadStructureElement(Boat.class, id);

        return boat;
    }

    public static Competitor getOrLoadCompetitor(Long id)
    {
        Competitor competitor = competitors.get(id);
        if (competitor != null)
            return competitor;

        competitor = (Competitor) DBManager.loadStructureElement(Competitor.class, id);

        return competitor;
    }

    public static StructureElement getOrLoadStructure(Class type, Long id)
    {
        if (type == Championship.class)
            return getOrLoadChampionship(id);
        if (type == Event.class)
            return getOrLoadEvent(id);
        if (type == Race.class)
            return getOrLoadRace(id);
        if (type == Boat.class)
            return getOrLoadBoat(id);
        if (type == Competitor.class)
            return getOrLoadCompetitor(id);

        throw new IllegalArgumentException("Type must be non-abstract subtype of StructureManager");
    }
    //
    // Save or Update
    //

//TODO   write add methods without DB save/update to be able to uncomment "createStructure" methods
//
//
//  TTTTTTTTTTTTTTTTTTTTTTT     OOOOOOOOO          DDDDDDDDDDDDD             OOOOOOOOO
//  T:::::::::::::::::::::T   OO:::::::::OO        D::::::::::::DDD        OO:::::::::OO
//  T:::::::::::::::::::::T OO:::::::::::::OO      D:::::::::::::::DD    OO:::::::::::::OO
//  T:::::TT:::::::TT:::::TO:::::::OOO:::::::O     DDD:::::DDDDD:::::D  O:::::::OOO:::::::O
//  TTTTTT  T:::::T  TTTTTTO::::::O   O::::::O       D:::::D    D:::::D O::::::O   O::::::O
//          T:::::T        O:::::O     O:::::O       D:::::D     D:::::DO:::::O     O:::::O
//          T:::::T        O:::::O     O:::::O       D:::::D     D:::::DO:::::O     O:::::O
//          T:::::T        O:::::O     O:::::O       D:::::D     D:::::DO:::::O     O:::::O
//          T:::::T        O:::::O     O:::::O       D:::::D     D:::::DO:::::O     O:::::O
//          T:::::T        O:::::O     O:::::O       D:::::D     D:::::DO:::::O     O:::::O
//          T:::::T        O:::::O     O:::::O       D:::::D     D:::::DO:::::O     O:::::O
//          T:::::T        O::::::O   O::::::O       D:::::D    D:::::D O::::::O   O::::::O
//        TT:::::::TT      O:::::::OOO:::::::O     DDD:::::DDDDD:::::D  O:::::::OOO:::::::O
//        T:::::::::T       OO:::::::::::::OO      D:::::::::::::::DD    OO:::::::::::::OO
//        T:::::::::T         OO:::::::::OO        D::::::::::::DDD        OO:::::::::OO
//        TTTTTTTTTTT           OOOOOOOOO          DDDDDDDDDDDDD             OOOOOOOOO
//
//
//
//
//
//
//

    public static void saveOrUpdateChampionship(Championship championship)
    {
        championships.put(championship.id, championship);
        DBManager.saveOrUpdateStructureElement(Championship.class, championship);

        System.out.println(
                championship.getID() + " " +
                        championship.getName()
        );
    }

    public static void saveOrUpdateEvent(Event event)
    {
        events.put(event.id, event);
        DBManager.saveOrUpdateStructureElement(Event.class, event);

        System.out.println(
                event.getID() + " " +
                        event.getName()
        );
    }

    public static void saveOrUpdateRace(Race race)
    {
        races.put(race.id, race);
        DBManager.saveOrUpdateStructureElement(Race.class, race);

        System.out.println(
                race.getID() + " " +
                        race.getName()
        );
    }

    public static void saveOrUpdateBoat(Boat boat)
    {
        boats.put(boat.id, boat);
        DBManager.saveOrUpdateStructureElement(Boat.class, boat);

        System.out.println(
                boat.getID() + " " +
                        boat.getName()
        );
    }

    public static void saveOrUpdateCompetitor(Competitor competitor)
    {
        competitors.put(competitor.id, competitor);
        //DBManager.saveOrUpdateStructureElement(Competitor.class, competitor);

        System.out.println(
                competitor.getID() + " " +
                        competitor.getName()
        );
    }

}
