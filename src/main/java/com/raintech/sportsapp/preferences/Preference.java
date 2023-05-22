package com.raintech.sportsapp.preferences;

import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


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

    @Column(name = "Time_Slot")
    private String timeSlot;

    // Constructors, getters, and setters
}
