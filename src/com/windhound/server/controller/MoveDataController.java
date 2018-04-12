package com.windhound.server.controller;

import com.windhound.server.database.DBManager;
import com.windhound.server.movedata.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class MoveDataController
{
    @CrossOrigin
    @PostMapping("/movedata/add")
    public Long addMoveData(@RequestBody MoveDataDTO dto)
    {
        MoveData moveData = new MoveData(dto);

        Long moveDataID = DBManager.saveMoveData(moveData);

        return moveDataID;
    }

    @RequestMapping("/movedata/get/{raceID}/{boatID}")
    public MoveDataDTO[] getMoveData(@PathVariable Long raceID,
                                     @PathVariable Long boatID)
    {
        MoveData[] moveDataPoints = DBManager.loadMoveData(raceID, boatID);

        MoveDataDTO[] moveDataDTOs = new MoveDataDTO[moveDataPoints.length];
        for (int i = 0; i < moveDataPoints.length; ++i)
            moveDataDTOs[i] = moveDataPoints[i].toDTO();

        return moveDataDTOs;
    }
}