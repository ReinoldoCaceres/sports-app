package com.raintech.sportsapp.team_member;

import com.raintech.sportsapp.team.TeamRepository;
import com.raintech.sportsapp.user.User;
import com.raintech.sportsapp.user.UserRepository;
import com.raintech.sportsapp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/TeamMember")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberService teamMemberService;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/AddTeamMember")
    public ResponseEntity<?> addTeamMember(@RequestBody String teamID, @RequestHeader("Authorization") String authorizationHeader) {
        String username = userService.extractUsernameFromToken(authorizationHeader);
        Optional<User> user = userRepository.findByUsername(username);
        int userID = -1;

        if (user.isPresent()) {
            userID = user.get().getUserId();
        }

        // Check user authorization
        ResponseEntity<?> authorizationResponse = userService.checkUserAuthorization(userID, username);
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        // If authorization is ok, proceed
        try {
            TeamMember savedTeamMember = teamMemberService.addUserToTeam(Integer.parseInt(teamID), userID);
            return ResponseEntity.ok(savedTeamMember);
        } catch (IllegalArgumentException e) {
            // Exception occurred due to a conflict in Team Member
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Other exceptions occurred
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Team Member: " + e.getMessage());
        } catch (StackOverflowError e) {
            // Handle StackOverflowError
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("StackOverflowError occurred: " + e.getMessage());
        }
    }
}