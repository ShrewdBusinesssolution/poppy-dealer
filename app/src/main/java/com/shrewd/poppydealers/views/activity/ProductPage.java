package com.shrewd.poppydealers.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.shrewd.poppydealers.adapters.SectionPagerAdapter;
import com.shrewd.poppydealers.databinding.ActivityProductPageBinding;
import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.model.Series;
import com.shrewd.poppydealers.utilities.LoadingProgress;
import com.shrewd.poppydealers.utilities.SessionManager;
import com.shrewd.poppydealers.views.activity.cart.Cart;
import com.shrewd.poppydealers.views.activity.cart.CartViewModel;
import com.shrewd.poppydealers.views.fragment.placeholder.PlaceHolderViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductPage extends AppCompatActivity {
    private static final String TAG = "ProductPage";
    public static List<Series> mList;
    ActivityProductPageBinding binding;
    private SessionManager sessionManager;
    private LoadingProgress loadingProgress;
    private PlaceHolderViewModel productViewModel;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sessionManager = new SessionManager(this);
        loadingProgress = LoadingProgress.getInstance();
        loadingProgress.ShowProgress(this, "please wait", false);
        productViewModel = new ViewModelProvider(this).get(PlaceHolderViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        binding.productPageToolbar.commonTitle.setText("Products");
        binding.productPageToolbar.cartLayout.setVisibility(View.VISIBLE);


        binding.productPageToolbar.cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Cart.class));
            }
        });
        List<CartItem> cartItems = sessionManager.cartRead(this);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            cartItems.clear();
        }
        cartViewModel.getCart(ProductPage.this).observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                binding.productPageToolbar.mainCartCount.setText(String.valueOf(cartItems.size()));
            }
        });


        binding.productPageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        productViewModel.getProducts("asc").observe(this, new Observer<List<Series>>() {
            @Override
            public void onChanged(List<Series> products) {
                mList = products;
                loadingProgress.hideProgress();

                SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), products);
                binding.viewpager.setAdapter(pagerAdapter);
                binding.tabs.setupWithViewPager(binding.viewpager);
                binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        });
//        binding.productPageFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    productViewModel.getProducts("asc").observe(ProductPage.this, new Observer<List<Series>>() {
//                        @Override
//                        public void onChanged(List<Series> products) {
//                            mList = products;
//                            loadingProgress.hideProgress();
//
//                            SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), products);
//                            binding.viewpager.setAdapter(pagerAdapter);
//                            binding.tabs.setupWithViewPager(binding.viewpager);
//                            binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
//                        }
//                    });
//                } else if (position == 1) {
//                    productViewModel.getProducts("desc").observe(ProductPage.this, new Observer<List<Series>>() {
//                        @Override
//                        public void onChanged(List<Series> products) {
//                            mList = products;
//                            loadingProgress.hideProgress();
//
//                            SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), products);
//                            binding.viewpager.setAdapter(pagerAdapter);
//                            binding.tabs.setupWithViewPager(binding.viewpager);
//                            binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

//        List<Product> series = new ArrayList<>();
//        series.add(new Product("GRAND", getGrand()));
//        series.add(new Product("PREMIUM", getPremium()));
//        series.add(new Product("MEDICO", getMedico()));
//        series.add(new Product("PU FORM", getPuFoam()));
//        series.add(new Product("RUBBERIZED COIR", getRubberizedCoir()));
//        series.add(new Product("LATEX", getLatex()));


//        SeriesPageAdapter adapter = new SeriesPageAdapter(getData(), new SeriesPageAdapter.SelectedListener() {
//            @Override
//            public void onItemClick(Product item) {
//                startActivity(new Intent(getApplicationContext(), ProductPage.class));
//            }
//        });
//
//        binding.homeRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//        binding.homeRecycler.setAdapter(adapter);


