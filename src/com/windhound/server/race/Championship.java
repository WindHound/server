package com.windhound.server.race;

import java.util.HashSet;

public class Championship extends ManageableElement<Event, Championship>
{
    //private Date date;

    public Championship(Long          a_id,
                        String        a_name,
                        HashSet<Long> a_admins,
                        HashSet<Long> a_events)
    {
        super(a_id, a_name, a_admins, a_events, new HashSet<>());
    }

    public static Championship createChampionship(Long          a_id,
                                                  String        a_name,
                                                  HashSet<Long> a_admins,
                                                  HashSet<Long> a_events)
    {
        Championship championship = StructureManager.getChampionship(a_id);
        if (championship == null)
        {
            championship = new Championship(a_id, a_name, a_admins, a_events);
            StructureManager.saveOrUpdateChampionship(championship);
        } else
            throw new ExceptionInInitializerError("Championship already exists");

        return championship;
    }
}