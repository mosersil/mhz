package com.silviomoser.mhz.data;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by silvio on 19.07.18.
 */

@Entity
@Getter
@Setter
@ToString
@Table(name = "MEMBERSHIP")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership extends AbstractEntity {

    @ToString.Exclude
    @JsonView({Views.Public.class, Views.Internal.class})
    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    @NotNull
    private Person person;

    @ManyToOne
    @JoinColumn(name = "ORGANIZATION_ID")
    @NotNull
    private Organization organization;

    @Column(name = "JOIN_DATE", nullable = false)
    @NotNull
    private LocalDate joinDate;

    @Column(name = "LEAVE_DATE")
    private LocalDate leaveDate;

    @JsonView({Views.Public.class, Views.Internal.class})
    @Column(name = "FUNCTION")
    private String function;

    @Column(name = "REMARKS")
    private String remarks;

}
