package com.windhound.server.race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class ManageableElement<S extends StructureElement, M extends StructureElement> extends StructureElement<S, M>
{
    protected HashSet<Long> admins;

    protected ManageableElement(Long          a_id,
                                String        a_name,
                                HashSet<Long> a_admins,
                                HashSet<Long> a_subordinates,
                                HashSet<Long> a_managers)
    {
        super(a_id, a_name, a_subordinates, a_managers);

        admins = a_admins;
    }

    public List<Long> getAdmins()
    {
        return Collections.unmodifiableList(new ArrayList<>(admins));
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
