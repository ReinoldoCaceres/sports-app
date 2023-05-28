package com.raintech.sportsapp.team;

import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.team_member.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Team")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Team_ID")
    private int teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Campus_Sport_ID")
    private CampusSport campusSport;

    @Column(name = "Weekday")
    private String weekday;

    @Column(name = "Start_Time")
    private LocalTime startTime;

    @Column(name = "End_Time")
    private LocalTime endTime;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<TeamMember> teamMembers = new HashSet<>();

    public void addMember(TeamMember teamMember) {
        // Check if the user is already a member of the team
        boolean isExistingMember = teamMembers.stream()
                .anyMatch(member -> member.getUser().equals(teamMember.getUser()));

        if (isExistingMember) {
            throw new IllegalArgumentException("User is already a member of the team.");
        }

        teamMembers.add(teamMember);
        teamMember.setTeam(this);
    }


    public void removeMember(TeamMember teamMember) {
        teamMembers.remove(teamMember);
        teamMember.setTeam(null);
    }

    public String getGroupKey() {
        String campusSportId = String.valueOf(campusSport.getCampusSportId());
        String weekday = this.weekday;
        String startTime = this.startTime.toString() + ":00"; // Include seconds in the string representation
        String endTime = this.endTime.toString() + ":00"; // Include seconds in the string representation

        return campusSportId + weekday + startTime + endTime;
    }



}
