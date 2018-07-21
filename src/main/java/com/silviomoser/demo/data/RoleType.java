package com.silviomoser.demo.data;


public enum RoleType {

    ADMIN,
    USER;

    public static RoleType findByType(String type) {
        for(RoleType role: values()) {
            if(role.name().equals(type)) {
                return role;
            }
        }
        return null;
    }
}
