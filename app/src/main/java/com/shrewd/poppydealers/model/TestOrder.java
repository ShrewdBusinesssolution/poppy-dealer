package com.shrewd.poppydealers.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestOrder {

    @SerializedName("dealer_id")
    @Expose
    private String dealerId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("orderproduct_count")
    @Expose
    private String orderproductCount;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderproductCount() {
        return orderproductCount;
    }

    public void setOrderproductCount(String orderproductCount) {
        this.orderproductCount = orderproductCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "TestOrder{" +
                "dealerId='" + dealerId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderproductCount='" + orderproductCount + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
