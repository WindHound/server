package com.windhound.server.movedata;

public class AccelerometerData
{
    private Float x;
    private Float y;
    private Float z;

    public AccelerometerData(Float a_x, Float a_y, Float a_z)
    {
        x = a_x;
        y = a_y;
        z = a_z;
    }

    public Float getX()
    {
        return x;
    }

    public void setX(Float x)
    {
        this.x = x;
    }

    public Float getY()
    {
        return y;
    }

    public void setY(Float y)
    {
        this.y = y;
    }

    public Float getZ()
    {
        return z;
    }

    public void setZ(Float z)
    {
        this.z = z;
    }
}
