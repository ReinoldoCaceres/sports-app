package com.raintech.sportsapp.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    @Query("SELECT t FROM Team t WHERE t.campusSport.campusSportId = :campusSportId " +
            "AND t.weekday = :weekday " +
            "AND t.startTime = :startTime " +
            "AND t.endTime = :endTime")

    Team findByCampusSportIdAndWeekdayAndStartTimeAndEndTime(
            @Param("campusSportId") int campusSportId,
            @Param("weekday") String weekday,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}