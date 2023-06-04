package com.raintech.sportsapp.team;

import com.raintech.sportsapp.campus_sport.CampusSport;
import com.raintech.sportsapp.team_member.TeamMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Team")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ToString.Exclude
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null) {
            Hibernate.getClass(this);
            Hibernate.getClass(o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
