package com.raintech.sportsapp.university;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityRepository universityRepository;

    @GetMapping("/")
    public ResponseEntity<List<University>> getAllUniversities() {
        List<University> universities = universityRepository.findAll();
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/{universityId}")
    public ResponseEntity<University> getUniversityById(@PathVariable int universityId) {
        University university = universityRepository.findById(universityId)
                .orElse(null);
        if (university != null) {
            return ResponseEntity.ok(university);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<University> addUniversity(@RequestBody University university) {
        University createdUniversity = universityRepository.save(university);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUniversity);
    }

    @PutMapping("/{universityId}")
    public ResponseEntity<University> updateUniversity(@PathVariable int universityId, @RequestBody University university) {
        University existingUniversity = universityRepository.findById(universityId)
                .orElse(null);
        if (existingUniversity != null) {
            existingUniversity.setUniversityName(university.getUniversityName());
            University updatedUniversity = universityRepository.save(existingUniversity);
            return ResponseEntity.ok(updatedUniversity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{universityId}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable int universityId) {
        universityRepository.deleteById(universityId);
        return ResponseEntity.noContent().build();
    }
}
