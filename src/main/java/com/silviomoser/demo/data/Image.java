package com.silviomoser.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "IMAGE")
public class Image extends AbstractEntity{

    @Column(name = "RAW")
    String raw;
    @Column(name = "THUMBNAIL")
    String thumbnail;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "DATE")
    private LocalDateTime dateStart;
    @Column(name = "APPROVED")
    private boolean approved;
}
