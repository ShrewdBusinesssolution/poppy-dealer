package com.shrewd.poppydealers.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.model.Notify;
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


public class NotifyRepo {
    private static final String TAG = "NotifyRepo";
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<Notify>> mutableNotificationList = new MutableLiveData<>();


    public LiveData<List<Notify>> getNotifications(Context context) {
        loadNotification(context);
        return mutableNotificationList;
    }

    private void loadNotification(Context context) {

        SessionManager sessionManager = new SessionManager(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_USER_MOBILE, "8098629606");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .getNotification(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        List<Notify> notifyList = new ArrayList<>();

                        String json = String.valueOf(apiResponses.get("data"));
                        try {
                            JSONArray jsonObject = new JSONArray(json);
                            notifyList = GetJsonArray(jsonObject);

                            mutableNotificationList.setValue(notifyList);


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

    private List<Notify> GetJsonArray(JSONArray jsonObject) throws JSONException {
        List<Notify> notifies = new ArrayList<>();
        for (int j = 0; j < jsonObject.length(); j++) {
            JSONObject object = jsonObject.getJSONObject(j);
            notifies.add(new Notify(object.getString("notification_id"), object.getString("notification_title"), object.getString("notification_body"), object.getString("notification_date")));

        }

        return notifies;
    }
}
