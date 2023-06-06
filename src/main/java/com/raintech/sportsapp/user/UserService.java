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

/**
 * Service class for managing User-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CampusRepository campusRepository;
    private final CampusSportRepository campusSportRepository;
    private final SportRepository sportRepository;
    private final JwtService jwtService;
    private final PreferenceRepository preferenceRepository;

    /**
     * Checks user authorization based on the user ID and username.
     *
     * @param userId   The ID of the user.
     * @param username The username of the user.
     * @return ResponseEntity with an error response if the user is not authorized, or null if authorized.
     */
    public ResponseEntity<?> checkUserAuthorization(int userId, String username) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty() || !username.equals(optionalUser.get().getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to make modifications for this user ID.");
        }
        return null; // No unauthorized response
    }

    /**
     * Adds a campus to the user.
     *
     * @param user        The user to add the campus to.
     * @param campusName  The name of the campus.
     * @return ResponseEntity with the updated User object if the campus is found, or an error response if not found.
     */
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

    /**
     * Extracts the username from the authorization token.
     *
     * @param authorizationHeader The authorization header containing the token.
     * @return The extracted username.
     */
    public String extractUsernameFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()); // Remove "Bearer " prefix
        return jwtService.extractUsername(token);
    }

    /**
     * Creates a preference for the user.
     *
     * @param user              The user to create the preference for.
     * @param preferenceRequest The preference request object containing the preference details.
     * @return The created Preference object if successful.
     * @throws IllegalArgumentException If a preference with the same criteria already exists or if sport or campus sport is not found.
     */
    public Preference createPreference(User user, PreferenceRequest preferenceRequest) throws IllegalArgumentException {
        // Check if the user already has a preference with the same criteria
        Optional<Preference> existingPreference = findExistingPreference(user, preferenceRequest);
        if (existingPreference.isPresent()) {
            throw new IllegalArgumentException("A preference with the same CampusSport, weekday, startTime, and endTime already exists for the user.");
        }

        Preference preference = buildPreference(user, preferenceRequest);
        return preferenceRepository.save(preference);
    }

    /**
     * Finds an existing preference for the user based on the provided preference criteria.
     *
     * @param user              The user to search for existing preferences.
     * @param preferenceRequest The preference request object containing the preference criteria.
     * @return An Optional containing the existing preference if found, or an empty Optional if not found.
     */
    private Optional<Preference> findExistingPreference(User user, PreferenceRequest preferenceRequest) {
        Optional<Sport> optionalSport = sportRepository.findBySportName(preferenceRequest.getSportsName());
        if (optionalSport.isPresent()) {
            Sport sport = optionalSport.get();
            Optional<CampusSport> optionalCampusSport = campusSportRepository.findBySportAndCampus(sport, user.getCampus());
            if (optionalCampusSport.isPresent()) {
                return preferenceRepository.findByUserAndCampusSportAndWeekdayAndStartTimeAndEndTime(
                        user, optionalCampusSport, preferenceRequest.getWeekday(),
                        preferenceRequest.getStartTime(), preferenceRequest.getEndTime());
            }
        }
        return Optional.empty();
    }

    /**
     * Builds a new Preference object based on the user and preference request.
     *
     * @param user              The user for whom the preference is being created.
     * @param preferenceRequest The preference request object containing the preference details.
     * @return The created Preference object.
     * @throws IllegalArgumentException If the sport or campus sport is not found for the user.
     */
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

