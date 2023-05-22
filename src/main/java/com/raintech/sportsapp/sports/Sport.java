package com.raintech.sportsapp.sports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "Sports")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Sport_ID")
    private int sportId;

    @Column(name = "Sport_Name")
    private String sportName;

    // Constructors, getters, and setters
}
