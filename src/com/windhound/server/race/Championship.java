package com.windhound.server.race;

public class Championship extends Structure
{
    private Championship(Long a_id)//, String a_name)
    {
        super(a_id);//, a_name);
    }

    public static Championship getInstance(Long a_id)//, String a_name)
    {
        Championship championship = StructureManager.getChampionship(a_id);
        if (championship == null)
        {
            championship = new Championship(a_id);//, a_name);
            StructureManager.addChampionship(championship);
        }

        return championship;
    }
}