package com.windhound.server.race;

import java.util.*;

public abstract class StructureElement
{
    protected Long   id;
    protected String name;

    protected HashSet<Long> subordinates;
    protected HashSet<Long> managers;

    public Long getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    protected StructureElement(Long          a_id,
                               String        a_name,
                               HashSet<Long> a_subordinates,
                               HashSet<Long> a_managers)
    {
        id           = a_id;
        name         = a_name;
        subordinates = a_subordinates;
        managers     = a_managers;
    }

    public List<Long> getSubordinates()
    {
        return Collections.unmodifiableList(new ArrayList<>(subordinates));
    }

    public List<Long> getManagers()
    {
        return Collections.unmodifiableList(new ArrayList<>(managers));
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