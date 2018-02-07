package com.windhound.server.race;

public abstract class Stage<S, M> extends Structured<S, M>
{
    protected long   eventId;
    protected String name;

    public Stage(long a_eventId, String a_name)
    {
        super();

        eventId = a_eventId;
        name    = a_name;
    }
}
