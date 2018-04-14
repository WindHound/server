package com.windhound.server.race;

public class BoatDTO
{
    private Long   id;
    private String name;
    private Long[] admins;
    private Long[] competitors;
    private Long[] races;

    public BoatDTO()
    {

    }

    public BoatDTO(Long   a_id,
                   String a_name,
                   Long[] a_admins,
                   Long[] a_competitors,
                   Long[] a_races)
    {
        id          = a_id;
        name        = a_name;
        admins      = a_admins;
        competitors = a_competitors;
        races       = a_races;
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

    public Long[] getAdmins()
    {
        return admins;
    }

    public void setAdmins(Long[] admins)
    {
        this.admins = admins;
    }

    public Long[] getCompetitors()
    {
        return competitors;
    }

    public void setCompetitors(Long[] competitors)
    {
        this.competitors = competitors;
    }

    public Long[] getRaces()
    {
        return races;
    }

    public void setRaces(Long[] races)
    {
        this.races = races;
    }
}
