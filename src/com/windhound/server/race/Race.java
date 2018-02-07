package com.windhound.server.race;

public class Race extends Stage<Event, Boat>
{
    public Race(long a_id, String a_name)
    {
        super(a_id, a_name);
    }

    /*
    public boolean hasBoat(Boat boat)
    {
        return hasSubordinate(boat);
    }

    public boolean hasEvent(Event event)
    {
        return hasManager(event);
    }

    public void addBoat(Boat boat)
    {
        addSubordinate(boat);
    }

    public void addEvent(Event event)
    {
        addManager(event);
    }
    //*/
}