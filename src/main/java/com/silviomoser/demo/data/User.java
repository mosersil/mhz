package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.utils.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "USER")
@Getter
@Setter
@EntityListeners(value = AuditingEntityListener.class)
public class User extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonView(Views.Public.class)
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    @ToString.Exclude
    private String password;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @CreatedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    @JsonView(Views.Public.class)
    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @Column(name = "RESETTOKEN")
    @Size(max = 50)
    private String resetToken;

    @Column(name = "ACTIVE")
    private boolean active;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", roles=" + roles +
                ", person=" + FormatUtils.toFirstLastName(person) +
                '}';
    }

    public void addRole(Role role) {
        if (getRoles()==null) {
            setRoles(new HashSet<>(1));
        }
        getRoles().add(role);
    }

}
