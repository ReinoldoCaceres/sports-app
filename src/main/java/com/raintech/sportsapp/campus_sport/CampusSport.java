package com.raintech.sportsapp.campus_sport;

import com.raintech.sportsapp.sports.Sport;
import com.raintech.sportsapp.campus.Campus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Entity
@Table(name = "Campus_Sport")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampusSport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Campus_Sport_ID")
    private int campusSportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Campus_ID")
    private Campus campus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Sport_ID")
    private Sport sport;

    // Constructors, getters, and setters
}
