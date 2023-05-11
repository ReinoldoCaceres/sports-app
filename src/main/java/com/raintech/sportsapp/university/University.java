package com.raintech.sportsapp.university;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int universityId;

    private String universityName;

}
