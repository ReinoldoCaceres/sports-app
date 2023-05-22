package com.raintech.sportsapp.campus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raintech.sportsapp.university.University;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "Campus")
@Data
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Campus_ID")
    private Integer campusId;

    @Column(name = "Campus_Name")
    private String campusName;

    @Column(name = "University_ID")
    private Integer universityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "University_ID", referencedColumnName = "University_ID", insertable = false, updatable = false)
    @JsonIgnore
    private University university;
}