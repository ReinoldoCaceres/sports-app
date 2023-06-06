package com.raintech.sportsapp.user;

import com.raintech.sportsapp.campus.Campus;
import com.raintech.sportsapp.campus.CampusRepository;
import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.campus_sport.CampusSportRepository;
import com.raintech.sportsapp.config.JwtService;
import com.raintech.sportsapp.preferences.Preference;
import com.raintech.sportsapp.preferences.PreferenceRepository;
import com.raintech.sportsapp.preferences.PreferenceRequest;
import com.raintech.sportsapp.sports.Sport;
import com.raintech.sportsapp.sports.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CampusRepository campusRepository;
    private final CampusSportRepository campusSportRepository;
    private final SportRepository sportRepository;
    private final JwtService jwtService;
    private final PreferenceRepository preferenceRepository;

    public ResponseEntity<?> checkUserAuthorization(int userId, String username) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty() || !username.equals(optionalUser.get().getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to make modifications for this user ID.");
        }
        return null; // No unauthorized response
    }

    public ResponseEntity<?> addUserCampus(User user, String campusName) {
        Optional<Campus> optionalCampus = campusRepository.findByCampusName(campusName);
        if (optionalCampus.isPresent()) {
            Campus campus = optionalCampus.get();
            user.setCampus(campus);
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            String errorMessage = "Campus name not found: " + campusName;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    public String extractUsernameFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()); // Remove "Bearer " prefix
        return jwtService.extractUsername(token);
    }

    public Preference createPreference(User user, PreferenceRequest preferenceRequest) throws IllegalArgumentException {
        // Check if the user already has a preference with the same CampusSport, weekday, startTime, and endTime
        Optional<Preference> existingPreference = findExistingPreference(user, preferenceRequest);
        if (existingPreference.isPresent()) {
            throw new IllegalArgumentException("A preference with the same CampusSport, weekday, startTime, and endTime already exists for the user.");
        }

        Preference preference = buildPreference(user, preferenceRequest);
        return preferenceRepository.save(preference);
    }


    private Optional<Preference> findExistingPreference(User user, PreferenceRequest preferenceRequest) {
        // Find the sport based on the sport name in the preference request
        Optional<Sport> optionalSport = sportRepository.findBySportName(preferenceRequest.getSportsName());
        if (optionalSport.isPresent()) {
            // If the sport is found, retrieve it from the optional
            Sport sport = optionalSport.get();

            // Find the campus sport based on the found sport and the user's campus
            Optional<CampusSport> optionalCampusSport = campusSportRepository.findBySportAndCampus(sport, user.getCampus());
            if (optionalCampusSport.isPresent()) {
                // Query the preference repository to find a preference with the specified criteria
                return preferenceRepository.findByUserAndCampusSportAndWeekdayAndStartTimeAndEndTime(
                        user, optionalCampusSport, preferenceRequest.getWeekday(),
                        preferenceRequest.getStartTime(), preferenceRequest.getEndTime());
            }
        }
        // Return an empty optional if no existing preference is found
        return Optional.empty();
    }

    private Preference buildPreference(User user, PreferenceRequest preferenceRequest) {
        Preference preference = new Preference();
        preference.setUser(user);

        Optional<Sport> optionalSport = sportRepository.findBySportName(preferenceRequest.getSportsName());
        if (optionalSport.isEmpty()) {
            throw new IllegalArgumentException("Sport not found for sports name: " + preferenceRequest.getSportsName());
        }
        Sport sport = optionalSport.get();

        Optional<CampusSport> optionalCampusSport = campusSportRepository.findBySportAndCampus(sport, user.getCampus());
        if (optionalCampusSport.isEmpty()) {
            throw new IllegalArgumentException(preferenceRequest.getSportsName() + " Sport not found for your campus!");
        }
        CampusSport campusSport = optionalCampusSport.get();
        preference.setCampusSport(campusSport);
        preference.setWeekday(preferenceRequest.getWeekday());
        preference.setStartTime(preferenceRequest.getStartTime());
        preference.setEndTime(preferenceRequest.getEndTime());

        return preference;
    }
}
