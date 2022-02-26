package com.shrewd.poppydealers.views.activity.UserPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shrewd.poppydealers.adapters.LoginListAdapter;
import com.shrewd.poppydealers.databinding.ActivityUserPageBinding;
import com.shrewd.poppydealers.model.User;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.views.activity.order.OrderPage;

import java.util.ArrayList;
import java.util.List;

public class UserPage extends AppCompatActivity {

    ActivityUserPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.userPageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String classType = bundle.getString(Constants.KEY_CLASS_TYPE);
            if (classType != null & classType.equals("Dealer")) {
                binding.userPageToolbar.commonTitle.setText("Dealers");

            } else {
                binding.userPageToolbar.commonTitle.setText("Marketing Staffs");


            }
        }
        List<User> users = new ArrayList<>();
        users.add(new User(819080, "Harikii", "completed"));
        users.add(new User(819081, "Mahe", "pending"));
        users.add(new User(819082, "Jawa", "completed"));
        users.add(new User(819082, "Jeeva", "pending"));
        users.add(new User(819082, "Ram", "completed"));
        users.add(new User(819082, "Karthi", "pending"));


        binding.userRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LoginListAdapter loginListAdapter = new LoginListAdapter(users, new LoginListAdapter.SelectedListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent = new Intent(getApplicationContext(), OrderPage.class);
                intent.putExtra(Constants.KEY_CLASS_TYPE, "profile");
                startActivity(intent);

            }
        });
        loginListAdapter.notifyDataSetChanged();
        binding.userRecycler.setAdapter(loginListAdapter);

    }
}