package com.windhound.server.race.stage;

import java.util.ArrayList;
import com.windhound.server.race.*;

public class Race extends Stage
{
    private String name;
    private ArrayList<Boat> boats;
    private ArrayList<Event> parentEvents;

    public Race()
    {

    }

    public Race(long a_id, String a_name)
    {
        super(a_id, a_name);
    }
}
