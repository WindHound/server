package com.windhound.server.race;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Championship extends ManageableElement<Event, Championship>
{
    private Instant startDate;
    private Instant endDate;

    public Championship(Long          a_id,
                        String        a_name,
                        Instant       a_startDate,
                        Instant       a_endDate,
                        HashSet<Long> a_admins,
                        HashSet<Long> a_events)
    {
        super(a_id, a_name, a_admins, a_events, new HashSet<>());

        startDate = a_startDate;
        endDate   = a_endDate;
    }

    public Championship(ChampionshipDTO dto)
    {
        this(
                dto.getId(),
                dto.getName(),
                Instant.ofEpochMilli(dto.getStartDate()),
                Instant.ofEpochMilli(dto.getEndDate()),
                new HashSet<Long>(Arrays.asList(dto.getAdmins())),
                new HashSet<Long>(Arrays.asList(dto.getEvents()))
        );
    }

    public ChampionshipDTO toDTO()
    {
        ChampionshipDTO dto = new ChampionshipDTO(
                id,
                name,
                startDate.toEpochMilli(),
                endDate.toEpochMilli(),
                admins.toArray(new Long[admins.size()]),
                subordinates.toArray(new Long[subordinates.size()])
        );

        return dto;
    }

    public Instant getStartDate()
    {
        return startDate;
    }

    public Instant getEndDate()
    {
        return endDate;
    }
}