package com.windhound.server.race;

import java.util.HashSet;

public class Championship extends Structure
{
    private Championship(Long          a_id,
                         String        a_name,
                         HashSet<Long> a_subordinates,
                         HashSet<Long> a_managers)
    {
        super(a_id, a_name, a_subordinates, a_managers);
    }

    public static Championship createChampionship(Long          a_id,
                                                  String        a_name,
                                                  HashSet<Long> a_subordinates,
                                                  HashSet<Long> a_managers)
    {
        Championship championship = StructureManager.getChampionship(a_id);
        if (championship == null)
        {
            championship = new Championship(a_id, a_name, a_subordinates, a_managers);
            StructureManager.addChampionship(championship);
        } else
            throw new ExceptionInInitializerError("Championship already exists");

        return championship;
    }
}