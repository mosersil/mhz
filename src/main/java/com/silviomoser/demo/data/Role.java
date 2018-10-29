package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.type.RoleType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;


@Entity
@Table(name = "ROLE")
@EntityListeners(value = AuditingEntityListener.class)
public class Role extends AbstractEntity {

    public Role() {
    }

    @JsonView(Views.Public.class)
    @Enumerated(value = EnumType.STRING)
    @Column(name="ROLE_TYPE", nullable = false, unique = true)
    private RoleType type;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users;


    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
