package com.silviomoser.mhz.data;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompositionFormSubmition {

    @NotNull
    private String title;
    private String subtitle;
    private String genre;
    private String description;
    private String tag;
    @NotNull
    private List<String> composers;
    private List<String> arrangers;

}
