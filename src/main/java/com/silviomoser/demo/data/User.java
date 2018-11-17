package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.utils.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


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
    @Type(type = "java.sql.Timestamp")
    @Column(name = "CREATED_DATE", updatable = false)
    private Date createdDate;

    @CreatedDate
    @Type(type = "java.sql.Timestamp")
    @Column(name = "LAST_MODIFIED_DATE", updatable = false)
    private Date lastModifiedDate;

    @JsonView(Views.Public.class)
    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON_ID")
    private Person person;


    @Column(name = "RESETTOKEN")
    @Size(max = 50)
    private String resetToken;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", roles=" + roles +
                ", person=" + FormatUtils.toFirstLastName(person) +
                '}';
    }



}
