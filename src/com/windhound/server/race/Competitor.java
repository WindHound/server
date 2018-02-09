package com.windhound.server.race;

public class Competitor extends Structure
{
    private Competitor(Long a_id)//, String a_name)
    {
        super(a_id);//, a_name);
    }

    public static Competitor getInstance(Long a_id)//, String a_name)
    {
        Competitor competitor = StructureManager.getCompetitor(a_id);
        if (competitor == null)
        {
            competitor = new Competitor(a_id);//, a_name);
            StructureManager.addCompetitor(competitor);
        }

        return competitor;
    }
}
