package com.raintech.sportsapp.team_member;

import com.raintech.sportsapp.team.Team;
import com.raintech.sportsapp.team.TeamRepository;
import com.raintech.sportsapp.user.User;
import com.raintech.sportsapp.user.UserRepository;
import com.raintech.sportsapp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Team Members.
 */
@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    /**
     * Adds a user to a team.
     *
     * @param teamId The ID of the team.
     * @param userId The ID of the user.
     * @return The created TeamMember object representing the association between the user and the team.
     * @throws NotFoundException      If the team or user is not found.
     * @throws IllegalArgumentException If the user is already a member of the team.
     */
    public TeamMember addUserToTeam(int teamId, int userId) {
        // Find the team by ID or throw an exception if not found
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team not found"));

        // Find the user by ID or throw an exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check if the user is already a member of the team
        if (team.getTeamMembers().stream().anyMatch(member -> member.getUser().equals(user))) {
            throw new IllegalArgumentException("User is already a member of the team");
        }

        // Create a new TeamMember object
        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        teamMember.setUser(user);

        // Add the team member to the team and user
        team.getTeamMembers().add(teamMember);
        user.getTeamMembers().add(teamMember);

        // Save the team member
        teamMemberRepository.save(teamMember);

        return teamMember;
    }
}