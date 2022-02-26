package com.shrewd.poppydealers.views.activity.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.adapters.PaymentAdapter;
import com.shrewd.poppydealers.databinding.ActivityPaymentPageBinding;
import com.shrewd.poppydealers.model.Payment;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.MainActivity;

import java.util.List;

public class PaymentPage extends AppCompatActivity {

    ActivityPaymentPageBinding binding;
    List<Payment> products;
    private SessionManager sessionManager;
    private PaymentViewModel mViewModel;
    private PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        mViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        binding.paymentPageToolbar.commonTitle.setText("Payment History");
        binding.paymentPageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.paymentRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mViewModel.getPayments(PaymentPage.this).observe(PaymentPage.this, new Observer<List<Payment>>() {
            @Override
            public void onChanged(List<Payment> payments) {
                adapter = new PaymentAdapter(payments, new PaymentAdapter.SelectedListener() {
                    @Override
                    public void onItemClick(Payment item) {
//                        startActivity(new Intent(getApplicationContext(), OrderView.class));
                    }
                });
                adapter.notifyDataSetChanged();
                binding.paymentRecycler.setAdapter(adapter);
            }
        });
        if (sessionManager.getString(Constants.KEY_CREDIT_AMOUNT) != null) {
            binding.paymentCredit.setVisibility(View.VISIBLE);
            binding.paymentCredit.setText("credit: ₹" + sessionManager.getString(Constants.KEY_CREDIT_AMOUNT).replaceAll("\"", ""));
        } else {
            binding.paymentCredit.setVisibility(View.GONE);
        }


//        products = new ArrayList<>();
//
//        products.add(new Payment("#Poppy6124", "05-10-21", "10 products", 14799, "paid"));
//        products.add(new Payment("#Poppy6123", "04-10-21", "11 products", 13799, "paid"));
//        products.add(new Payment("#Poppy6122", "03-10-21", "12 products", 12799, "unpaid"));
//        products.add(new Payment("#Poppy6121", "02-10-21", "13 products", 11799, "unpaid"));
//        products.add(new Payment("#Poppy6120", "01-10-21", "14 products", 10799, "unpaid"));
//        products.add(new Payment("#Poppy6119", "30-09-21", "15 products", 9799, "unpaid"));


    }

    @Override
    public void onBackPressed() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String classType = bundle.getString(Constants.KEY_CLASS_TYPE);
            if (classType != null && classType.equals("profile")) {
                super.onBackPressed();
                finish();
            }
        } else {
            Intent intent = new Intent(PaymentPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}