package com.shrewd.poppydealers.views.fragment.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.adapters.LoginListAdapter;
import com.shrewd.poppydealers.databinding.FeedbackAlertBinding;
import com.shrewd.poppydealers.databinding.LoginAlertBinding;
import com.shrewd.poppydealers.databinding.ProfileFragmentBinding;
import com.shrewd.poppydealers.listeners.BackPressListener;
import com.shrewd.poppydealers.model.User;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.edit.EditProfile;
import com.shrewd.poppydealers.views.activity.intro.Intro;
import com.shrewd.poppydealers.views.activity.order.OrderPage;
import com.shrewd.poppydealers.views.activity.payment.PaymentPage;
import com.shrewd.poppydealers.views.activity.support.CommonView;
import com.shrewd.poppydealers.views.fragment.home.HomeFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements BackPressListener {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    public static BackPressListener listener;
    private final CompositeDisposable disposable = new CompositeDisposable();

    ProfileFragmentBinding binding;
    private FirebaseAuth mAuth;
    private LoginListAdapter loginListAdapter;
    private Dialog dialogView;
    private ProfileViewModel mViewModel;
    private SessionManager sessionManager;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        if (activity != null) {


            mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            mAuth=FirebaseAuth.getInstance();

            sessionManager = new SessionManager(activity);
            dialogView = new Dialog(activity, R.style.FullHeightDialog);
            dialogView.setCancelable(false);


            binding.profileSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    sessionManager.putBoolean(Constants.KEY_LOGIN, false);

                    startActivity(new Intent(activity, Intro.class));
                    activity.finish();
                }
            });

            binding.profileEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, EditProfile.class));
                }
            });

            binding.profileFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedBackAlert(activity);
                }
            });

            binding.profileChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProfileAlert(activity);
                }
            });
            binding.profileOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, OrderPage.class);
                    intent.putExtra(Constants.KEY_CLASS_TYPE, "profile");
                    startActivity(intent);
                }
            });
            binding.profilePayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PaymentPage.class);
                    intent.putExtra(Constants.KEY_CLASS_TYPE, "profile");
                    startActivity(intent);
                }
            });
            binding.profileSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CommonView.class);
                    startActivity(intent);
                }
            });


            mViewModel.getProfile(activity).observe(getViewLifecycleOwner(), new Observer<User>() {
                @Override
                public void onChanged(User user) {

                    binding.profileName.setText(user.getName());
                }
            });
        }
    }

    private void getProfileAlert(Activity activity) {

        List<User> users = new ArrayList<>();
        users.add(new User(819080, "Harikii", "active"));
        users.add(new User(819081, "The future", "pending"));
        users.add(new User(819082, "The revolution", "Inactive"));


        LoginAlertBinding alertBinding = LoginAlertBinding.inflate(LayoutInflater.from(activity));
        dialogView.setContentView(alertBinding.getRoot());


        if (dialogView.getWindow() != null) {
            dialogView.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialogView.getWindow().setGravity(Gravity.CENTER);

        }


        alertBinding.loginAlertClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });


        alertBinding.loginAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, EditProfile.class);
                intent.putExtra(Constants.KEY_CLASS_TYPE, "Add");
                startActivity(intent);

            }
        });


        User user = new User(819080, "Harikii", "Pending");
        alertBinding.setUser(user);

        alertBinding.loginRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        loginListAdapter = new LoginListAdapter(users, new LoginListAdapter.SelectedListener() {
            @Override
            public void onItemClick(User user) {


                if (!user.getStatus().equals("pending")) {

                    alertBinding.setUser(user);

                    loginListAdapter.changeProfile(user);
                } else {
                    Toast.makeText(activity, "Waiting for Approval", Toast.LENGTH_SHORT).show();
                }


            }
        });
        loginListAdapter.notifyDataSetChanged();
        alertBinding.loginRecycler.setAdapter(loginListAdapter);


        dialogView.show();

    }

    private void feedBackAlert(Activity activity) {

        FeedbackAlertBinding alertBinding = FeedbackAlertBinding.inflate(LayoutInflater.from(activity));
        dialogView.setContentView(alertBinding.getRoot());


        if (dialogView.getWindow() != null) {
            dialogView.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialogView.getWindow().setGravity(Gravity.CENTER);

        }

        alertBinding.feedBackCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });


        alertBinding.feedBackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = alertBinding.feedbackText.getText().toString();
                if (!message.isEmpty()) {
                    JSONObject jsonObject = new JSONObject();

                    try {

                        jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
                        jsonObject.put(Constants.KEY_TYPE, "dealer");
                        jsonObject.put(Constants.KEY_MESSAGE, message);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());


                    RxClient.getInstance()
                            .addFeedBack(bodyRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                    disposable.add(d);
                                }

                                @Override
                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {


                                    String message = String.valueOf(apiResponses.get("message"));
                                    message = message.replaceAll("\"", "");
                                    if (message.equalsIgnoreCase("Feedback Inserted!")) {
                                        dialogView.dismiss();
                                    }
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

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

                } else {
                    Toast.makeText(getActivity(), "Enter your FeedBack", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogView.show();


    }


    @Override
    public void onPause() {
        listener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener = this;
    }

    @Override
    public void onBackPressed() {
        listener = null;
        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
    }
}