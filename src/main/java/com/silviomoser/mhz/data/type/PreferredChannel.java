package com.silviomoser.mhz.data.type;

import com.silviomoser.mhz.ui.i18.I18Helper;

public enum PreferredChannel implements HasLabel {
    MAIL("channel_mail"),
    ELECTRONIC("channel_electronic");

    String label;

    @Override
    public String getLabel() {
        I18Helper i18Helper = new I18Helper();
        return i18Helper.getMessage(label);
    }

    PreferredChannel(String label) {
        this.label = label;
    }

}
