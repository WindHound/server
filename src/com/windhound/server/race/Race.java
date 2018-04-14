package com.windhound.server.race;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

public class Race extends ManageableElement<Boat, Event>
{
    private Instant startDate;
    private Instant endDate;
    //private ArrayList<Courses/Classes> courses;

    public Race(Long          a_id,
                String        a_name,
                Instant       a_startDate,
                Instant       a_endDate,
                HashSet<Long> a_admins,
                HashSet<Long> a_boats,
                HashSet<Long> a_events)
    {
        super(a_id, a_name, a_admins, a_boats, a_events);

        startDate = a_startDate;
        endDate   = a_endDate;
    }

    public Race(RaceDTO dto)
    {
        this(
                dto.getId(),
                dto.getName(),
                Instant.ofEpochMilli(dto.getStartDate()),
                Instant.ofEpochMilli(dto.getEndDate()),
                new HashSet<Long>(Arrays.asList(dto.getAdmins())),
                new HashSet<Long>(Arrays.asList(dto.getBoats())),
                new HashSet<Long>(Arrays.asList(dto.getEvents()))
        );
    }

    public RaceDTO toDTO()
    {
        RaceDTO dto = new RaceDTO(
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