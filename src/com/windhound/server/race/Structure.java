package com.windhound.server.race;

import com.windhound.server.DBManager;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Structure
{
    protected Long   id;
    //protected String name;

    protected HashSet<Long> subordinates;
    protected HashSet<Long> managers;

    protected Structure(Long a_eventId)//, String a_name)
    {
        id = a_eventId;
        //name    = a_name;

        subordinates = new HashSet<>();
        managers     = new HashSet<>();
    }

    public boolean hasSubordinate(Long id)
    {
        return subordinates.contains(id);
    }

    public boolean hasManager(Long id)
    {
        return managers.contains(id);
    }

    public void addSubordinate(Long id)
    {
        if (!hasSubordinate(id))
            subordinates.add(id);
    }

    public void addManager(Long id)
    {
        if (!hasManager(id))
            managers.add(id);
    }
}