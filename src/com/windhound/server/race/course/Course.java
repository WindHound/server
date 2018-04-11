package com.windhound.server.race.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windhound.server.movedata.GPSData;
import java.util.List;

public class Course
{
    private Long       id;
    private int        laps;
    private GPSData    startLine;
    private GPSData    finishLine;
    private List<Buoy> buoys;
    //private List<GPSData> buoys;

    public Course()
    {

    }

    public Course(Long       a_id,
                  int        a_laps,
                  GPSData    a_startLine,
                  GPSData    a_finishLine,
                  List<Buoy> a_buoys)
                  //List<GPSData> a_buoys)
    {
        id         = a_id;
        laps       = a_laps;
        startLine  = a_startLine;
        finishLine = a_finishLine;
        buoys      = a_buoys;
    }

    public Course(String JSON)
    {
        ObjectMapper mapper = new ObjectMapper();
        Course course;

        try
        {
            course = mapper.readValue(JSON, Course.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            course = null;
            System.exit(1);
        }

        id         = course.getId();
        laps       = course.getLaps();
        startLine  = course.getStartLine();
        finishLine = course.getFinishLine();
        buoys      = course.getBuoys();
    }

    public String toJSON()
    {
        ObjectMapper mapper = new ObjectMapper();
        String JSON;

        try
        {
            JSON = mapper.writeValueAsString(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JSON = null;
            System.exit(1);
        }

        return JSON;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public int getLaps()
    {
        return laps;
    }

    public void setLaps(int laps)
    {
        this.laps = laps;
    }

    public GPSData getStartLine()
    {
        return startLine;
    }

    public void setStartLine(GPSData startLine)
    {
        this.startLine = startLine;
    }

    public GPSData getFinishLine()
    {
        return finishLine;
    }

    public void setFinishLine(GPSData finishLine)
    {
        this.finishLine = finishLine;
    }

    //*
    public List<Buoy> getBuoys()
    {
        return buoys;
    }

    public void setBuoys(List<Buoy> buoys)
    {
        this.buoys = buoys;
    }
    //*/
    /*
    public List<GPSData> getBuoys()
    {
        return buoys;
    }

    public void setBuoys(List<GPSData> buoys)
    {
        this.buoys = buoys;
    }
    //*/
}
