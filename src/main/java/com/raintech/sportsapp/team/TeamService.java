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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final PreferenceRepository preferenceRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    // ...

    public void createTeamsFromPreferences() {
        // Retrieve all preferences from the database
        List<Preference> preferences = preferenceRepository.findAll();

        // Group preferences based on common attributes (Campus_Sport_ID, Weekday, Start_Time, End_Time)
        List<Team> teams = groupPreferencesByAttributes(preferences);

        // Retrieve users with matching preferences and add them to the corresponding teams
        addUsersToTeams(teams);

        // Save the teams and their associations with users to the database
        saveTeamsWithUsers(teams);
    }

    private List<Team> groupPreferencesByAttributes(List<Preference> preferences) {
        List<Team> teams = new ArrayList<>();

        for (Preference preference : preferences) {
            String groupKey = getGroupKey(preference); // Create a key combining the common attributes

            Team existingTeam = findTeamByGroupKey(teams, groupKey);
            if (existingTeam == null) {
                Team newTeam = createNewTeam(preference);
                teams.add(newTeam);
            } else {
                System.out.println("Team with the same attributes already exists: " + groupKey);
            }
        }

        return teams;
    }

    private Team findTeamByGroupKey(List<Team> teams, String groupKey) {
        for (Team team : teams) {
            if (team.getGroupKey().equals(groupKey)) {
                return team;
            }
        }
        return null;
    }

    private void addUsersToTeams(List<Team> teams) {
        boolean teamMembersAdded = false;

        for (Team team : teams) {
            String groupKey = team.getGroupKey();

            List<User> users = preferenceRepository.findUsersByGroupKey(groupKey);
            if (!users.isEmpty()) {
                teamRepository.save(team); // Save the team first

                teamMembersAdded = true;
                for (User user : users) {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setUser(user);
                    teamMember.setTeam(team); // Associate the team with the team member
                    teamMemberRepository.save(teamMember);
                }
            }
        }

        if (!teamMembersAdded) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add team members to the teams.");
        }
    }


    private void saveTeamsWithUsers(List<Team> teams) {
        teamRepository.saveAll(teams);
    }

    private String getGroupKey(Preference preference) {
        return preference.getCampusSport().getCampusSportId() +
                preference.getWeekday() +
                preference.getStartTime() +
                preference.getEndTime();
    }

    private Team createNewTeam(Preference preference) {
        Team team = new Team();

        team.setCampusSport(preference.getCampusSport());
        team.setWeekday(preference.getWeekday());
        team.setStartTime(preference.getStartTime());
        team.setEndTime(preference.getEndTime());

        return team;
    }
}

