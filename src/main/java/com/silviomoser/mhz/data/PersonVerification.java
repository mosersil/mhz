package com.silviomoser.mhz.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PERSON_VERIFICATION")
public class PersonVerification extends AbstractEntity {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @NotNull
    @Column(name = "TOKEN", length = 40)
    private String token;

    @NotNull
    @Column(name = "CHALLENGE_DATE")
    private LocalDateTime challengeIssued;

    @Column(name = "CONFIRMED_DATE")
    private LocalDateTime confirmedDate;

}
