package com.sugar.common.enums;

public enum ItemEnum {
    BNB(1001, "bnb"),
    GE(1002, "ge"),
    PTM(1003, "ptm");
    public int value;
    public String name;

    ItemEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
