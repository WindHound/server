package com.windhound.server.race;

public class Event extends Structure
{
    private Event(Long a_id)//, String a_name)
    {
        super(a_id);//, a_name);
    }

    public static Event getInstance(Long a_id)//, String a_name)
    {
        Event event = StructureManager.getEvent(a_id);
        if (event == null)
        {
            event = new Event(a_id);//, a_name);
            StructureManager.addEvent(event);
        }

        return event;
    }
}