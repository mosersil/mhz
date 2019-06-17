package com.silviomoser.demo.data;

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

    @Column(name = "TAG")
    private String tag;

    @JsonView(Views.Public.class)
    @JoinTable(name = "COMPOSITION_COMPOSER", joinColumns = {
            @JoinColumn(name = "COMPOSITION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "COMPOSER_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Composer> composers;
    @JsonView(Views.Public.class)
    @JoinTable(name = "COMPOSITION_ARRANGER", joinColumns = {
            @JoinColumn(name = "COMPOSITION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "COMPOSER_ID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Composer> arrangers;


    @Column(name = "TITLE")
    private String title;

    @Column(name = "SUBTITLE")
    private String subtitle;

    @Column(name = "GENRE")
    private String genre;

    @Column(name = "DESCRIPTION")
    private String description;

}