//    private List<Dummy> getGrand() {
//
//        List<Dummy> products = new ArrayList<>();
//
//        products.add(new Dummy("1", "grand master D.T", ".100* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg", ""));
//        products.add(new Dummy("2", "grandeur B.T", ".200* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hybrid/1.jpg", ""));
//        products.add(new Dummy("3", "Classique TT", ".300* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hrfoam/1.jpg", ""));
//        products.add(new Dummy("4", "Classique S.PT", ".400* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/latex/1.jpg", ""));
//        products.add(new Dummy("5", "Exuber TT", ".500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg", ""));
//        products.add(new Dummy("6", "Excuber S.PT", ".500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg", ""));
//        products.add(new Dummy("7", "Excuber ET", ".500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg", ""));
//
//        return products;
//    }
//
//    private List<Dummy> getPremium() {
//
//        List<Dummy> products = new ArrayList<>();
//
//        products.add(new Dummy("8", "Selene TT", ".100* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("9", "Selene PT", ".200* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hybrid/1.jpg"));
//        products.add(new Dummy("10", "Luxe TT", ".300* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hrfoam/1.jpg"));
//        products.add(new Dummy("11", "Luxe ET", ".400* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/latex/1.jpg"));
//        products.add(new Dummy("12", "Luxe PT", ".500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//
//        return products;
//    }
//
//
//    private List<Dummy> getMedico() {
//
//        List<Dummy> products = new ArrayList<>();
//
//        products.add(new Dummy("13", "The Guardianz BSMF TT", ".100* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("14", "The Guardianz BSMF ET", ".200* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hybrid/1.jpg"));
//        products.add(new Dummy("15", "The Guardianz BFMF TT", ".300* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hrfoam/1.jpg"));
//        products.add(new Dummy("16", "The Guardianz ET", ".400* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/latex/1.jpg"));
//        products.add(new Dummy("17", "Spine Shield TT", ".500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//
//        return products;
//    }
//
//
//    private List<Dummy> getLatex() {
//
//        List<Dummy> products = new ArrayList<>();
//
//        products.add(new Dummy("18", "Natur Art NL", ".100* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("19", "Natur Art HRNL NL", ".200* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hybrid/1.jpg"));
//        products.add(new Dummy("20", "Natur Art RCNL NL", ".300* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hrfoam/1.jpg"));
//
//        return products;
//    }
//
//
//    private List<Dummy> getPuFoam() {
//
//        List<Dummy> products = new ArrayList<>();
//
//        products.add(new Dummy("21", "Siesta TT", ".100* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("22", "Siesta ET", ".200* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hybrid/1.jpg"));
//        products.add(new Dummy("23", "Grace TT", ".300* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hrfoam/1.jpg"));
//        products.add(new Dummy("24", "Grace ET", ".400* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/latex/1.jpg"));
//        products.add(new Dummy("25", "Softy Plus", ".500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("26", "Softy", ".600* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("27", "Eco Soft", ".700* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("28", "Fluffy", ".800* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//
//        return products;
//    }
//
//    private List<Dummy> getRubberizedCoir() {
//
//        List<Dummy> products = new ArrayList<>();
//
//        products.add(new Dummy("29", "Saffron Dlx TT", ".100* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("30", "Saffron Dlx ET", ".200* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hybrid/1.jpg"));
//        products.add(new Dummy("31", "Saffron Plus ", ".300* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hrfoam/1.jpg"));
//        products.add(new Dummy("32", "Saffron", ".400* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/latex/1.jpg"));
//        products.add(new Dummy("33", "Desire", ".500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Dummy("34", "Access", ".600* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//
//        return products;
//    }


//    private List<Product> getData() {
//        List<Product> products = new ArrayList<>();
//
//        products.add(new Product("GRAND", "starts from ₹.100* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Product("PREMIUM", "starts from ₹.200* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hybrid/1.jpg"));
//        products.add(new Product("MEDICO", "starts from ₹.300* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/hrfoam/1.jpg"));
//        products.add(new Product("PU FORM", "starts from ₹.400* \n(Inclusive all Taxes)", "https://poppyindia.com/images/products/cuddle-buddy/latex/1.jpg"));
//        products.add(new Product("RUBBERIZED COIR", "starts from ₹.500* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//        products.add(new Product("LATEX", "starts from ₹.600* \n(Inclusive all Taxes)", "https://poppyindia.com/images/main-slider/8447_deskc.jpg"));
//
//        return products;
//    }