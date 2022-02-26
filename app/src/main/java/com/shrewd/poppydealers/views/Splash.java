package com.shrewd.poppydealers.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.MainActivity;
import com.shrewd.poppydealers.views.activity.intro.Intro;


public class Splash extends AppCompatActivity {
    private final String TAG = Splash.class.getSimpleName();
    SessionManager sessionManager;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versioncode = pInfo.versionName;
            sessionManager.putString(Constants.VERSION, versioncode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        getFcmData();


        if (sessionManager.getBoolean(Constants.KEY_LOGIN)) {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(Splash.this, Intro.class));
            finish();
        }

    }

    private void getFcmData() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        // Get new FCM registration token
                        String token = task.getResult();
                        sessionManager.putString(Constants.KEY_FCM_TOKEN, token);
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("dealer")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

}