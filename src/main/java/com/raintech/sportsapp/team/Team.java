package com.raintech.sportsapp.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.team_member.TeamMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Team")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //sequence 1 2 3 4 etc
    @Column(name = "Team_ID")
    private int teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Campus_Sport_ID")
    @ToString.Exclude
    private CampusSport campusSport;

    @Column(name = "Weekday")
    private String weekday;

    @Column(name = "Start_Time")
    private LocalTime startTime;

    @Column(name = "End_Time")
    private LocalTime endTime;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TeamMember> teamMembers = new HashSet<>();

    // Getters and Setters
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public CampusSport getCampusSport() {
        return campusSport;
    }

    public void setCampusSport(CampusSport campusSport) {
        this.campusSport = campusSport;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Set<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Set<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String getGroupKey() {
        return campusSport.getCampusSportId() +
                weekday +
                startTime.toString() +
                endTime.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null) {
            Hibernate.getClass(this);
            Hibernate.getClass(o);
        }
        return false;
    }

}
