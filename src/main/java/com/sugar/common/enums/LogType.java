package com.sugar.common.enums;

public enum LogType {
    BNB_TREASURE_HUNT(1000, "bnb寻宝"), GE_TREASURE_HUNT(1001, "ge寻宝"), PTM_TREASURE_HUNT(1002, "ptm寻宝"), LOTTERY_TREASURE_HUNT(1003, "奖券寻宝"), SELLING_ITEMS(1004, "出售物品所得"), PURCHASE_THE_TRADING_MARKET(1005, "在交易市场上购买"), CARD_BATTLE(1006, "战斗奖励");
    public int value;
    public String describe;

    LogType(int value, String describe) {
        this.value = value;
        this.describe = describe;
    }
}
