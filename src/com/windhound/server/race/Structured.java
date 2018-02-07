package com.windhound.server.race;

import java.util.HashSet;

public abstract class Structured<S, M>
{
    protected HashSet<S> subordinates;
    protected HashSet<M> managers;

    public Structured()
    {
        subordinates = new HashSet<>();
        managers     = new HashSet<>();
    }

    public boolean hasSubordinate(S subordinate)
    {
        return subordinates.contains(subordinate);
    }

    public boolean hasManager(M manager)
    {
        return managers.contains(manager);
    }

    public void addSubordinate(S subordinate)
    {
        if (!hasSubordinate(subordinate))
            subordinates.add(subordinate);
    }

    public void addManager(M manager)
    {
        if (!hasManager(manager))
            managers.add(manager);
    }
}