package com.raintech.sportsapp.user;

import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.campus_sport.CampusSportRepository;
import com.raintech.sportsapp.preferences.Preference;
import com.raintech.sportsapp.preferences.PreferenceRepository;
import com.raintech.sportsapp.preferences.PreferenceRequest;
import com.raintech.sportsapp.sports.Sport;
import com.raintech.sportsapp.sports.SportRepository;
import com.raintech.sportsapp.campus.Campus;
import com.raintech.sportsapp.campus.CampusRepository;
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
    private final CampusRepository campusRepository;
    private final CampusSportRepository campusSportRepository;
    private final PreferenceRepository preferenceRepository;
    private final SportRepository sportRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @PostMapping("/{userId}/campus")
    public ResponseEntity<?> addUserCampus(@PathVariable int userId, @RequestBody String campusName) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Campus> optionalCampus = campusRepository.findByCampusName(campusName);
            if (optionalCampus.isPresent()) {
                Campus campus = optionalCampus.get();
                user.setCampus(campus);
                User updatedUser = userRepository.save(user);
                return ResponseEntity.ok(updatedUser);
            } else {
                String errorMessage = "Campus name not found: " + campusName;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage); // or return a different error response as needed
            }
        } else {
            String errorMessage = "User ID not found: " + userId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage); // or return a different error response as needed
        }
    }

    @PostMapping("/{userId}/preferences")
    public ResponseEntity<?> addUserPreference(@PathVariable int userId, @RequestBody PreferenceRequest preferenceRequest) {
        // Retrieve the user based on the provided userId
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Create a new Preference object based on the request
            Preference preference = new Preference();
            preference.setUser(user);

            // Find the CampusSport entity based on the sportsName and user's campus ID
            Optional<Sport> optionalSport = sportRepository.findBySportName(preferenceRequest.getSportsName());
            if (optionalSport.isPresent()) {
                Sport sport = optionalSport.get();

                // Check if the sport belongs to the user's campus
                Optional<CampusSport> optionalCampusSport = campusSportRepository.findBySportAndCampus(sport, user.getCampus());
                if (optionalCampusSport.isPresent()) {
                    CampusSport campusSport = optionalCampusSport.get();
                    preference.setCampusSport(campusSport);
                    preference.setWeekday(preferenceRequest.getWeekday());
                    preference.setTimeSlot(preferenceRequest.getTimeSlot());

                    // Save the preference in the database
                    Preference savedPreference = preferenceRepository.save(preference);

                    // Return a successful response with the saved preference
                    return ResponseEntity.ok(savedPreference);
                } else {
                    // Return a NOT_FOUND response if the CampusSport entity is not found for the specified sportsName and user's campus ID
                    String errorMessage = preferenceRequest.getSportsName() + " Sport not found for your campus! ";
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
                }
            } else {
                // Return a NOT_FOUND response if the Sport entity is not found for the specified sportsName
                String errorMessage = "Sport not found for sports name: " + preferenceRequest.getSportsName();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        } else {
            // Return a NOT_FOUND response if the user is not found for the specified userId
            String errorMessage = "User ID not found: " + userId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }









}
