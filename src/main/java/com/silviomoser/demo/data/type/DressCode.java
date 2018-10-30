package com.silviomoser.demo.data.type;

import com.silviomoser.demo.ui.i18.I18Helper;

/**
 * Created by silvio on 22.05.18.
 */
public enum DressCode implements HasLabel {
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

    @Override
    public String getLabel() {
        I18Helper i18Helper = new I18Helper();
        return i18Helper.getMessage(tag);
    }
}
