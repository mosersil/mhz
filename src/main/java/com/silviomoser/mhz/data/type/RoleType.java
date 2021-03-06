package com.silviomoser.mhz.data.type;


import com.silviomoser.mhz.ui.i18.I18Helper;

public enum RoleType implements HasLabel{

    USER("role_user"),
    ADMIN("role_admin"),
    DATAVIEWER("role_dataviewer"),
    PERSMGR("role_persmgr"),
    CONTMGR("role_contmgr"),
    USRMGR("role_usrmgr");

    String tag;

    RoleType(String tag) {
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

    public static RoleType findByType(String type) {
        for(RoleType role: values()) {
            if(role.name().equals(type)) {
                return role;
            }
        }
        return null;
    }
}
