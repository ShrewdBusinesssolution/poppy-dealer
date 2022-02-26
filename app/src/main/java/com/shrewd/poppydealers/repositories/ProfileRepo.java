package com.shrewd.poppydealers.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.model.User;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProfileRepo {
    private static final String TAG = "ProfileRepo";
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<User> mutableOrderList = new MutableLiveData<>();

    public LiveData<User> getProfile(Context context) {
        loadProfile(context);
        return mutableOrderList;
    }

    private void loadProfile(Context context) {
        SessionManager sessionManager = new SessionManager(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_TYPE, "dealer");
            jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .getProfile(bodyRequest)
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
                            User orders = GetJsonArray(jsonObject);

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

    private User GetJsonArray(JSONArray jsonObject) throws JSONException {
        User user = null;
        for (int j = 0; j < jsonObject.length(); j++) {
            JSONObject object = jsonObject.getJSONObject(j);

            user = new User(object.getString("shop_name"), object.getString("dealer_mobile"), object.getString("dealer_email"), object.getString("dealer_experience"), object.getString("dealer_experience_month"), object.getString("dealer_address"), object.getString("dealer_district"), object.getString("dealer_state"), object.getString("dealer_pincode"), object.getString("landline"), object.getString("current_business"), object.getBoolean("kyc_status"));


        }

        return user;
    }
}
