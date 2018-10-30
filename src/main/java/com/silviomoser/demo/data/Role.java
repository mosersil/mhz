package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.RoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@Table(name = "ROLE")
@EntityListeners(value = AuditingEntityListener.class)
public class Role extends AbstractEntity {

    @JsonView(Views.Public.class)
    @Enumerated(value = EnumType.STRING)
    @Column(name="ROLE_TYPE", nullable = false, unique = true)
    private RoleType type;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users;

}
