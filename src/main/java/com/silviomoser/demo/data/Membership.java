package com.silviomoser.demo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by silvio on 19.07.18.
 */

@Entity
@Table(name = "MEMBERSHIP")
public class Membership extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "ORGANIZATION_ID")
    private Organization organization;

    @Column(name = "JOIN_DATE")
    private LocalDateTime joinDate;

    @Column(name = "LEAVE_DATE")
    private LocalDateTime leaveDate;

    @Column(name = "FUNCTION")
    private String function;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
