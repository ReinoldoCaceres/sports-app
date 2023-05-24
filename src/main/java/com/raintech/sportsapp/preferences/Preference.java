package com.raintech.sportsapp.preferences;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.config.LocalTimeSerializer;
import com.raintech.sportsapp.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "Preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Preference_ID")
    private int preferenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Campus_Sport_ID")
    private CampusSport campusSport;

    @Column(name = "Weekday")
    private String weekday;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "Start_Time")
    private LocalTime startTime;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "End_Time")
    private LocalTime endTime;

}
