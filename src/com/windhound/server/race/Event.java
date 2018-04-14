package com.windhound.server.race;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

public class Event extends ManageableElement<Race, Championship>
{
    private Instant startDate;
    private Instant endDate;
    //private Long longitude, latitude;

    public Event(Long          a_id,
                 String        a_name,
                 Instant       a_startDate,
                 Instant       a_endDate,
                 HashSet<Long> a_admins,
                 HashSet<Long> a_races,
                 HashSet<Long> a_championships)
    {
        super(a_id, a_name, a_admins, a_races, a_championships);

        startDate = a_startDate;
        endDate   = a_endDate;
    }

    public Event(EventDTO dto)
    {
        this(
                dto.getId(),
                dto.getName(),
                Instant.ofEpochMilli(dto.getStartDate()),
                Instant.ofEpochMilli(dto.getEndDate()),
                new HashSet<Long>(Arrays.asList(dto.getAdmins())),
                new HashSet<Long>(Arrays.asList(dto.getRaces())),
                new HashSet<Long>(Arrays.asList(dto.getChampionships()))
        );
    }

    public EventDTO toDTO()
    {
        EventDTO dto = new EventDTO(
                id,
                name,
                startDate.toEpochMilli(),
                endDate.toEpochMilli(),
                admins.toArray(new Long[admins.size()]),
                subordinates.toArray(new Long[subordinates.size()]),
                managers.toArray(new Long[managers.size()])
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