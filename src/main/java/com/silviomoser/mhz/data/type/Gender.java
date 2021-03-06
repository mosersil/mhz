package com.silviomoser.mhz.data.type;

import com.silviomoser.mhz.ui.i18.I18Helper;

/**
 * Created by silvio on 26.05.18.
 */
public enum Gender implements HasLabel {
    MALE("tag_male"),
    FEMALE("tag_female"),
    NA("tag_na");

    String tag;

    Gender(String tag) {
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
