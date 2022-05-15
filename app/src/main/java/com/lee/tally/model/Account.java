package com.lee.tally.model;

import java.util.Date;

public class Account {
    private int id;
    private String typeName; // 类型名称
    private int selectImageId; // 类型图片
    private String comment; // 备注
    private float money; // 金额
    private Date date; // 时间
    private int kind; // 收入1 支出0

    public Account() {
    }

    public Account(int id, String typeName, int selectImageId, String comment, float money, Date date, int kind) {
        this.id = id;
        this.typeName = typeName;
        this.selectImageId = selectImageId;
        this.comment = comment;
        this.money = money;
        this.date = date;
        this.kind = kind;
    }

    public void setDatePart(int year, int month, int dayOfMonth) {
        date.setYear(year);
        date.setMonth(month);
        date.setDate(dayOfMonth);
    }

    public void setTimePart(int hourOfDay, int minute, int seconds) {
        date.setHours(hourOfDay);
        date.setMinutes(minute);
        date.setSeconds(seconds);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", selectImageId=" + selectImageId +
                ", comment='" + comment + '\'' +
                ", money=" + money +
                ", date=" + date +
                ", kind=" + kind +
                '}';
    }
}
