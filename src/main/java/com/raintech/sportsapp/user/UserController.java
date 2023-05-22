package com.raintech.sportsapp.user;

import com.raintech.sportsapp.university.Campus;
import com.raintech.sportsapp.university.CampusRepository;
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

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @PostMapping("/{userId}/campus")
//    public ResponseEntity<User> addUserCampus(@PathVariable int userId, @RequestBody Campus campus) {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user != null) {
//            user.setCampus(campus);
//            User updatedUser = userRepository.save(user);
//            return ResponseEntity.ok(updatedUser);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

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



}
