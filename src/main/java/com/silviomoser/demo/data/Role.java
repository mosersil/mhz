package com.silviomoser.demo.data;

import com.silviomoser.demo.data.type.RoleType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class Role extends AbstractEntity {

    public Role() {
    }

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name="ROLE_TYPE", nullable = false, unique = true)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
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
