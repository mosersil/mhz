package com.silviomoser.mhz.data;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "COMPOSITION")
public class Composition extends AbstractEntity {

    @JsonView(Views.Public.class)
    @Column(name = "Inventory", length = 20)
    @Size(max = 20)
    private String inventory;

    @JsonView(Views.Public.class)
    @JoinTable(name = "COMPOSITION_COMPOSER", joinColumns = {
            @JoinColumn(name = "COMPOSITION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "COMPOSER_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    private Set<Composer> composers;
    @JsonView(Views.Public.class)
    @JoinTable(name = "COMPOSITION_ARRANGER", joinColumns = {
            @JoinColumn(name = "COMPOSITION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "COMPOSER_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    private Set<Composer> arrangers;

    @JsonView(Views.Public.class)
    @Column(name = "TITLE")
    private String title;

    @JsonView(Views.Public.class)
    @Column(name = "SUBTITLE")
    private String subtitle;

    @JsonView(Views.Public.class)
    @Column(name = "GENRE")
    private String genre;

    @JsonView(Views.Public.class)
    @Column(name = "DESCRIPTION")
    private String description;

    @JsonView(Views.Public.class)
    @JoinTable(name = "COMPOSITION_SHEET", joinColumns = {
            @JoinColumn(name = "COMPOSITION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "SHEET_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    private Set<StaticFile> sheets;

    @JsonView(Views.Public.class)
    @JoinTable(name = "COMPOSITION_SAMPLE", joinColumns = {
            @JoinColumn(name = "COMPOSITION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "SAMPLE_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<StaticFile> samples;

}
