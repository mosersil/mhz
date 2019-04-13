package com.silviomoser.demo.data.type;

import com.silviomoser.demo.ui.i18.I18Helper;

/**
 * Created by silvio on 22.05.18.
 */
public enum DressCode implements HasLabel, HasShortCode {
    CASUAL("dresscode_casual", "Z"),
    UNIFORM("dresscode_uniform", "U"),
    COSTUME("dresscode_costume", "K");

    String tag;
    String shortCode;

    DressCode(String tag, String shortCode) {
        this.tag=tag;
        this.shortCode=shortCode;
    }

    public String getTag() {
        return tag;
    }

    public String getShortCode() { return shortCode; }

    @Override
    public String getLabel() {
        I18Helper i18Helper = new I18Helper();
        return i18Helper.getMessage(tag);
    }
}
