package com.lee.tally.model;

/**
 * 收入/支出具体类型
 */
public class Type {
    private int id;
    private String name; // 类型名称
    private int imageId; // 图片id
    private int selectImageId; // 选中图片id
    private int kind; // 收入1 支出0

    public Type() {
    }

    public Type(int id, String name, int imageId, int selectImageId, int kind) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
        this.selectImageId = selectImageId;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSelectImageId() {
        return selectImageId;
    }

    public void setSelectImageId(int selectImageId) {
        this.selectImageId = selectImageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageId=" + imageId +
                ", selectImageId=" + selectImageId +
                ", kind=" + kind +
                '}';
    }
}
