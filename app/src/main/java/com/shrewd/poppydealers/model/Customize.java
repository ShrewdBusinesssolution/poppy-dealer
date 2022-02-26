package com.shrewd.poppydealers.model;

import java.util.List;

public class Customize {

    String category_id;
    String product_id;
    List<CustomizeItem> list;

    public Customize(String product_id, List<CustomizeItem> list) {

        this.product_id = product_id;
        this.list = list;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public List<CustomizeItem> getList() {
        return list;
    }

    public void setList(List<CustomizeItem> list) {
        this.list = list;
    }


}
