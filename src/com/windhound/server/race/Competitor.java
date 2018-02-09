package com.windhound.server.race;

import java.util.HashSet;

public class Competitor extends Structure
{
    private Competitor(Long          a_id,
                       String        a_name,
                       HashSet<Long> a_subordinates,
                       HashSet<Long> a_managers)
    {
        super(a_id, a_name, a_subordinates, a_managers);
    }

    public static Competitor createCompetitor(Long          a_id,
                                              String        a_name,
                                              HashSet<Long> a_subordinates,
                                              HashSet<Long> a_managers)
    {
        Competitor competitor = StructureManager.getCompetitor(a_id);
        if (competitor == null)
        {
            competitor = new Competitor(a_id, a_name, a_subordinates, a_managers);
            StructureManager.addCompetitor(competitor);
        } else
            throw new ExceptionInInitializerError("Competitor already exists");

        return competitor;
    }
}