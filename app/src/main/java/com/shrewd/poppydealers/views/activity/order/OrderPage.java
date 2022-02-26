package com.shrewd.poppydealers.views.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.shrewd.poppydealers.adapters.OrderAdapter;
import com.shrewd.poppydealers.databinding.ActivityOrderPageBinding;
import com.shrewd.poppydealers.databinding.FiltersLayoutBinding;
import com.shrewd.poppydealers.model.Order;
import com.shrewd.poppydealers.utilities.AndroidSQLiteData;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.LoadingProgress;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.MainActivity;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class OrderPage extends AppCompatActivity {
    private static final String TAG = OrderPage.class.getSimpleName();
    ActivityOrderPageBinding binding;
    MaterialDatePicker<Pair<Long, Long>> datePicker;
    private SessionManager sessionManager;
    private LoadingProgress loadingProgress;
    private OrderViewModel mViewModel;
    private OrderAdapter adapter;
    private AndroidSQLiteData sqLiteData;
    private String value = "";
    private String status = "default";
    private int count = 0;

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        sessionManager = new SessionManager(OrderPage.this);
        loadingProgress = LoadingProgress.getInstance();
        loadingProgress.ShowProgress(this, "please wait", false);

        binding.orderPageToolbar.commonTitle.setText("Orders History");
        binding.orderPageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ordersRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(Constants.KEY_CLASS_TYPE) != null) {
                if (bundle.getString(Constants.KEY_CLASS_TYPE).equals("pending") || bundle.getString(Constants.KEY_CLASS_TYPE).equals("completed")) {

                    status = bundle.getString(Constants.KEY_CLASS_TYPE);

                    setAdapter(bundle.getString(Constants.KEY_CLASS_TYPE), "", "", "");
                } else {
                    setAdapter("default", "", "", "");
                }

            }
        } else {
            setAdapter("default", "", "", "");
        }


        binding.orderSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBottomSheet("Sort");
            }
        });
        binding.orderFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBottomSheet("Filter");
            }
        });

        // TODO: Use the ViewModel

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        calendar.setTimeInMillis(today);


        CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();
        calendarConstraints.setValidator(DateValidatorPointBackward.now()).setEnd(calendar.getTimeInMillis());


        datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select From and To Date").setCalendarConstraints(calendarConstraints.build()).build();


    }


    private void getBottomSheet(String type) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OrderPage.this);

        FiltersLayoutBinding filtersBinding = FiltersLayoutBinding.inflate(LayoutInflater.from(OrderPage.this));
        bottomSheetDialog.setContentView(filtersBinding.getRoot());

        if (count == 1) {
            filtersBinding.filterButton1.setChecked(true);

        } else if (count == 2) {
            filtersBinding.filterButton2.setChecked(true);
        }


        filtersBinding.filterButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersBinding.filterButton1.setChecked(true);
                count = 1;
                value = "asc";
                setAdapter("default", "", "", "asc");
                bottomSheetDialog.dismiss();
            }
        });
        filtersBinding.filterButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersBinding.filterButton2.setChecked(true);
                count = 2;
                value = "desc";
                setAdapter("default", "", "", "desc");
                bottomSheetDialog.dismiss();
            }
        });


        if (type.equals("Sort")) {
            filtersBinding.sortLayout.setVisibility(View.VISIBLE);
            filtersBinding.filterLayout.setVisibility(View.GONE);
        } else {
            filtersBinding.filterLayout.setVisibility(View.VISIBLE);
            filtersBinding.sortLayout.setVisibility(View.GONE);
        }


        filtersBinding.filterTitle.setText(type);

        filtersBinding.statusFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    status = "default";
                } else if (position == 1) {
                    status = "pending";
                } else if (position == 2) {
                    status = "completed";
                } else if (position == 3) {
                    status = "cancel";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filtersBinding.filterDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "Material_Range");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Long startDate = selection.first;
                        Long endDate = selection.second;


                        filtersBinding.filterFromDate.setText(convertDate(String.valueOf(startDate), "yyyy-MM-dd"));
                        filtersBinding.filterToDate.setText(convertDate(String.valueOf(endDate), "yyyy-MM-dd"));


                    }
                });
            }
        });

        filtersBinding.filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                setAdapter(status, filtersBinding.filterFromDate.getText().toString(), filtersBinding.filterToDate.getText().toString(), value);

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(filtersBinding.getRoot());
        bottomSheetDialog.show();
    }

    private void setAdapter(String status, String from, String to, String value) {

        mViewModel.getOrders(OrderPage.this, status, from, to, value).observe(OrderPage.this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                loadingProgress.hideProgress();

                adapter = new OrderAdapter(orders,  new OrderAdapter.SelectedListener() {
                    @Override
                    public void onItemClick(Order item) {
                        Intent intent = new Intent(getApplicationContext(), OrderView.class);
                        intent.putExtra(Constants.KEY_ORDER_ID, item.getOrder_id());
                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();
                binding.ordersRecycler.setAdapter(adapter);

                if (orders.size() > 0) {
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.emptyAnimation.cancelAnimation();
                    binding.orderLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.orderLayout.setVisibility(View.GONE);
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.emptyAnimation.playAnimation();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String classType = bundle.getString(Constants.KEY_CLASS_TYPE);
            if (classType != null && classType.equals("profile")) {
                super.onBackPressed();
                finish();
            } else {
                Intent intent = new Intent(OrderPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(OrderPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
//            jsonObject.put(Constants.KEY_STATUS, status);
//            jsonObject.put(Constants.KEY_FROM_DATE, from);
//            jsonObject.put(Constants.KEY_TO_DATE, to);
//            jsonObject.put(Constants.KEY_VALUE, value);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JsonParser jsonParser = new JsonParser();
//        Gson gson = new Gson();
//        Call<JsonObject> call = RetrofitClient.getInstance().getMyApi().order((JsonObject) jsonParser.parse(jsonObject.toString()));
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                ApiResponse order = gson.fromJson(response.body(), ApiResponse.class);
//                if (order.getSuccess()==true) {
//
//
//                    Log.e(TAG,order.toString()+"");
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
