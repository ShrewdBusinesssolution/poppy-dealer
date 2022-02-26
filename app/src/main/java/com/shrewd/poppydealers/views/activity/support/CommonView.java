package com.shrewd.poppydealers.views.activity.support;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.databinding.ActivityCommonViewBinding;
import com.shrewd.poppydealers.rxjava.RxClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CommonView extends AppCompatActivity {
    private static final String TAG = CommonView.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    ActivityCommonViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommonViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.commonPageToolbar.commonTitle.setText("Support");
        binding.commonPageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RxClient.getInstance()
                .getSupport()
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
                            for (int i = 0; i < jsonObject.length(); i++) {
                                JSONObject object = jsonObject.getJSONObject(i);

                                binding.supportCompanyName.setText(object.getString("company_name"));
                                binding.supportEmail.setText(object.getString("email"));
                                binding.supportMobile.setText(object.getString("mobile"));
                                binding.supportWarehouse.setText(object.getString("warehouse"));
                                binding.supportAddress.setText(object.getString("company_name"));

                                binding.supportGmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri data = Uri.parse("mailto:"
                                                + binding.supportEmail.getText().toString().trim()
                                                + "?subject=" + "" + "&body=" + "");
                                        intent.setData(data);
                                        startActivity(intent);
                                    }
                                });

                                binding.supportWhatsapp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String number = binding.supportMobile.getText().toString();
                                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + number;
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    }
                                });

                                binding.supportFacebook.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            String facebook = object.getString("facebook");

                                            String pageUrl = "https://www.facebook.com/" + facebook;
                                            try {
                                                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);

                                                if (applicationInfo.enabled) {
                                                    int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                                                    String url;

                                                    if (versionCode >= 3002850) {
                                                        url = "fb://facewebmodal/f?href=" + pageUrl;
                                                    } else {
                                                        url = "fb://page/" + facebook;
                                                    }

                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                } else {
                                                    throw new Exception("Facebook is disabled");
                                                }
                                            } catch (Exception e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl)));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                binding.supportTwitter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            String twitter = object.getString("twitter");
                                            Intent intent = null;
                                            try {
                                                // get the Twitter app if possible
                                                getPackageManager().getPackageInfo("com.twitter.android", 0);
                                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            } catch (Exception e) {
                                                // no Twitter app, revert to browser
                                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitter));
                                            }
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                binding.supportInstagram.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            String instagram = object.getString("instagram");
                                            Uri uri = Uri.parse("http://instagram.com/_u/" + instagram);
                                            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                                            likeIng.setPackage("com.instagram.android");

                                            try {
                                                startActivity(likeIng);
                                            } catch (ActivityNotFoundException e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://instagram.com/" + instagram)));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }


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
}