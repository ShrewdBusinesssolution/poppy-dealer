package com.shrewd.poppydealers.model;

public class CustomizeItem {

    private String product_id;
    private int group_id;
    private int group_count;
    private String inches;
    private String color;
    private String color_code;
    private String size;

    public CustomizeItem() {
    }

    public CustomizeItem(int group_id, int group_count, String inches, String BedSize, String Rupees, String size) {
        this.group_id = group_id;
        this.group_count = group_count;
        this.inches = inches;
        this.color = BedSize;
        this.color_code = Rupees;
        this.size = size;

    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getGroup_count() {
        return group_count;
    }

    public void setGroup_count(int group_count) {
        this.group_count = group_count;
    }

    public String getInches() {
        return inches;
    }

    public void setInches(String inches) {
        this.inches = inches;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
