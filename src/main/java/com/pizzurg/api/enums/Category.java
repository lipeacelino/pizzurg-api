package com.pizzurg.api.enums;

import org.mapstruct.EnumMapping;

public enum Category {
    PIZZA ("Pizza"),
    HAMBURGUER ("Hambúrguer");

    private String name;

    private Category(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
