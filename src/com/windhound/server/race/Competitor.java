package com.windhound.server.race;

import java.util.HashSet;

public class Competitor extends StructureElement<Competitor,  Boat>
{
    public Competitor(Long          a_id,
                       String        a_name,
                       HashSet<Long> a_boats)
    {
        super(a_id, a_name, new HashSet<>(), a_boats);
    }
/*
    public static Competitor createCompetitor(Long          a_id,
                                              String        a_name,
                                              HashSet<Long> a_boats)
    {
        Competitor competitor = StructureManager.getCompetitor(a_id);
        if (competitor == null)
        {
            competitor = new Competitor(a_id, a_name, a_boats);
            StructureManager.saveOrUpdateCompetitor(competitor);
        } else
            throw new ExceptionInInitializerError("Competitor already exists");

        return competitor;
    }
    */
}