package com.abdydidit.automation.controller;


import com.abdydidit.automation.service.Initiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Lobby {

    @Autowired
    Initiator initiator;

    @GetMapping("/executeBatch")
    public String executeBatch() {
        // Replace with your actual server details and batch command
        String host = "10.1.125.58";
        String username = "localdevuser";
        String password = "Coop#4321";
        String command = "sh app.sh";

        initiator.executeBatch(host,command);

        return "Batch executed";
    }
}
