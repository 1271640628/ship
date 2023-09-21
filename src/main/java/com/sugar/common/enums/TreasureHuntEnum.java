package com.sugar.common.enums;

public enum TreasureHuntEnum {

    BNBLUCKYDRAW(1, 1007, 1005, 1009),
    GELUCKYDRAW(2, 1008, 1006, 1010),
    PTMLUCKYDRAW(3, 1027, 1031, 1029),
    JQLUCKYDRAW(4, 1028, 1032, 1030);

    /**
     * 类型
     */
    private int type;
    /**
     * 消耗
     */
    private int cost;
    /**
     * 最大次数
     */
    private int maxCount;
    /**
     * 奖励
     */
    private int reward;

    TreasureHuntEnum(int type, int cost, int maxCount, int reward) {
        this.type = type;
        this.cost = cost;
        this.maxCount = maxCount;
        this.reward = reward;
    }

    public static int getCost(int type) {
        TreasureHuntEnum[] treasureHuntEnums = TreasureHuntEnum.values();
        for (TreasureHuntEnum t : treasureHuntEnums) {
            if (t.type == type) return t.cost;
        }
        return 0;
    }

    public static int getMaxCount(int type) {
        TreasureHuntEnum[] treasureHuntEnums = TreasureHuntEnum.values();
        for (TreasureHuntEnum t : treasureHuntEnums) {
            if (t.type == type) return t.maxCount;
        }
        return 0;
    }

    public static int getReward(int type) {
        TreasureHuntEnum[] treasureHuntEnums = TreasureHuntEnum.values();
        for (TreasureHuntEnum t : treasureHuntEnums) {
            if (t.type == type) return t.reward;
        }
        return 0;
    }
}
