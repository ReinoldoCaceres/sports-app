package com.raintech.sportsapp.preferences;

import lombok.Data;

import java.time.LocalTime;

@Data
public class PreferenceRequest {

    private String sportsName;
    private String weekday;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructors, getters, and setters
}
