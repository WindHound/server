package com.windhound.server.race;

import java.util.Arrays;
import java.util.HashSet;

public class Boat extends ManageableElement<Competitor, Race>
{
    private BoatInfo info;

    public Boat(Long          a_id,
                String        a_name,
                HashSet<Long> a_admins,
                HashSet<Long> a_competitors,
                HashSet<Long> a_races)
    {
        super(a_id, a_name, a_admins, a_competitors, a_races);
    }

    public Boat(BoatDTO dto)
    {
        this(
                dto.getId(),
                dto.getName(),
                new HashSet<Long>(Arrays.asList(dto.getAdmins())),
                new HashSet<Long>(Arrays.asList(dto.getCompetitors())),
                new HashSet<Long>(Arrays.asList(dto.getRaces()))
        );
    }

    public BoatDTO toDTO()
    {
        BoatDTO dto = new BoatDTO(
                id,
                name,
                admins.toArray(new Long[admins.size()]),
                subordinates.toArray(new Long[subordinates.size()]),
                managers.toArray(new Long[managers.size()])
        );

        return dto;
    }

    public BoatInfo getBoatInfo()
    {
        return info;
    }
}