package com.windhound.server.race;

import java.util.HashSet;

public abstract class ManageableElement extends StructureElement
{
    protected HashSet<Long> admins;

    public ManageableElement(Long          a_id,
                             String        a_name,
                             HashSet<Long> a_admins,
                             HashSet<Long> a_subordinates,
                             HashSet<Long> a_managers)
    {
        super(a_id, a_name, a_subordinates, a_managers);

        admins = a_admins;
    }

    public boolean hasAdmin(Long id)
    {
        return admins.contains(id);
    }

    public void addAdmin(Long id)
    {
        if (!hasAdmin(id))
            admins.add(id);
    }
}
