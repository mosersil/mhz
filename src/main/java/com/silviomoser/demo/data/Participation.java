package com.silviomoser.demo.data;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PARTICIPATION")
public class Participation extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private CalendarEvent event;

    @Column(name = "REMARKS")
    private String remarks;
}
