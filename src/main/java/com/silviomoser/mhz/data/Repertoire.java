package com.silviomoser.mhz.data;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "REPERTOIRE")
public class Repertoire extends AbstractEntity{

    @JsonView(Views.Public.class)
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @JsonView(Views.Public.class)
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @JsonView(Views.Public.class)
    @Column(name = "DATE_START")
    private LocalDateTime dateStart;

    @NotNull
    @JsonView(Views.Public.class)
    @Column(name = "DATE_END")
    private LocalDateTime dateEnd;


    @JsonView(Views.Public.class)
    @JoinTable(name = "REPERTOIRE_COMPOSITION", joinColumns = {
            @JoinColumn(name = "REPERTOIRE_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "COMPOSITION_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Composition> compositions;

}
