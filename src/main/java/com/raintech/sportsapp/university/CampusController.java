package com.raintech.sportsapp.university;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campuses")
@RequiredArgsConstructor
public class CampusController {

    private final CampusRepository campusRepository;

    @GetMapping("/")
    public ResponseEntity<List<Campus>> getAllCampuses() {
        List<Campus> campuses = campusRepository.findAll();
        return ResponseEntity.ok(campuses);
    }

    @GetMapping("/{campusId}")
    public ResponseEntity<Campus> getCampusById(@PathVariable int campusId) {
        Campus campus = campusRepository.findById(campusId)
                .orElse(null);
        if (campus != null) {
            return ResponseEntity.ok(campus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Campus> addCampus(@RequestBody Campus campus) {
        Campus createdCampus = campusRepository.save(campus);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampus);
    }

    @PutMapping("/{campusId}")
    public ResponseEntity<Campus> updateCampus(@PathVariable int campusId, @RequestBody Campus campus) {
        Campus existingCampus = campusRepository.findById(campusId)
                .orElse(null);
        if (existingCampus != null) {
            existingCampus.setCampusName(campus.getCampusName());
            Campus updatedCampus = campusRepository.save(existingCampus);
            return ResponseEntity.ok(updatedCampus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{campusId}")
    public ResponseEntity<Void> deleteCampus(@PathVariable int campusId) {
        campusRepository.deleteById(campusId);
        return ResponseEntity.noContent().build();
    }
}
