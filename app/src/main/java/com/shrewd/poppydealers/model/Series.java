package com.shrewd.poppydealers.model;

import java.util.List;

public class Series {

    String series;
    List<Product> products;

    public Series(String series, List<Product> products) {
        this.series = series;
        this.products = products;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
