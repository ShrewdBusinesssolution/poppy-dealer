package com.shrewd.poppydealers.model;

import android.os.Parcel;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.Comparator;

public class Product {
    public static final Comparator<Product> LOW_TO_HIGH = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {


            return o1.getProduct_price() - o2.getProduct_price();
        }
    };
    public static final Comparator<Product> HIGH_TO_LOW = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {


            return o2.getProduct_price() - o1.getProduct_price();
        }
    };
    private String category_id;
    private String product_id;
    private String product_name;
    private String product_image;
    private int product_price;
    private String product_type;
    private String product_quantity;
    private JSONArray imageArray;

    public Product(String product_id, String product_name, String product_price, String product_image, String product_type) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = Integer.parseInt(product_price);
        this.product_image = product_image;
        this.product_type = product_type;

    }

    public Product(String category_id, String product_id, String product_type, String product_name, String product_price, JSONArray jsonImage) {
        this.category_id = category_id;
        this.product_id = product_id;
        this.product_type = product_type;
        this.product_name = product_name;
        this.product_price = Integer.parseInt(product_price);
        this.imageArray = jsonImage;
    }


    public Product(String product_id, String product_name, String product_rate, String product_image, String product_type, String product_quantity) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = Integer.parseInt(product_rate);
        this.product_image = product_image;
        this.product_type = product_type;
        this.product_quantity = product_quantity;
    }


    protected Product(Parcel in) {
        category_id = in.readString();
        product_id = in.readString();
        product_name = in.readString();
        product_image = in.readString();
        product_price = in.readInt();
        product_type = in.readString();
    }

    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView).load(imageUrl).into(imageView);

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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public JSONArray getImageArray() {
        return imageArray;
    }

    public void setImageArray(JSONArray imageArray) {
        this.imageArray = imageArray;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}
