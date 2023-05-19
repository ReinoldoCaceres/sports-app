package com.raintech.sportsapp.user;

import com.raintech.sportsapp.university.Campus;
import com.raintech.sportsapp.university.CampusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{userId}/campus")
    public ResponseEntity<User> addUserCampus(@PathVariable int userId, @RequestBody Campus campus) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setCampus(campus);
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
