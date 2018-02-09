package com.windhound.server.race;

import java.util.HashSet;

public abstract class Structure
{
    protected Long   id;
    protected String name;

    protected HashSet<Long> subordinates;
    protected HashSet<Long> managers;

    protected Structure(Long          a_id,
                        String        a_name,
                        HashSet<Long> a_subordinates,
                        HashSet<Long> a_managers)
    {
        id           = a_id;
        name         = a_name;
        subordinates = a_subordinates;
        managers     = a_managers;
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