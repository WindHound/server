package com.windhound.server.movedata;

public class CompassData
{
    private Float angle;

    public CompassData(Float a_angle)
    {
        angle = a_angle;
    }

    public Float getAngle()
    {
        return angle;
    }

    public void setAngle(Float angle)
    {
        this.angle = angle;
    }
}
