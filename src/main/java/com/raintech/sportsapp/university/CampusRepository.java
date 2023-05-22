package com.raintech.sportsapp.university;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampusRepository extends JpaRepository<Campus, Integer> {

    Optional<Campus> findByCampusName(String campusName);

}
