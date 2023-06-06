package com.raintech.sportsapp.team_member;

import com.raintech.sportsapp.team.Team;
import com.raintech.sportsapp.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Team_Member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"Team_ID", "User_ID"})
        })
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Team_Member_ID")
    private int teamMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Team_ID")
    @ToString.Exclude
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    @ToString.Exclude
    private User user;

    // Getters and Setters
    public int getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(int teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

