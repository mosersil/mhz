package com.silviomoser.demo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity(name = "PERSON_VERIFICATION")
public class PersonVerification extends AbstractEntity {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @NotNull
    @Column(name = "TOKEN")
    private String token;

    @NotNull
    @Column(name = "CHALLENGE_DATE")
    private LocalDateTime challengeIssued;

    @Column(name = "CONFIRMED_DATE")
    private LocalDateTime confirmedDate;

}
