package com.windhound.server.race;

import java.util.HashSet;

public abstract class Structured
{
    protected HashSet<Structured> children;
    protected HashSet<Structured> parents;

    public Structured()
    {
        children = new HashSet<>();
        parents  = new HashSet<>();
    }

    protected boolean hasChild(Structured child)
    {
        return children.contains(child);
    }

    protected boolean hasParent(Structured parent)
    {
        return parents.contains(parent);
    }

    protected void addChild(Structured child)
    {
        if (!hasChild(child))
            children.add(child);
    }

    protected void addParent(Structured parent)
    {
        if (!hasParent(parent))
            parents.add(parent);
    }
}
