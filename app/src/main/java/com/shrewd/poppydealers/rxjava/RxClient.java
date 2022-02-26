package com.shrewd.poppydealers.rxjava;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxClient {
    public static String BASE_URL = "https://poppyindia.com/erp/";
    public static String IMAGE_BASE_URL = "assets/images/";

    private static RxClient instance;
    private final RxService rxService;

    private RxClient() {
        final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        rxService = retrofit.create(RxService.class);
    }

    public static RxClient getInstance() {
        if (instance == null) {
            instance = new RxClient();
        }
        return instance;
    }

    public Observable<JsonObject> getLogin(RequestBody requestBody) {
        return rxService.getLogin(requestBody);
    }

    public Observable<JsonObject> getHome(RequestBody requestBody) {
        return rxService.getHome(requestBody);
    }

    public Observable<JsonObject> getRegister(RequestBody requestBody) {
        return rxService.getRegister(requestBody);
    }

    public Observable<JsonObject> getAllProducts() {
        return rxService.getProducts();
    }

    public Observable<JsonObject> getProductCustomize(String category,String product) {
        return rxService.getCustomize(category,product);
    }
    public Observable<JsonObject> getProductCustomizePrize(String product,String category,String Bedtype,String Dimon ,String Thickness) {
        return rxService.getcustprize(product,category,Bedtype,Dimon,Thickness);
    }

    public Observable<JsonObject> getProductCustomizes(String category,String product,String Bedtype,String Dimon) {
        return rxService.getCustomizes(category,product,Bedtype,Dimon);
    }

    public Observable<JsonObject> addCart(RequestBody requestBody) {
        return rxService.addCart(requestBody);
    }

    public Observable<JsonObject> subCart(RequestBody requestBody) {
        return rxService.subCart(requestBody);
    }

    public Observable<JsonObject> getCart(RequestBody requestBody) {
        return rxService.getCart(requestBody);
    }

    public Observable<JsonObject> orderAdd(RequestBody requestBody) {
        return rxService.orderAdd(requestBody);
    }

    public Observable<JsonObject> getOrder(RequestBody requestBody) {
        return rxService.getOrder(requestBody);
    }

    public Observable<JsonObject> getOrderView(RequestBody requestBody) {
        return rxService.getOrderView(requestBody);
    }

    public Observable<JsonObject> getPayment(RequestBody requestBody) {
        return rxService.getPayment(requestBody);
    }

    public Observable<JsonObject> getProfile(RequestBody requestBody) {
        return rxService.getProfile(requestBody);
    }

    public Observable<JsonObject> EditProfile(RequestBody requestBody) {
        return rxService.EditProfile(requestBody);
    }

    public Observable<JsonObject> getNotification(RequestBody requestBody) {
        return rxService.getNotification(requestBody);
    }

    public Observable<JsonObject> addFeedBack(RequestBody requestBody) {
        return rxService.addFeedBack(requestBody);
    }

    public Observable<JsonObject> getSupport() {
        return rxService.getSupport();
    }
}
