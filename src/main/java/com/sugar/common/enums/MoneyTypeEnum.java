package com.sugar.common.enums;

public enum MoneyTypeEnum {
    ONE(1, "bnb", 1001),
    TWO(2, "ge", 1002),
    THREE(3, "ptm", 1003);

    MoneyTypeEnum(int type, String name, int itemId) {
        this.type = type;
        this.name = name;
        this.itemId = itemId;
    }

    private int type;
    private String name;

    private int itemId;

    public static String getMoneyNmae(int type) {
        MoneyTypeEnum[] moneyTypeEnum = MoneyTypeEnum.values();
        for (MoneyTypeEnum m : moneyTypeEnum) {
            if (m.type == type) return m.name;
        }
        return null;
    }

    public static int getMoneyItemId(int type) {
        MoneyTypeEnum[] moneyTypeEnum = MoneyTypeEnum.values();
        for (MoneyTypeEnum m : moneyTypeEnum) {
            if (m.type == type) return m.itemId;
        }
        return 0;
    }

    public static int getMoneyTypeMax() {
        MoneyTypeEnum[] moneyTypeEnum = MoneyTypeEnum.values();
        return moneyTypeEnum[moneyTypeEnum.length - 1].type;
    }

}
