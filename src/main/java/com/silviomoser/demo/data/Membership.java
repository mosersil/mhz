package com.silviomoser.demo.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by silvio on 19.07.18.
 */

@Entity
@Getter
@Setter
@ToString
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

}
