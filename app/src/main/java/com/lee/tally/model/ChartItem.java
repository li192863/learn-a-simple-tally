package com.lee.tally.model;

public class ChartItem {
    private String typeName; // 类型名称
    private int selectImageId; // 类型图片
    private float money; // 金额
    private float percentage; // 比例

    public ChartItem() {
    }

    public ChartItem(String typeName, int selectImageId, float money, float percentage) {
        this.typeName = typeName;
        this.selectImageId = selectImageId;
        this.money = money;
        this.percentage = percentage;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSelectImageId() {
        return selectImageId;
    }

    public void setSelectImageId(int selectImageId) {
        this.selectImageId = selectImageId;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "ChartItem{" +
                "typeName='" + typeName + '\'' +
                ", selectImageId=" + selectImageId +
                ", money=" + money +
                ", percentage=" + percentage +
                '}';
    }
}
