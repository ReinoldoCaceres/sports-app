package com.raintech.sportsapp.sports;

import com.raintech.sportsapp.campus_sport.CampusSport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {

    Optional<Sport> findBySportName(String sportName);
}