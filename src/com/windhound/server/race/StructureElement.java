package com.windhound.server.race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.windhound.server.database.DBManager;

import org.springframework.core.GenericTypeResolver;

public abstract class StructureElement<S extends StructureElement, M extends StructureElement>
{
    protected Long   id;
    protected String name;

    protected HashSet<Long> subordinates;
    protected HashSet<Long> managers;

    private Class<S> subordinateType;
    private Class<M> managerType;

    protected StructureElement(Long          a_id,
                               String        a_name,
                               HashSet<Long> a_subordinates,
                               HashSet<Long> a_managers)
    {
        id           = a_id;
        name         = a_name;
        subordinates = a_subordinates;
        managers     = a_managers;

        Class[] classes = GenericTypeResolver.resolveTypeArguments(getClass(), StructureElement.class);
        subordinateType = classes[0];
        managerType     = classes[1];
    }

    public void loadStructure()
    {
        List<Long> subordinateIds = getSubordinates();
        for (Long subordinateId : subordinateIds)
        {
            S subordinate = (S)StructureManager.getStructure(subordinateType, subordinateId);
            if (subordinate == null)
            {
                subordinate = (S) DBManager.loadStructureElement(subordinateType, subordinateId);
                subordinate.loadStructure();
            }
        }

        List<Long> managerIds = getManagers();
        for (Long managerId : managerIds)
        {
            M manager = (M)StructureManager.getStructure(managerType, managerId);
            if (manager == null)
            {
                manager = (M) DBManager.loadStructureElement(managerType, managerId);
                manager.loadStructure();
            }
        }
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

    public Long getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}