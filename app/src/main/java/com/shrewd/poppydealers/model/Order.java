package com.shrewd.poppydealers.model;

import java.util.Comparator;

public class Order  {

    public static final Comparator<Order> LOW_TO_HIGH = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {


            return o1.getOrder_price() - o2.getOrder_price();
        }
    };
    public static final Comparator<Order> HIGH_TO_LOW = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {


            return o2.getOrder_price() - o1.getOrder_price();
        }
    };
    String order_id;
    String order_date;
    String order_product;
    int order_price;
    String order_status;

    public Order(String order_id, String order_date, String order_product, int order_price, String order_status) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.order_product = order_product;
        this.order_price = order_price;
        this.order_status = order_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_product() {
        return order_product;
    }

    public void setOrder_product(String order_product) {
        this.order_product = order_product;
    }

    public int getOrder_price() {
        return order_price;
    }

    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
