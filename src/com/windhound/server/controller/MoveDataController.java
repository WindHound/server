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
/*
    @CrossOrigin
    @PostMapping("/movedata/add2/")
    public String addMoveData2(@RequestParam(value = "competitorID")       Long competitorID,
                                  @RequestParam(value = "boatID")             Long boatID,
                                  @RequestParam(value = "raceID")             Long raceID,
                                  @RequestParam(value = "time")               Long timeMilli,
                                                                              GPSData gpsData)
                                  //@RequestParam(value = "sensorDataPoints") List<SensorData> sensorDataPoints)
    {
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        time.setTimeInMillis(timeMilli);

        MoveData moveData = new MoveData(
                competitorID,
                boatID,
                raceID,
                time,
                gpsData,
                null//sensorDataPoints
        );

        temp = moveData;

        return "accept";
    }

    @RequestMapping("/movedata/get2/")
    public MoveData getMoveData2()
    {
        return temp;
    }*/
}

/*
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
<script>
		$.ajax({
                url: 'http://localhost:8080/movedata/add/',
                type: 'post',
                data:  {"competitorID":1,"boatID":2,"raceID":3,"timeMilli":1514805000000,"latitude":34.4642,"longitude":60.34314,"x":[1.0]},
                success: function( data ){
                //console.log("text");
                var text = document.getElementById('text');
                text.innerHTML = data;
                },
                error: function( errorThrown ){
                console.log( errorThrown );
                }
                });
</script>
<div id="text"></div>
</body>
</html>
*/



/*
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
<script>
		$.ajax({
                url: 'http://localhost:8080/movedata/add/',
                type: 'post',
                data:  {"competitorID":1,"boatID":2,"raceID":3,"time":1514805000000,"gpsData":{"latitude":10.134,"longitude":60.67},"sensorDataPoints":[{"accelerometerData":null,"gyroscopeData":null,"compassData":null},{"accelerometerData":null,"gyroscopeData":null,"compassData":null}]} ,
                success: function( data ){
                //console.log("text");
                var text = document.getElementById('text');
                text.innerHTML = data;
                },
                error: function( errorThrown ){
                console.log( errorThrown );
                }
                });
</script>
<div id="text"></div>
</body>
</html>
*/