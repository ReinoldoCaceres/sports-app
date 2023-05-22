package com.raintech.sportsapp.preferences;

import lombok.Data;

@Data
public class PreferenceRequest {

    private String sportsName;
    private String weekday;
    private String timeSlot;

    // Constructors, getters, and setters
}