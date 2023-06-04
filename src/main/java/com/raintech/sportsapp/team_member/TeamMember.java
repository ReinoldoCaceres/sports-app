package com.raintech.sportsapp.team_member;

import com.raintech.sportsapp.team.Team;
import com.raintech.sportsapp.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;


@Entity
@Table(name = "Team_Member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"Team_ID", "User_ID"})
        })
@Getter
@Setter
@ToString
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

    // Other properties and relationships as needed

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
