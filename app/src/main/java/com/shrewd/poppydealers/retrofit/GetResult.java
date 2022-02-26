package com.shrewd.poppydealers.retrofit;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.utilities.MyApplication;
import com.shrewd.poppydealers.utilities.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetResult {
    public static MyListener myListener;

    public void callForResponse(Call<JsonObject> call, String callno) {
        if (!Utility.internetCheck()) {
            Toast.makeText(MyApplication.mContext, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("message", " : " + response.message());
                    Log.e("body", " : " + response.body());
                    Log.e("callno", " : " + callno);
                    myListener.callback(response.body(), callno);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    myListener.callback(null, callno);

                    call.cancel();
                    t.printStackTrace();
                }
            });
        }
    }

    public void setMyListener(MyListener Listener) {
        myListener = Listener;
    }

    public interface MyListener {
        // you can define any parameter as per your requirement
        void callback(JsonObject result, String callNo);
    }
}
