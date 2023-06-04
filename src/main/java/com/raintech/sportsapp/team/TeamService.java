package com.raintech.sportsapp.team;

import com.raintech.sportsapp.preferences.Preference;
import com.raintech.sportsapp.preferences.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final PreferenceRepository preferenceRepository;
    private final TeamRepository teamRepository;


    public void createTeamFromPreference(Preference preference) {
        int campusSportId = preference.getCampusSport().getCampusSportId();
        String weekday = preference.getWeekday();
        LocalTime startTime = preference.getStartTime();
        LocalTime endTime = preference.getEndTime();

        Team existingTeam = teamRepository.findByCampusSportIdAndWeekdayAndStartTimeAndEndTime(
                campusSportId, weekday, startTime, endTime);

        if (existingTeam != null) {
            // Team with the same fields already exists, return nothing
            return;
        }

        Team newTeam = new Team();
        newTeam.setCampusSport(preference.getCampusSport());
        newTeam.setWeekday(weekday);
        newTeam.setStartTime(startTime);
        newTeam.setEndTime(endTime);

        teamRepository.save(newTeam);
    }

}

