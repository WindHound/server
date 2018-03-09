package com.windhound.server.movedata;

public class GyroscopeData
{
    private final float x;
    private final float y;
    private final float z;

    public GyroscopeData(float a_x, float a_y, float a_z)
    {
        x = a_x;
        y = a_y;
        z = a_z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
