package com.raintech.sportsapp.campus_sport;

import com.raintech.sportsapp.sports.Sport;
import com.raintech.sportsapp.campus.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CampusSportRepository extends JpaRepository<CampusSport, Integer> {
    Optional<CampusSport> findByCampusSportId(int campusSportId);

    Optional<CampusSport> findBySportAndCampus(Sport sport, Campus campus);

}