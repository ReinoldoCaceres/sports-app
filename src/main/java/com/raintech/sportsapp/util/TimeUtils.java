package com.raintech.sportsapp.util;

import java.time.LocalTime;

public class TimeUtils {

    public static LocalTime getTimeFromString(String time, boolean isStartTime) {
        String[] parts = time.split(" - ");
        return isStartTime ? LocalTime.parse(parts[0]) : LocalTime.parse(parts[1]);
    }
}
