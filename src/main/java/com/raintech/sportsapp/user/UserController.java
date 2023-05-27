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


    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


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


    @PostMapping("/{userId}/preferences")
    @Transactional // Add the @Transactional annotation
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

        // Create a new preference
        Preference preference = userService.createPreference(user, preferenceRequest);

        // Trigger team creation process
        try {
            teamService.createTeamsFromPreferences();
        } catch (Exception e) {
            // Rollback the transaction if an exception occurs
            // You can handle the exception and log the error if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create teams from preferences.");
        }

        // Save the preference in the database
        Preference savedPreference = preferenceRepository.save(preference);

        // Return a successful response with the saved preference
        return ResponseEntity.ok(savedPreference);
    }


}
