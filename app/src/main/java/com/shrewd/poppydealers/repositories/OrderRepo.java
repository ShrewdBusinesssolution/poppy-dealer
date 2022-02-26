package com.shrewd.poppydealers.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.model.Order;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class OrderRepo {
    private static final String TAG = "OrderRepo";
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<Order>> mutableOrderList = new MutableLiveData<>();

    public LiveData<List<Order>> getOrders(Context context, String status, String from, String to, String value) {
        loadOrders(context, status, from, to, value);
        return mutableOrderList;
    }

    private void loadOrders(Context context, String status, String from, String to, String value) {
        SessionManager sessionManager = new SessionManager(context);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
            jsonObject.put(Constants.KEY_STATUS, status);
            jsonObject.put(Constants.KEY_FROM_DATE, from);
            jsonObject.put(Constants.KEY_TO_DATE, to);
            jsonObject.put(Constants.KEY_VALUE, value);
        } catch (Exception e) {
            e.printStackTrace();
        }



        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .getOrder(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {


                        String json = String.valueOf(apiResponses.get("data"));
                        try {
                            JSONArray jsonObject = new JSONArray(json);
                            List<Order> orders = GetJsonArray(jsonObject);

                            mutableOrderList.setValue(orders);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private List<Order> GetJsonArray(JSONArray jsonObject) throws JSONException {
        List<Order> orders = new ArrayList<>();
        for (int j = 0; j < jsonObject.length(); j++) {
            JSONObject object = jsonObject.getJSONObject(j);
            orders.add(new Order(object.getString("order_id"), object.getString("order_date"), object.getString("orderproduct_count") + " products", Integer.parseInt(object.getString("total_amount")), object.getString("order_status")));


        }

        return orders;
    }
}


