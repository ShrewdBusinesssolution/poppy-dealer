package com.shrewd.poppydealers.views.fragment.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.adapters.OrderAdapter;
import com.shrewd.poppydealers.databinding.HomeFragmentBinding;
import com.shrewd.poppydealers.model.Order;
import com.shrewd.poppydealers.model.User;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.LoadingProgress;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.order.OrderPage;
import com.shrewd.poppydealers.views.activity.order.OrderView;
import com.shrewd.poppydealers.views.fragment.profile.ProfileViewModel;

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

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();

    HomeFragmentBinding binding;
    private LoadingProgress loadingProgress;
    private SessionManager sessionManager;
    private ProfileViewModel profileViewModel;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {

            loadingProgress = LoadingProgress.getInstance();
            sessionManager = new SessionManager(activity);

            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

            profileViewModel.getProfile(activity).observe(getViewLifecycleOwner(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    binding.homeName.setText("Hi " + user.getName());
                }
            });

            getHomeData();

            binding.homePendingOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderPage.class);
                    intent.putExtra(Constants.KEY_CLASS_TYPE, "pending");
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            binding.homeCompletedOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderPage.class);
                    intent.putExtra(Constants.KEY_CLASS_TYPE, "completed");
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            binding.homeTotalOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderPage.class);
                    intent.putExtra(Constants.KEY_CLASS_TYPE, "all");
                    startActivity(intent);
                    getActivity().finish();
                }
            });

        }
    }

    private void getHomeData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
            jsonObject.put(Constants.DEALER_ID, sessionManager.getString(Constants.DEALER_ID));
            jsonObject.put(Constants.VERSION, sessionManager.getString(Constants.VERSION));
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .getHome(bodyRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {

                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        Boolean is_update_needed = apiResponses.get("is_update_needed").getAsBoolean();

                        if (is_update_needed) {
                            if(getActivity()!=null){
                                new AlertDialog.Builder(getActivity()).setTitle("App Update!").setCancelable(false).setMessage("Please Update to Access new Features")
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                final String appPackageName = getActivity().getPackageName(); // package name of the app
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        }).create().show();
                            }

                        }

                        String data = String.valueOf(apiResponses.get("data"));
                        String recent_orders = String.valueOf(apiResponses.get("recent_orders"));
                        try {
                            JSONObject jsonData = new JSONObject(data);

                            String total_orders = jsonData.getString("total_orders");
                            String pending_orders = jsonData.getString("pending_orders");
                            String completed_orders = jsonData.getString("completed_orders");
                            String credit_amount = jsonData.getString("credit_amount");


                            binding.homeOrderCount.setText(total_orders);
                            binding.homePendingOrderCount.setText(pending_orders);
                            binding.homeCompletedOrderCount.setText(completed_orders);
                            binding.homeCredit.setText(credit_amount);

                            JSONArray jsonObject = new JSONArray(recent_orders);
                            List<Order> orders = GetJsonArray(jsonObject);


                            if (orders.size() > 0) {
                                binding.homeRecyclerTitle.setVisibility(View.VISIBLE);
                                binding.homeRecycler.setVisibility(View.VISIBLE);
                            } else {
                                binding.homeRecyclerTitle.setVisibility(View.GONE);
                                binding.homeRecycler.setVisibility(View.GONE);
                            }

                            OrderAdapter adapter = new OrderAdapter(orders, new OrderAdapter.SelectedListener() {
                                @Override
                                public void onItemClick(Order item) {
                                    Intent intent = new Intent(getActivity(), OrderView.class);
                                    intent.putExtra(Constants.KEY_ORDER_ID, item.getOrder_id());
                                    startActivity(intent);
                                }
                            });

                            binding.homeRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                            binding.homeRecycler.setHasFixedSize(true);
                            binding.homeRecycler.setAdapter(adapter);

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