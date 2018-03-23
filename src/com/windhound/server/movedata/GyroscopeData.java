package com.windhound.server.movedata;

public class GyroscopeData
{
    private Float dX;
    private Float dY;
    private Float dZ;

    public GyroscopeData(Float a_x, Float a_y, Float a_z)
    {
        dX = a_x;
        dY = a_y;
        dZ = a_z;
    }

    public Float getdX()
    {
        return dX;
    }

    public void setdX(Float dX)
    {
        this.dX = dX;
    }

    public Float getdY()
    {
        return dY;
    }

    public void setdY(Float dY)
    {
        this.dY = dY;
    }

    public Float getdZ()
    {
        return dZ;
    }

    public void setdZ(Float dZ)
    {
        this.dZ = dZ;
    }
}
