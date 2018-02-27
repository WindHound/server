package com.windhound.server.race;

import oracle.sql.TIMESTAMP;

import java.util.HashSet;

public class Championship extends ManageableElement<Event, Championship>
{
    private TIMESTAMP start_date;
    private TIMESTAMP end_date;

    //TODO: How to set variables individually?
    private Championship(Long          a_id,
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
            StructureManager.addChampionship(championship);
        } else
            throw new ExceptionInInitializerError("Championship already exists");

        return championship;
    }

    public TIMESTAMP getStartDate() {
        return start_date;
    }

    public void setStartDate(TIMESTAMP start_date) {
        this.start_date = start_date;
    }

    public TIMESTAMP getEndDate() {
        return end_date;
    }

    public void setEndDate(TIMESTAMP end_date) {
        this.end_date = end_date;
    }
}