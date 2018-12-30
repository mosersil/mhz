package com.silviomoser.demo.data;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    @ToString.Exclude
    @JsonView({Views.Public.class, Views.Internal.class})
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

    @JsonView({Views.Public.class, Views.Internal.class})
    @Column(name = "FUNCTION")
    private String function;

}
