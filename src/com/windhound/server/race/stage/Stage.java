package com.windhound.server.race.stage;

public abstract class Stage
{
    private long   id;
    private String name;

    public Stage()
    {

    }

    public Stage(long a_id, String a_name)
    {
        id   = a_id;
        name = a_name;
    }
}
