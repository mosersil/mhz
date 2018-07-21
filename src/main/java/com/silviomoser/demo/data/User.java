package com.silviomoser.demo.data;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public User() {}

    public User(String username, String password) {
        this.username=username;
        this.password=password;
    }

    public User(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @CreatedDate
    @Type(type="java.sql.Timestamp")
    @Column(updatable = false)
    private Date createdDate;

    @CreatedDate
    @Type(type="java.sql.Timestamp")
    @Column(updatable = false)
    private Date lastModifiedDate;

    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (!username.equals(user.username)) return false;
        if (!password.equals(user.password)) return false;
        if (createdDate != null ? !createdDate.equals(user.createdDate) : user.createdDate != null) return false;
        return lastModifiedDate != null ? lastModifiedDate.equals(user.lastModifiedDate) : user.lastModifiedDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }

}
