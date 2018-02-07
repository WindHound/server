package com.windhound.server.race;

public class Championship extends Stage
{
    public Championship(long a_id, String a_name)
    {
        super(a_id, a_name);
    }

    public boolean hasEvent(Event event)
    {
        return hasChild(event);
    }

    public void addEvent(Event event)
    {
        addChild(event);
    }
}
