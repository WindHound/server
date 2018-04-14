package com.windhound.server.race;

public class ChampionshipDTO
{
    private Long   id;
    private String name;
    private Long   startDate;
    private Long   endDate;
    private Long[] admins;
    private Long[] events;

    public ChampionshipDTO()
    {

    }

    public ChampionshipDTO(Long   a_id,
                           String a_name,
                           Long   a_startDate,
                           Long   a_endDate,
                           Long[] a_admins,
                           Long[] a_events)
    {
        id        = a_id;
        name      = a_name;
        startDate = a_startDate;
        endDate   = a_endDate;
        admins    = a_admins;
        events    = a_events;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Long startDate)
    {
        this.startDate = startDate;
    }

    public Long getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Long endDate)
    {
        this.endDate = endDate;
    }

    public Long[] getAdmins()
    {
        return admins;
    }

    public void setAdmins(Long[] admins)
    {
        this.admins = admins;
    }

    public Long[] getEvents()
    {
        return events;
    }

    public void setEvents(Long[] events)
    {
        this.events = events;
    }
}
