package com.silviomoser.demo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by silvio on 23.07.18.
 */
@Entity
@Table(name = "ARTICLE")
public class Article extends AbstractEntity<Long> {


    @Column(name = "TITLE")
    private String title;
    @Column(name = "TEXT")
    private String text;
    @NotNull
    @Column(name = "START_DATE")
    private LocalDateTime startDate;
    @NotNull
    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    public Article(String title, LocalDateTime startDate) {
        this.title=title;
        this.startDate=startDate;
    }

    public Article() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
