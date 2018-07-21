package com.silviomoser.demo.data.type;

/**
 * Created by silvio on 22.05.18.
 */
public enum DressCode {
    CASUAL("dresscode_casual"),
    UNIFORM("dresscode_uniform"),
    COSTUME("dresscode_costume");

    String tag;

    DressCode(String tag) {
        this.tag=tag;
    }

    public String getTag() {
        return tag;
    }
}
