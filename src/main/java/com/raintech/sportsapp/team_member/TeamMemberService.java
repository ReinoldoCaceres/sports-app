package com.raintech.sportsapp.team_member;

import com.raintech.sportsapp.team.Team;
import com.raintech.sportsapp.team.TeamRepository;
import com.raintech.sportsapp.user.User;
import com.raintech.sportsapp.user.UserRepository;
import com.raintech.sportsapp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;


    public TeamMember addUserToTeam(int teamId, int userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check if the user is already a member of the team
        if (team.getTeamMembers().stream().anyMatch(member -> member.getUser().equals(user))) {
            throw new IllegalArgumentException("User is already a member of the team");
        }

        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        teamMember.setUser(user);

        team.getTeamMembers().add(teamMember);
        user.getTeamMembers().add(teamMember);

        teamMemberRepository.save(teamMember);

        return(teamMember);


    }
}