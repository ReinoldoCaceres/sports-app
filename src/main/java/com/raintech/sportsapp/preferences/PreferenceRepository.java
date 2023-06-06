package com.raintech.sportsapp.preferences;

import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

    @Query("SELECT u FROM Preference p JOIN User u ON p.user.userId = u.userId WHERE CONCAT(p.campusSport.campusSportId, p.weekday, p.startTime, p.endTime) = :groupKey")
    List<User> findUsersByGroupKey(String groupKey);

    Optional<Preference> findByUserAndCampusSportAndWeekdayAndStartTimeAndEndTime(User user, Optional<CampusSport> campusSport, String weekday, LocalTime startTime, LocalTime endTime);

}
