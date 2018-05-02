package test;

import com.windhound.server.database.DBManager;
import com.windhound.server.race.Race;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class StructureControllerTest
{
    @Test
    public static void addGetRace()
    {
        Instant startDate = Instant.ofEpochMilli(1525232682000l);
        Instant endDate   = Instant.ofEpochMilli(1525232782000l);

        HashSet<Long> admins = new HashSet<>();
        admins.add(1l);
        admins.add(2l);
        HashSet<Long> boats  = new HashSet<>();
        boats.add(50l);
        boats.add(54l);
        boats.add(57l);
        HashSet<Long> events = new HashSet<>();
        events.add(2l);

        Race race = new Race(
                null,
                "test race",
                startDate,
                endDate,
                admins,
                boats,
                events
        );

        Long id = DBManager.saveOrUpdateStructureElement(Race.class, race);

        Race dbRace = (Race)DBManager.loadStructureElement(Race.class, id);

        assertEquals(dbRace.getID(), id);
        assertEquals(dbRace.getStartDate().toEpochMilli(), startDate.toEpochMilli());
        assertEquals(dbRace.getEndDate().toEpochMilli(),   endDate.toEpochMilli());
        assertEquals(dbRace.getAdmins().toString(),       admins.toString());
        assertEquals(dbRace.getSubordinates().toString(), boats.toString());
        assertEquals(dbRace.getManagers().toString(),     events.toString());
    }
}