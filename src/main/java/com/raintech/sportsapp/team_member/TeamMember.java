package com.raintech.sportsapp.team_member;

import com.raintech.sportsapp.team.Team;
import com.raintech.sportsapp.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Team_Member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"Team_ID", "User_ID"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Team_Member_ID")
    private int teamMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Team_ID")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID")
    private User user;

    // Other properties and relationships as needed

    @Override
    public int hashCode() {
        return Objects.hash(teamMemberId, team, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TeamMember other = (TeamMember) obj;
        return teamMemberId == other.teamMemberId &&
                Objects.equals(team, other.team) &&
                Objects.equals(user, other.user);
    }

}
