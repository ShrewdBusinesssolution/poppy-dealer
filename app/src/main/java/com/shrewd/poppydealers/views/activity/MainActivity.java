package com.shrewd.poppydealers.views.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.ActivityMainBinding;
import com.shrewd.poppydealers.databinding.PendingAlertBinding;
import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.cart.Cart;
import com.shrewd.poppydealers.views.activity.cart.CartViewModel;
import com.shrewd.poppydealers.views.activity.order.OrderPage;
import com.shrewd.poppydealers.views.activity.order.OrderViewModel;
import com.shrewd.poppydealers.views.activity.payment.PaymentPage;
import com.shrewd.poppydealers.views.activity.support.CommonView;
import com.shrewd.poppydealers.views.fragment.home.HomeFragment;
import com.shrewd.poppydealers.views.fragment.notification.NotificationFragment;
import com.shrewd.poppydealers.views.fragment.profile.ProfileFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ActivityMainBinding binding;
    private SessionManager sessionManager;
    private OrderViewModel orderViewModel;
    private CartViewModel cartViewModel;
    private Dialog dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        sessionManager = new SessionManager(this);
        dialogView = new Dialog(MainActivity.this, R.style.FullHeightDialog);
        dialogView.setCancelable(false);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);


        if (savedInstanceState == null) {
            binding.bottomNavigation.show(3, true);


            if (!sessionManager.getBoolean(Constants.KEY_ALERT)) {
                getPendingAlert();
            }

//            binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        }

//        binding.bottomNavigation.setOnNavigationItemSelectedListener(navlistener);


        binding.toolbar.mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        cartViewModel.getCart(MainActivity.this).observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                binding.toolbar.mainCartCount.setText(String.valueOf(cartItems.size()));
            }
        });

        binding.toolbar.mainCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Cart.class));
            }
        });

        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.bed));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.orders));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.home));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.notification));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.user));


        binding.bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                Fragment fragment = null;

                switch (item.getId()) {
                    case 1:
                        startActivity(new Intent(MainActivity.this, ProductPage.class));
                        finish();
                        return;
                    case 2:
                        startActivity(new Intent(MainActivity.this, OrderPage.class));
                        finish();
                        return;
                    case 3:
                        fragment = new HomeFragment();
                        break;
                    case 4:
                        fragment = new NotificationFragment();
                        break;
                    case 5:
                        fragment = new ProfileFragment();
                        break;
                }
                loadFragment(fragment);

            }
        });
//        binding.bottomNavigation.setCount(2, "10");


        binding.bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if (item.getId() == 1) {

                } else if (item.getId() == 2) {

                } else if (item.getId() == 3) {

                } else if (item.getId() == 4) {

                }
            }
        });

        binding.bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                if (item.getId() == 1) {

                } else if (item.getId() == 2) {

                } else if (item.getId() == 3) {

                } else if (item.getId() == 4) {

                }
            }
        });

        binding.toolbar.mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.sideNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);

                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.side_home:

                        binding.bottomNavigation.show(3, true);

                        break;

                    case R.id.side_product:

                        binding.bottomNavigation.show(1, true);

                        return true;

                    case R.id.side_payment_history:
                        startActivity(new Intent(MainActivity.this, PaymentPage.class));
                        return true;

                    case R.id.side_order_history:

                        startActivity(new Intent(MainActivity.this, OrderPage.class));
                        return true;


                    case R.id.side_share:
                        Toast.makeText(getApplicationContext(), "Under Construction", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.side_support:
                        startActivity(new Intent(MainActivity.this, CommonView.class));
                        return true;


                }

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                return true;
            }
        });
    }





    private void getPendingAlert() {

        PendingAlertBinding alertBinding = PendingAlertBinding.inflate(LayoutInflater.from(MainActivity.this));
        dialogView.setContentView(alertBinding.getRoot());


        if (dialogView.getWindow() != null) {
            dialogView.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialogView.getWindow().setGravity(Gravity.CENTER);

        }

        alertBinding.pendingAlertClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.putBoolean(Constants.KEY_ALERT, true);

                dialogView.dismiss();


            }
        });

        Glide.with(MainActivity.this).asGif()
                .load(R.drawable.animation)
                .into(alertBinding.pendingImage);

        alertBinding.pendingUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();


            }
        });

        dialogView.show();

    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, fragment).commit();

    }

    @Override
    public void onBackPressed() {

        if (NotificationFragment.listener != null) {
            NotificationFragment.listener.onBackPressed();
            binding.bottomNavigation.show(3, true);
        } else if (ProfileFragment.listener != null) {
            ProfileFragment.listener.onBackPressed();
            binding.bottomNavigation.show(3, true);
        } else {
            super.onBackPressed();
            finish();
        }
    }

//    @SuppressLint("NewApi")
//    private final BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment = null;
//            switch (item.getItemId()) {
//                case R.id.bottom_home:
//
//                    fragment = new HomeFragment();
//                    break;
//
//                case R.id.bottom_notification:
//                    Toast.makeText(getApplicationContext(), "under construction!", Toast.LENGTH_SHORT).show();
//                    return true;
//
//                case R.id.bottom_orders:
//
//                    Toast.makeText(getApplicationContext(), "under construction!", Toast.LENGTH_SHORT).show();
//                    return true;
//
//                case R.id.bottom_profile:
//
//                    Toast.makeText(getApplicationContext(), "under construction!", Toast.LENGTH_SHORT).show();
//                    return true;
//
//            }
//            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, fragment).commit();
//
//            return true;
//        }
//    };
}