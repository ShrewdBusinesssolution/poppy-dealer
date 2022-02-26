package com.shrewd.poppydealers.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.model.Payment;
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

public class PaymentRepo {
    private static final String TAG = "PaymentRepo";
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<Payment>> mutablePaymentList = new MutableLiveData<>();

    public LiveData<List<Payment>> getPayments(Context context) {
        loadPayments(context);
        return mutablePaymentList;
    }

    private void loadPayments(Context context) {

        SessionManager sessionManager = new SessionManager(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.DEALER_ID, sessionManager.getString(Constants.DEALER_ID));
            jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .getPayment(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        String amount = String.valueOf(apiResponses.get("credit_amount"));
                        sessionManager.putString(Constants.KEY_CREDIT_AMOUNT, amount);

                        String json = String.valueOf(apiResponses.get("data"));
                        try {
                            JSONArray jsonObject = new JSONArray(json);
                            List<Payment> payments = GetJsonArray(jsonObject);

                            mutablePaymentList.setValue(payments);


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

    private List<Payment> GetJsonArray(JSONArray jsonObject) throws JSONException {
        List<Payment> orders = new ArrayList<>();
        for (int j = 0; j < jsonObject.length(); j++) {
            JSONObject object = jsonObject.getJSONObject(j);
            orders.add(new Payment(object.getString("order_id"), object.getString("payment_date"), object.getString("product_count") + " products", object.getInt("payment_amount"), object.getString("payment_status")));

        }

        return orders;
    }
}
