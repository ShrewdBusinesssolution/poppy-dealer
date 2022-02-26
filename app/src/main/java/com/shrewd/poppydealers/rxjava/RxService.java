package com.shrewd.poppydealers.rxjava;


import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RxService {


    @POST("home/login_api")
    Observable<JsonObject> getLogin(@Body RequestBody requestBody);

    @POST("home/dealer_dashboard_api")
    Observable<JsonObject> getHome(@Body RequestBody requestBody);

    @POST("home/register_api")
    Observable<JsonObject> getRegister(@Body RequestBody requestBody);

    @GET("home/product_api")
    Observable<JsonObject> getProducts();

    @FormUrlEncoded
    @POST("home/product_customise_api")
    Observable<JsonObject> getCustomize(@Field("category_id") String category, @Field("product_id") String product);

    @POST("Home/product_customiseprice_api")
    Observable<JsonObject> getcustprize(@Query("product_id") String product_id, @Query("category_id") String category_id, @Query("bed_type") String bed_type, @Query("dimension") String dimension, @Query("thickness") String thickness);

    @FormUrlEncoded
    @POST("home/product_customise_api")
    Observable<JsonObject> getCustomizes(@Field("category_id") String category_id, @Field("product_id") String product_id, @Field("bed_type") String bed_type, @Field   ("dimension") String dimension);

    @POST("home/cart_api")
    Observable<JsonObject> addCart(@Body RequestBody requestBody);

    @POST("home/cart_decrease_api")
    Observable<JsonObject> subCart(@Body RequestBody requestBody);

    @POST("home/cart_view_api")
    Observable<JsonObject> getCart(@Body RequestBody requestBody);

    @POST("home/order_insert_api")
    Observable<JsonObject> orderAdd(@Body RequestBody requestBody);

    @POST("home/order_filter_api")
    Observable<JsonObject> getOrder(@Body RequestBody requestBody);

    @POST("home/order_preview_api")
    Observable<JsonObject> getOrderView(@Body RequestBody requestBody);

    @POST("home/payment_history_api")
    Observable<JsonObject> getPayment(@Body RequestBody requestBody);

    @POST("home/profile_api")
    Observable<JsonObject> getProfile(@Body RequestBody requestBody);

    @POST("home/profile_edit_api")
    Observable<JsonObject> EditProfile(@Body RequestBody requestBody);

    @POST("home/notification_api")
    Observable<JsonObject> getNotification(@Body RequestBody requestBody);

    @POST("home/feedback_api")
    Observable<JsonObject> addFeedBack(@Body RequestBody requestBody);

    @POST("home/support_api")
    Observable<JsonObject> getSupport();


}
