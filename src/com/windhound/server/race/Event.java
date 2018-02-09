package com.windhound.server.race;

import java.util.HashSet;

public class Event extends Structure
{
    private Event(Long          a_id,
                  String        a_name,
                  HashSet<Long> a_subordinates,
                  HashSet<Long> a_managers)
    {
        super(a_id, a_name, a_subordinates, a_managers);
    }

    public static Event createEvent(Long          a_id,
                                    String        a_name,
                                    HashSet<Long> a_subordinates,
                                    HashSet<Long> a_managers)
    {
        Event event = StructureManager.getEvent(a_id);
        if (event == null)
        {
            event = new Event(a_id, a_name, a_subordinates, a_managers);
            StructureManager.addEvent(event);
        } else
            throw new ExceptionInInitializerError("Event already exists");

        return event;
    }
}