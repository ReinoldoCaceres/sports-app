package com.raintech.sportsapp.preferences;

import com.raintech.sportsapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

    @Query("SELECT p.user FROM Preference p WHERE CONCAT(p.campusSport.campusSportId, p.weekday, p.startTime, p.endTime) = :groupKey")
    List<User> findUsersByGroupKey(String groupKey);


}
