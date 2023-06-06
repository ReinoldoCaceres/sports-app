package com.raintech.sportsapp.user;

import com.raintech.sportsapp.preferences.Preference;
import com.raintech.sportsapp.preferences.PreferenceRepository;
import com.raintech.sportsapp.preferences.PreferenceRequest;
import com.raintech.sportsapp.team.TeamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PreferenceRepository preferenceRepository;
    private final UserService userService;
    private final TeamService teamService;


    /**
     * Retrieves a list of all users.
     *
     * @return A list of User objects representing all users.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    /**
     * Adds a campus to the user identified by the given userId.
     *
     * @param userId             The ID of the user to add the campus to.
     * @param campusName         The name of the campus to be added.
     * @param authorizationHeader The authorization header containing the user's token.
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @PostMapping("/{userId}/campus")
    public ResponseEntity<?> addUserCampus(@PathVariable int userId, @RequestBody String campusName, @RequestHeader("Authorization") String authorizationHeader) {

        String username = userService.extractUsernameFromToken(authorizationHeader);

        // Check user authorization
        ResponseEntity<?> authorizationResponse = userService.checkUserAuthorization(userId, username);
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        //If authorization is ok proceed
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return userService.addUserCampus(user, campusName);
        } else {
            String errorMessage = "User ID not found: " + userId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    /**
     * Adds a preference for the user identified by the given userId.
     *
     * @param userId              The ID of the user to add the preference to.
     * @param preferenceRequest   The preference request object containing the preference details.
     * @param authorizationHeader The authorization header containing the user's token.
     * @return A ResponseEntity with the appropriate status and response body.
     */
    @PostMapping("/{userId}/preferences")
    @Transactional
    public ResponseEntity<?> addUserPreference(@PathVariable int userId, @RequestBody PreferenceRequest preferenceRequest, @RequestHeader("Authorization") String authorizationHeader) {

        String username = userService.extractUsernameFromToken(authorizationHeader);

        // Check user authorization
        ResponseEntity<?> authorizationResponse = userService.checkUserAuthorization(userId, username);
        if (authorizationResponse != null) {
            return authorizationResponse;
        }

        // Retrieve the user
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found: " + userId);
        }
        User user = optionalUser.get();


        try {
            // Create a new preference
            Preference preference = userService.createPreference(user, preferenceRequest);

            // Save the preference in the database
            Preference savedPreference = preferenceRepository.save(preference);

            // Create team from preference
            teamService.createTeamFromPreference(savedPreference);

            // Return a successful response with the saved preference
            return ResponseEntity.ok(savedPreference);

        } catch (IllegalArgumentException e) {
            // Exception occurred due to a conflict in preferences
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Other exceptions occurred
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create preference: " + e.getMessage());
        }
    }

}
