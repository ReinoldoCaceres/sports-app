package com.raintech.sportsapp.team;

import com.raintech.sportsapp.preferences.Preference;
import com.raintech.sportsapp.preferences.PreferenceRepository;
import com.raintech.sportsapp.team_member.TeamMember;
import com.raintech.sportsapp.team_member.TeamMemberRepository;
import com.raintech.sportsapp.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final PreferenceRepository preferenceRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    /**
     * Creates teams from preferences.
     */
    public void createTeamsFromPreferences() {
        // Retrieve all preferences from the database
        List<Preference> preferences = preferenceRepository.findAll();

        // Group preferences based on common attributes (campusSport, weekday, startTime, endTime)
        Map<String, Team> teamsMap = groupPreferencesByAttributes(preferences);

        // Retrieve users with matching preferences and add them to the corresponding teams
        addUsersToTeams(teamsMap);

        // Save the teams and their associations with users to the database
        saveTeamsWithUsers(teamsMap);
    }

    /**
     * Groups preferences by common attributes.
     *
     * @param preferences the list of preferences to group
     * @return a map of group keys to teams
     */
    private Map<String, Team> groupPreferencesByAttributes(List<Preference> preferences) {
        Map<String, Team> teamsMap = new HashMap<>();
        for (Preference preference : preferences) {
            String groupKey = getGroupKey(preference); // Create a key combining the common attributes

            Team team = teamsMap.get(groupKey);
            if (team == null) {
                team = createNewTeam(preference);
                teamsMap.put(groupKey, team);
            } else {
                System.out.println("Team with the same attributes already exists: " + groupKey);
            }
        }
        return teamsMap;
    }


    /**
     * Retrieves users with matching preferences and adds them to the corresponding teams.
     *
     * @param teamsMap the map of group keys to teams
     * @throws RuntimeException if no team members are added to any team
     */
    private void addUsersToTeams(Map<String, Team> teamsMap) {
        boolean teamMembersAdded = false; // Track if any team members were added

        for (Team team : teamsMap.values()) {
            List<User> users = preferenceRepository.findUsersByGroupKey(team.getGroupKey());

            if (!users.isEmpty()) {
                teamMembersAdded = true; // Set flag to true if any team members are added

                for (User user : users) {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setUser(user);
                    team.addMember(teamMember);
                    teamMemberRepository.save(teamMember);
                }
                teamRepository.save(team); // Save the team after adding the team members
            }
        }

        // If no team members were added, send an HTTP error response
        if (!teamMembersAdded) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add team members to the teams.");
        }
    }




    /**
     * Saves the teams and their associations with users to the database.
     *
     * @param teamsMap the map of group keys to teams
     */
    private void saveTeamsWithUsers(Map<String, Team> teamsMap) {
        teamRepository.saveAll(teamsMap.values());
    }

    /**
     * Creates a key combining the common attributes (campusSport, weekday, startTime, endTime).
     *
     * @param preference the preference to create the key from
     * @return the group key
     */
    private String getGroupKey(Preference preference) {
        return preference.getCampusSport().getCampusSportId() +
                preference.getWeekday() +
                preference.getStartTime() +
                preference.getEndTime();
    }

    /**
     * Creates a new team based on the preference.
     *
     * @param preference the preference to create the team from
     * @return the new team
     */
    private Team createNewTeam(Preference preference) {
        Team team = new Team();

        // Set the campus sport for the team
        team.setCampusSport(preference.getCampusSport());

        // Set the weekday for the team
        team.setWeekday(preference.getWeekday());

        // Set the start time and end time for the team
        team.setStartTime(preference.getStartTime());
        team.setEndTime(preference.getEndTime());

        return team;
    }

}
