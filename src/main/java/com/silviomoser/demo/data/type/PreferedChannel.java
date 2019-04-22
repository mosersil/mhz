package com.silviomoser.demo.data.type;

import com.silviomoser.demo.ui.i18.I18Helper;

public enum PreferedChannel implements HasLabel {
    MAIL("channel_mail"),
    ELECTRONIC("channel_electronic");

    String label;

    @Override
    public String getLabel() {
        I18Helper i18Helper = new I18Helper();
        return i18Helper.getMessage(label);
    }

    PreferedChannel(String label) {
        this.label = label;
    }

}
