package com.shrewd.poppydealers.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.model.Customize;
import com.shrewd.poppydealers.model.Order;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void customizeWrite(Context context, List<Customize> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_PRODUCT_CUSTOMIZE, jsonString);
        editor.apply();
    }

    public List<Customize> customizeRead(Context context) {

        String jsonString = sharedPreferences.getString(Constants.KEY_PRODUCT_CUSTOMIZE, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Customize>>() {
        }.getType();
        List<Customize> list = gson.fromJson(jsonString, type);
        return list;
    }

    public void cartWrite(Context context, List<CartItem> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_PRODUCT_CART, jsonString);
        editor.apply();
    }

    public List<CartItem> cartRead(Context context) {

        String jsonString = sharedPreferences.getString(Constants.KEY_PRODUCT_CART, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartItem>>() {
        }.getType();
        List<CartItem> list = gson.fromJson(jsonString, type);
        return list;
    }

    public void orderWrite(Context context, List<Order> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_ORDER, jsonString);
        editor.apply();
    }

    public List<Order> orderRead(Context context) {

        String jsonString = sharedPreferences.getString(Constants.KEY_ORDER, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Order>>() {
        }.getType();
        List<Order> list = gson.fromJson(jsonString, type);
        return list;
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {

        return sharedPreferences.getString(key, null);
    }

    public void clearPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void clearUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.DEALER_ID);
        editor.commit();
    }

    public void customizeClear(Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.KEY_PRODUCT_CUSTOMIZE);
        editor.commit();
    }

    public void cartClear(Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.KEY_PRODUCT_CART);
        editor.commit();
    }

    public void orderClear(Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.KEY_ORDER);
        editor.commit();
    }
}
