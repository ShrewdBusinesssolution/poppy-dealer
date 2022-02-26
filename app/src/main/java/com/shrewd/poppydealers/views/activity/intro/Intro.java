package com.shrewd.poppydealers.views.activity.intro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.ActivityIntroBinding;
import com.shrewd.poppydealers.model.User;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.LoadingProgress;
import com.shrewd.poppydealers.utilities.MapsActivity;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.MainActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Intro extends AppCompatActivity {

    private static final String TAG = Intro.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1234;
    private static final int PAN_DOC_REQUEST = 120;
    private static final int GST_DOC_REQUEST = 121;

    private Uri Panpath, GstPath;
    private Bitmap Panbitmap, Gstbitmap;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static Double latitude, longitude;
    private String loginType = "Nothing";
    private final CompositeDisposable disposable = new CompositeDisposable();
    ActivityIntroBinding binding;
    String[] appPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE};
    private String mVerificationId;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private int position = 0;
    private Animation btnAnim, slideUp, slideDown, slideLeft, slideRight, fadeIn, fadeOut;
    private SessionManager sessionManager;
    private LoadingProgress loadingProgress;
    private FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        client = LocationServices.getFusedLocationProviderClient(Intro.this);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        loadingProgress = LoadingProgress.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnAnim = AnimationUtils.loadAnimation(Intro.this, R.anim.button_animation);
        sessionManager = new SessionManager(Intro.this);
        askpermissions();


        if (restorePrefData()) {
            binding.introScreen.setVisibility(View.GONE);
            binding.introLogin.setVisibility(View.VISIBLE);
        }

        if (sessionManager.getString(Constants.KEY_LATITUDE) != null && sessionManager.getString(Constants.KEY_LONGITUDE) != null) {
            latitude = Double.parseDouble(sessionManager.getString(Constants.KEY_LATITUDE));
            longitude = Double.parseDouble(sessionManager.getString(Constants.KEY_LONGITUDE));

        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(Constants.KEY_MAP_ADDRESS) != null && bundle.getString(Constants.KEY_CLASS_TYPE) != null && bundle.getString(Constants.KEY_CLASS_TYPE).equalsIgnoreCase("signup")) {

                binding.introLogin.setVisibility(View.GONE);
                binding.introRegister.setVisibility(View.VISIBLE);
                binding.registerLayout.signUpAddress.setText(bundle.getString(Constants.KEY_MAP_ADDRESS));
                binding.registerLayout.signUpName.setText(bundle.getString(Constants.KEY_NAME));

                getdata(latitude, longitude);
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }

        initAnimation();

        binding.loginLayout.loginContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = binding.loginLayout.loginNumber.getText().toString();
                String otp = binding.loginLayout.loginOtp.getText().toString();

                if (!TextUtils.isEmpty(number) && loginType.equals("Nothing") && number.length() == 10) {


                    if (binding.loginLayout.loginPolicyCheck.isChecked()) {
                        loadingProgress.ShowProgress(Intro.this, "Please wait", false);
                        startVerification(number);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check Terms of Service, Privacy Policy!", Toast.LENGTH_SHORT).show();
                    }

                } else if (!loginType.equals("Nothing") && mVerificationId != null) {
                    if (!TextUtils.isEmpty(otp) && otp.length() == 6) {
                        verifyPhoneNumberWithCode(mVerificationId, otp);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid mobile number!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        binding.introStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.introScreen.setVisibility(View.GONE);
                binding.introLogin.setVisibility(View.VISIBLE);
                binding.introLogin.setAnimation(slideRight);

                savePrefData();

            }
        });

        binding.loginLayout.loginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.introLogin.setVisibility(View.GONE);
                binding.introLogin.startAnimation(slideDown);

                binding.introRegister.setVisibility(View.VISIBLE);
                binding.introRegister.startAnimation(slideUp);
            }
        });
        binding.registerLayout.signupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.introRegister.setVisibility(View.GONE);
                binding.introRegister.startAnimation(slideDown);

                binding.introLogin.setVisibility(View.VISIBLE);
                binding.introLogin.startAnimation(slideUp);
            }
        });

        binding.registerLayout.signUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.introRegister.setVisibility(View.GONE);
                binding.introRegister.startAnimation(slideDown);

                binding.introLogin.setVisibility(View.VISIBLE);
                binding.introLogin.startAnimation(slideUp);
            }
        });
        binding.registerLayout.signUpMapClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intro.this, MapsActivity.class).putExtra(Constants.KEY_CLASS_TYPE, "intro").putExtra(Constants.KEY_NAME, binding.registerLayout.signUpName.getText().toString()).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

            }
        });
        binding.registerLayout.signUpPanCardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PAN_DOC_REQUEST);
            }
        });
        binding.registerLayout.signUpGSTClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GST_DOC_REQUEST);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken
                    ResendingToken) {
                super.onCodeSent(s, ResendingToken);
                mVerificationId = s;
                loginType = "Something";
                binding.loginLayout.loginTitle.setText("OTP");
                binding.loginLayout.loginDescription.setText("Enter valid verification code");
                binding.loginLayout.loginPhone.setVisibility(View.GONE);
                binding.loginLayout.loginPhone.setAnimation(fadeOut);
                binding.loginLayout.loginOtpLayout.setVisibility(View.VISIBLE);
                binding.loginLayout.loginOtpLayout.setAnimation(fadeIn);

                if (loadingProgress != null) {
                    loadingProgress.hideProgress();
                }

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                Log.d("TAG", "CODE:::" + code);
                if (code != null) {

                    binding.loginLayout.loginOtp.setText(code);

                    signInUsingCredentilas(phoneAuthCredential);
                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Message:::" + e.getMessage());
            }
        };

        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new

                ScreenItem("Poppy Matters", "Sleep on Haven ", R.drawable.contour));
        mList.add(new

                ScreenItem("Sleepy Matters", "Sleep on Water ", R.drawable.contour));
        mList.add(new

                ScreenItem("Cuddle Matters", "Sleep on Sky ", R.drawable.contour));

        introViewPagerAdapter = new

                IntroViewPagerAdapter(this, mList);
        binding.introPager.setAdapter(introViewPagerAdapter);

        binding.introIndicator.setupWithViewPager(binding.introPager);


        binding.introNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = binding.introPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    binding.introPager.setCurrentItem(position);
                }

                if (position == mList.size() - 1) {

                    lastScreen();

                }
            }
        });


        binding.introIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    lastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.loginLayout.loginPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Intro.this, R.style.BottomSheetDialogTheme);

                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_policy, (FrameLayout) findViewById(R.id.policySheetContainer));


                TextView t = bottomSheetView.findViewById(R.id.policyTitle);
                Typeface typeface = ResourcesCompat.getFont(Intro.this, R.font.exo_2_bold);
                t.setTypeface(typeface);
                t.setText("Terms of Service,\n Privacy Policy");


                FrameLayout back = bottomSheetView.findViewById(R.id.policyBack);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        binding.registerLayout.signUpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = binding.registerLayout.signUpName.getText().toString();
                String address = binding.registerLayout.signUpAddress.getText().toString();
                String district = binding.registerLayout.signUpDistrict.getText().toString();
                String state = binding.registerLayout.signUpState.getText().toString();
                String pincode = binding.registerLayout.signUpPincode.getText().toString();
                String mobile = binding.registerLayout.signUpMobile.getText().toString();
                String landline = binding.registerLayout.signUpLandline.getText().toString();
                String email = binding.registerLayout.signUpEmail.getText().toString();
                String business = binding.registerLayout.signUpCurrentBusiness.getText().toString();
                String yearlyExperience = binding.registerLayout.signUpTotalExperience.getText().toString();
                String monthlyExperience = binding.registerLayout.signUpTotalExperience.getText().toString();
                String panCard = binding.registerLayout.signUpPanCardNo.getText().toString();
                String gst = binding.registerLayout.signUpGSTNo.getText().toString();
                String pancardFile = binding.registerLayout.signUpPanCard.getText().toString();
                String gstFile = binding.registerLayout.signUpGST.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_SHORT).show();

                } else if (address.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter address", Toast.LENGTH_SHORT).show();

                } else if (district.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter district", Toast.LENGTH_SHORT).show();

                } else if (state.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter state", Toast.LENGTH_SHORT).show();

                } else if (pincode.isEmpty() || pincode.length() != 6) {
                    if (pincode.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "please enter pincode", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "pincode must be six digits", Toast.LENGTH_SHORT).show();
                    }
                } else if (mobile.isEmpty() || mobile.length() != 10) {
                    if (mobile.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "please enter mobile number", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "number must be 10 digits", Toast.LENGTH_SHORT).show();
                    }
                } else if (isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_SHORT).show();

                } else if (business.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter business", Toast.LENGTH_SHORT).show();

                } else if (panCard.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter pancard number", Toast.LENGTH_SHORT).show();

                } else if (gst.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please select gst file", Toast.LENGTH_SHORT).show();

                } else if (panCard.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter experience in year", Toast.LENGTH_SHORT).show();

                } else if (gst.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter experience in month", Toast.LENGTH_SHORT).show();

                } else if (pancardFile.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please select pancard photocopy", Toast.LENGTH_SHORT).show();

                } else if (gstFile.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please select gst photocopy", Toast.LENGTH_SHORT).show();

                } else {
                    ByteArrayOutputStream panByteArrayOutputStream = new ByteArrayOutputStream();
                    Panbitmap.compress(Bitmap.CompressFormat.JPEG, 70, panByteArrayOutputStream);
                    byte[] panImageInByte = panByteArrayOutputStream.toByteArray();

                    String panImage = Base64.encodeToString(panImageInByte, Base64.DEFAULT);


                    ByteArrayOutputStream GstByteArrayOutputStream = new ByteArrayOutputStream();
                    Gstbitmap.compress(Bitmap.CompressFormat.JPEG, 70, GstByteArrayOutputStream);
                    byte[] GstImageInByte = GstByteArrayOutputStream.toByteArray();

                    String gstImage = Base64.encodeToString(GstImageInByte, Base64.DEFAULT);

                    User user = new User(name, mobile, email, yearlyExperience, monthlyExperience, address, district, state, pincode, landline, business, panCard, panImage, gst, gstImage);

                    sendRequest(user);
                }


            }
        });
    }

    private boolean isValidEmail(String trim) {
        return (!TextUtils.isEmpty(trim) && !Patterns.EMAIL_ADDRESS.matcher(trim).matches());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAN_DOC_REQUEST && resultCode == RESULT_OK && data != null) {

            Panpath = data.getData();
            binding.registerLayout.signUpPanCard.setText(getFileName(Panpath));
            try {
                Panbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Panpath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == GST_DOC_REQUEST && resultCode == RESULT_OK && data != null) {

            GstPath = data.getData();
            binding.registerLayout.signUpGST.setText(getFileName(GstPath));
            try {
                Gstbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), GstPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void sendRequest(User user) {
        loadingProgress.ShowFileProgress(Intro.this, "please wait for uploading GST file", false);

        String token = sessionManager.getString(Constants.KEY_FCM_TOKEN);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_NAME, user.getName());
            jsonObject.put(Constants.KEY_MOBILE, user.getMobile());
            jsonObject.put(Constants.KEY_EMAIL, user.getEmail());
            jsonObject.put(Constants.KEY_EXPERIENCE, user.getYearlyExperience());
            jsonObject.put(Constants.KEY_EXPERIENCE_MONTH, user.getMonthlyExperience());
            jsonObject.put(Constants.KEY_LATITUDE, latitude);
            jsonObject.put(Constants.KEY_LONGITUDE, longitude);
            jsonObject.put(Constants.KEY_ADDRESS, user.getAddress());
            jsonObject.put(Constants.KEY_DISTRICT, user.getDistrict());
            jsonObject.put(Constants.KEY_STATE, user.getState());
            jsonObject.put(Constants.KEY_PINCODE, user.getPincode());
            jsonObject.put(Constants.KEY_FCM, token);
            jsonObject.put(Constants.KEY_TYPE, "dealer");
            jsonObject.put(Constants.KEY_LANDLINE, user.getLandline());
            jsonObject.put(Constants.KEY_BUSINESS, user.getBusiness());
            jsonObject.put(Constants.KEY_PANCARD, user.getPancard());
            jsonObject.put(Constants.KEY_PANCARD_DOC, user.getPancardDoc());
            jsonObject.put(Constants.KEY_GST, user.getGst());
            jsonObject.put(Constants.KEY_GST_DOC, user.getGstDoc());


        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .getRegister(bodyRequest)
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

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        loadingProgress.hideProgress();

                        if (success.equalsIgnoreCase("true")) {

                            binding.registerLayout.signUpName.setText("");
                            binding.registerLayout.signUpAddress.setText("");
                            binding.registerLayout.signUpDistrict.setText("");
                            binding.registerLayout.signUpState.setText("");
                            binding.registerLayout.signUpPincode.setText("");
                            binding.registerLayout.signUpMobile.setText("");
                            binding.registerLayout.signUpLandline.setText("");
                            binding.registerLayout.signUpEmail.setText("");
                            binding.registerLayout.signUpCurrentBusiness.setText("");
                            binding.registerLayout.signUpTotalExperience.setText("");
                            binding.registerLayout.signUpMonthExperience.setText("");
                            binding.registerLayout.signUpPanCard.setText("");
                            binding.registerLayout.signUpPanCardNo.setText("");
                            binding.registerLayout.signUpGST.setText("");
                            binding.registerLayout.signUpGSTNo.setText("");

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

    private void initAnimation() {
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
    }


    private void verifyPhoneNumberWithCode(String mVerificationId, String otp) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInUsingCredentilas(credential);
    }

    private void startVerification(String number) {

        String token = sessionManager.getString(Constants.KEY_FCM_TOKEN);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_mobile", number);
            jsonObject.put("user_fcm", token);
            jsonObject.put("user_type", "dealer");

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());


        RxClient.getInstance()
                .getLogin(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        String success = apiResponses.get(Constants.KEY_SUCCESS).getAsString();
                        String message = apiResponses.get(Constants.KEY_MESSAGE).getAsString();

                        message = message.replaceAll("\"", "");
                        String userId = String.valueOf(apiResponses.get(Constants.KEY_USER_ID));
                        userId = userId.replaceAll("\"", "");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        loadingProgress.hideProgress();

                        if (success.equalsIgnoreCase("true")) {

                            Boolean location = apiResponses.get(Constants.KEY_LOCATION_STATUS).getAsBoolean();

                            sessionManager.putBoolean(Constants.KEY_LOCATION_STATUS, location);
                            sessionManager.putString(Constants.DEALER_ID, userId);
                            sessionManager.putString(Constants.KEY_USER_MOBILE, number);

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + number,
                                    60,
                                    TimeUnit.SECONDS,
                                    TaskExecutors.MAIN_THREAD,
                                    mCallbacks //Object of OnVerificationStateChangedCallbacks
                            );

                            new CountDownTimer(1000 * 60, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    binding.loginLayout.loginOtpSec.setText("seconds remaining: " + millisUntilFinished / 1000);


                                    //here you can have your logic to set text to edittext
                                }

                                public void onFinish() {
                                    binding.loginLayout.loginOtpSec.setEnabled(true);
                                    binding.loginLayout.loginOtpSec.setText("RESEND");

                                    binding.loginLayout.loginOtpSec.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String number = binding.loginLayout.loginNumber.getText().toString();
                                            if (TextUtils.isEmpty(number)) {
                                                startVerification(number);
                                            }
                                        }
                                    });


                                }

                            }.start();
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

    private void signInUsingCredentilas(PhoneAuthCredential credential) {

        loadingProgress.ShowProgress(Intro.this, "Please wait", false);

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (loadingProgress != null) {
                        loadingProgress.hideProgress();
                    }
                    sessionManager.putBoolean(Constants.KEY_LOGIN, true);
                    Toast.makeText(getApplicationContext(), "Welcome to Poppy!", Toast.LENGTH_SHORT).show();

                    if (!sessionManager.getBoolean(Constants.KEY_LOCATION_STATUS)) {
                        startActivity(new Intent(Intro.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(Intro.this, MapsActivity.class).putExtra(Constants.KEY_CLASS_TYPE, "Login"));
                        finish();
                    }

                } else {
                    if (loadingProgress != null) {
                        loadingProgress.hideProgress();
                    }
                    Toast.makeText(getApplicationContext(), " please enter valid OTP & try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean restorePrefData() {
        return sessionManager.getBoolean(Constants.KEY_INTRO);
    }

    private void savePrefData() {

        sessionManager.putBoolean(Constants.KEY_INTRO, true);

    }

    private void lastScreen() {

        binding.introNext.setVisibility(View.INVISIBLE);
        binding.introStart.setVisibility(View.VISIBLE);
        binding.introIndicator.setVisibility(View.INVISIBLE);

        binding.introStart.setAnimation(btnAnim);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(Intro.this, MainActivity.class));
            finish();
        }
    }

    private boolean askpermissions() {
        List<String> listpermissionsneeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listpermissionsneeded.add(perm);
            }
        }
        if (!listpermissionsneeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listpermissionsneeded.toArray(new String[listpermissionsneeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String perName = entry.getKey();
                    int perResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, perName)) {
                        new AlertDialog.Builder(Intro.this).setTitle("Permission").setCancelable(false).setMessage("Please Provide the required permissions for using all the features")
                                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        askpermissions();
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    } else {
                        new AlertDialog.Builder(Intro.this).setTitle("PermissionDenied").setCancelable(false).setMessage("Permissions Denied").setCancelable(false).setMessage("You have Denied Some Permissions.Allow all permissions at[settings] > [permissions]")
                                .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setNegativeButton("No,Exit App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
                        break;
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission,RestrictedApi")
    private void getCurrentLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Double lat = location.getLatitude();
                        Double lng = location.getLongitude();
                        latitude = lat;
                        longitude = lng;
                        getdata(lat, lng);

                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                String lat = String.valueOf(location1.getLatitude());
                                String lng = String.valueOf(location1.getLongitude());

                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {

        }

    }


    private void getdata(Double lat, Double lng) {


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(Intro.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String addressLine = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getSubAdminArea();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.d("Intro", "onCreate: :::::::::::::::::Gps permission granted.." + state);

            binding.registerLayout.signUpState.setText(state);
            binding.registerLayout.signUpDistrict.setText(city);
            binding.registerLayout.signUpPincode.setText(postalCode);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}