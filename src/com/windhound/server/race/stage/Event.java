package com.windhound.server.race.stage;

import java.util.ArrayList;

public class Event extends Stage
{
    private String                  name;
    private ArrayList<Race>         races;
    private ArrayList<Championship> parentChampionships;

    public Event()
    {

    }

    public Event(long a_id, String a_name)
    {
        super(a_id, a_name);
    }
}
