package com.windhound.server.controller;

import com.windhound.server.DBManager;
import com.windhound.server.race.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StructureController
{
    @RequestMapping("/getBoat")
    public Boat getBoat(@RequestParam(value = "id", defaultValue = "0") Long id)
    {
        return DBManager.loadBoat(id);
    }
}