package com.shrewd.poppydealers.model;

public class Payment {

    String payment_id;
    String payment_date;
    String payment_product;
    int payment_price;
    String payment_status;

    public Payment(String payment_id, String payment_date, String payment_product, int payment_price, String payment_status) {
        this.payment_id = payment_id;
        this.payment_date = payment_date;
        this.payment_product = payment_product;
        this.payment_price = payment_price;
        this.payment_status = payment_status;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_product() {
        return payment_product;
    }

    public void setPayment_product(String payment_product) {
        this.payment_product = payment_product;
    }

    public int getPayment_price() {
        return payment_price;
    }

    public void setPayment_price(int payment_price) {
        this.payment_price = payment_price;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
