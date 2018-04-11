package com.windhound.server.race.course;

import com.windhound.server.movedata.GPSData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class Course
{
    Long       id;
    int        laps;
    GPSData    startLine;
    GPSData    finishLine;
    List<Buoy> buoys;

    public Course(Long       a_id,
                  int        a_laps,
                  GPSData    a_startLine,
                  GPSData    a_finishLine,
                  List<Buoy> a_buoys)
    {
        id         = a_id;
        laps       = a_laps;
        startLine  = a_startLine;
        finishLine = a_finishLine;
        buoys      = a_buoys;
    }

    public Course(String JSON)
    {
        throw new NotImplementedException();
    }

    public String toJSON()
    {
        throw new NotImplementedException();
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

    public List<Buoy> getBuoys()
    {
        return buoys;
    }

    public void setBuoys(List<Buoy> buoys)
    {
        this.buoys = buoys;
    }
}
