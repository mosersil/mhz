package com.silviomoser.mhz.data;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "COMPOSER")
public class Composer extends AbstractEntity {

    @JsonView(Views.Public.class)
    private String name;

    @ManyToMany(mappedBy = "composers", fetch = FetchType.EAGER)
    private Set<Composition> compositions;
}
