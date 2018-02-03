package com.windhound.server.race.stage;

import java.util.ArrayList;

public class Championship extends Stage
{
    private String name;
    private ArrayList<Event> events;

    public Championship()
    {

    }

    public Championship(long a_id, String a_name)
    {
        super(a_id, a_name);
    }
}
