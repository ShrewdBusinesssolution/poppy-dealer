package com.shrewd.poppydealers.views.activity.edit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.ActivityEditProfileBinding;
import com.shrewd.poppydealers.model.User;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.LoadingProgress;
import com.shrewd.poppydealers.utilities.MapsActivity;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.fragment.profile.ProfileViewModel;

import org.json.JSONObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = EditProfile.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    ActivityEditProfileBinding binding;
    private LoadingProgress loadingProgress;
    private SessionManager sessionManager;
    private ProfileViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        loadingProgress = LoadingProgress.getInstance();
        sessionManager = new SessionManager(this);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String classType = bundle.getString(Constants.KEY_CLASS_TYPE);
            String Address = bundle.getString(Constants.KEY_MAP_ADDRESS);

            if (Address != null) {
                binding.editProfileAddress.setText(Address);
            }

            if (classType != null && classType.equals("Add")) {

                binding.editProfileLayout.setVisibility(View.GONE);
                binding.ProfileAddLayout.setVisibility(View.VISIBLE);
            } else {
                binding.ProfileAddLayout.setVisibility(View.GONE);
                binding.editProfileLayout.setVisibility(View.VISIBLE);
            }

        } else {

            binding.ProfileAddLayout.setVisibility(View.GONE);
            binding.editProfileLayout.setVisibility(View.VISIBLE);


            binding.editMapPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EditProfile.this, MapsActivity.class).putExtra(Constants.KEY_CLASS_TYPE, "edit"));
                }
            });


            mViewModel.getProfile(EditProfile.this).observe(EditProfile.this, new Observer<User>() {
                @Override
                public void onChanged(User user) {

                    binding.editProfileName.setText(user.getName());
                    binding.editProfileEmail.setText(user.getEmail());
                    binding.editProfileMobile.setText(user.getMobile());
                    binding.editProfileAddress.setText(user.getAddress());
                    binding.editProfilelandline.setText(user.getLandline());
                    binding.editProfileBusiness.setText(user.getBusiness());
                    binding.editProfileExperience.setText(user.getYearlyExperience());
                    binding.editProfileExperienceMonth.setText(user.getMonthlyExperience());
                    binding.editProfileDistrict.setText(user.getDistrict());
                    binding.editProfileState.setText(user.getState());
                    binding.editProfilePincode.setText(user.getPincode());
                    if (user.getKyc_status()) {
                        binding.editKyc.setText("KYC uploaded Successfully");
                        binding.editKyc.setTextColor(ContextCompat.getColor(EditProfile.this, R.color.status_green));
                    } else {
                        binding.editKyc.setText("Need to upload KYC");
                    }

                }
            });

        }

        binding.signUp.signIn.setVisibility(View.GONE);
        binding.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.signUp.signUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = binding.editProfileName.getText().toString();
                String Address = binding.editProfileAddress.getText().toString();
                String Email = binding.editProfileEmail.getText().toString();
                String Mobile = binding.editProfileMobile.getText().toString();
                String Landline = binding.editProfilelandline.getText().toString();
                String Business = binding.editProfileBusiness.getText().toString();
                String Experience = binding.editProfileExperience.getText().toString();
                String ExperienceMonth = binding.editProfileExperienceMonth.getText().toString();
                String District = binding.editProfileDistrict.getText().toString();
                String State = binding.editProfileState.getText().toString();
                String Pincode = binding.editProfilePincode.getText().toString();


                if (Name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_SHORT).show();
                } else if (Address.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter address", Toast.LENGTH_SHORT).show();
                } else if (Email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_SHORT).show();
                } else if (Mobile.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter Mobile", Toast.LENGTH_SHORT).show();
                } else if (Landline.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter Landline", Toast.LENGTH_SHORT).show();
                } else if (Business.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter Business", Toast.LENGTH_SHORT).show();
                } else if (Experience.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter Experience", Toast.LENGTH_SHORT).show();
                } else if (District.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter District", Toast.LENGTH_SHORT).show();
                } else if (State.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter State", Toast.LENGTH_SHORT).show();
                } else if (Pincode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter Pincode", Toast.LENGTH_SHORT).show();
                } else {

                    User user = new User(Name, Mobile, Email, Experience, ExperienceMonth, Address, District, State, Pincode, Landline, Business, true);

                    sendRequest(user);

                }


            }
        });

    }


    private void sendRequest(User user) {
        Toast.makeText(getApplicationContext(), user.getMonthlyExperience(), Toast.LENGTH_SHORT).show();
        loadingProgress.ShowProgress(EditProfile.this, "please wait", false);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_NAME, user.getName());
            jsonObject.put(Constants.KEY_USER_MOBILE, user.getMobile());
            jsonObject.put(Constants.KEY_EMAIL, user.getEmail());
            jsonObject.put(Constants.KEY_EXPERIENCE, user.getYearlyExperience());
            jsonObject.put(Constants.KEY_EXPERIENCE_MONTH, user.getMonthlyExperience());
            jsonObject.put(Constants.KEY_LATITUDE, sessionManager.getString(Constants.KEY_LATITUDE));
            jsonObject.put(Constants.KEY_LONGITUDE, sessionManager.getString(Constants.KEY_LONGITUDE));
            jsonObject.put(Constants.KEY_ADDRESS, user.getAddress());
            jsonObject.put(Constants.KEY_DISTRICT, user.getDistrict());
            jsonObject.put(Constants.KEY_STATE, user.getState());
            jsonObject.put(Constants.KEY_PINCODE, user.getPincode());
            jsonObject.put(Constants.KEY_TYPE, "dealer");
            jsonObject.put(Constants.KEY_LANDLINE, user.getLandline());
            jsonObject.put(Constants.KEY_BUSINESS, user.getBusiness());


        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .EditProfile(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {


                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        String success = String.valueOf(apiResponses.get(Constants.KEY_SUCCESS));
                        String message = String.valueOf(apiResponses.get(Constants.KEY_MESSAGE));
                        message = message.replaceAll("\"", "");

                        if (success.equalsIgnoreCase("true")) {
                            loadingProgress.hideProgress();

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        } else {
                            loadingProgress.hideProgress();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}