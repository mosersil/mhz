package com.silviomoser.demo.data.type;

import com.silviomoser.demo.ui.i18.I18Helper;
import com.silviomoser.demo.utils.HasLabel;

/**
 * Created by silvio on 26.05.18.
 */
public enum Gender implements HasLabel {
    MALE("title_male"),
    FEMALE("title_female");

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
