package com.raintech.sportsapp.university;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "University")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "University_ID")
    private int universityId;

    @Column(name = "University_Name")
    private String universityName;

}
