package com.windhound.server.race;

public class Race extends Stage
{
    public Race(long a_id, String a_name)
    {
        super(a_id, a_name);
    }

    public boolean hasBoat(Boat boat)
    {
        return hasChild(boat);
    }

    public boolean hasEvent(Event event)
    {
        return hasParent(event);
    }

    public void addBoat(Boat boat)
    {
        addChild(boat);
    }

    public void addEvent(Event event)
    {
        addParent(event);
    }
}