package com.shrewd.poppydealers.views.activity.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.adapters.CartListAdapter;
import com.shrewd.poppydealers.databinding.ActivityCartBinding;
import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.model.Customize;
import com.shrewd.poppydealers.model.CustomizeItem;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Cart extends AppCompatActivity {

    private static final String TAG = Cart.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();

    ActivityCartBinding binding;
    private SessionManager sessionManager;
    private CartViewModel cartViewModel;
    private List<Customize> customizeList;
    private CartListAdapter adapter;
    private String userId, userMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        sessionManager = new SessionManager(this);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        userMobile = sessionManager.getString(Constants.KEY_USER_MOBILE);
        userId = sessionManager.getString(Constants.DEALER_ID);

        binding.recyclerLayout.setVisibility(View.GONE);
        binding.emptyLayout.setVisibility(View.VISIBLE);
        binding.emptyAnimation.playAnimation();

        binding.cartPageToolbar.commonTitle.setText("Cart");
        binding.cartPageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        dataset();


        binding.cartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                int total = Integer.parseInt(binding.orderDetailsTotalCost.getText().toString().replaceAll("[^0-9]", ""));

                try {

                    jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
                    jsonObject.put(Constants.DEALER_ID, sessionManager.getString(Constants.DEALER_ID));
                    jsonObject.put(Constants.KEY_TOTAL_AMOUNT, total);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());


                RxClient.getInstance()
                        .orderAdd(bodyRequest)
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

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                cartViewModel.getCart(Cart.this);
                                dataset();
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
        });

    }

    private void dataset() {

        customizeList = sessionManager.customizeRead(this);
        if (customizeList == null) {
            customizeList = new ArrayList<>();
            customizeList.clear();
        }
        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        cartViewModel.getCart(Cart.this).observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {

                if (cartItems.size() > 0) {
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.emptyAnimation.cancelAnimation();
                    binding.recyclerLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerLayout.setVisibility(View.GONE);
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.emptyAnimation.playAnimation();
                }

                adapter = new CartListAdapter(Cart.this, cartItems, customizeList, new CartListAdapter.CartInterface() {


                    @Override
                    public void onPlusClick(int quantity, CartItem cartItem) {
                        CustomizeItem item = new CustomizeItem();
                        addTocart(quantity, cartItem.getProduct(), 0, item);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onMinusClick(int quantity, CartItem cartItem) {
                        CustomizeItem item = new CustomizeItem();
                        subTocart(quantity, cartItem.getProduct(), 0, item);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDelete(CartItem cartItem) {
                        removeCart(0, cartItem.getProduct(), cartItem.getProduct().getProduct_type());
                        cartViewModel.cartRepo.removeItemFromCart(cartItem);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void nestedAdd(int pro_qua, Product product, int cus_qua, CustomizeItem item) {
                        addTocart(pro_qua, product, cus_qua, item);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void nestedSub(int pro_qua, Product product, int cus_qua, CustomizeItem item) {
                        subTocart(pro_qua, product, cus_qua, item);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void nestedRemove(int pro_qua, Product product, int cus_qua, CustomizeItem item) {

                    }
                });

                binding.cartRecycler.setAdapter(adapter);

            }
        });

        cartViewModel.getTotalPrice().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                int val = integer * 18 / 100;
                int sum = integer + val;

                binding.orderDetailsSubTotal.setText("₹ " + integer);
                binding.orderDetailsGstTax.setText("₹ " + val);
                binding.orderDetailsTotalCost.setText("₹ " + sum);
            }
        });
    }


    private void addTocart(int pro_qua, Product product, int cus_qua, CustomizeItem item) {

        cartViewModel.addItemToCart(product,1);

        JSONObject jsonObject = new JSONObject();

        if (product.getProduct_type().equals("1")) {
            try {

                jsonObject.put(Constants.KEY_PRODUCT_ID, product.getProduct_id());
                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, pro_qua + 1);
                jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
                jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, item.getGroup_id());
                jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, cus_qua);
                jsonObject.put(Constants.KEY_PRODUCT_INCHES, item.getInches());
                jsonObject.put(Constants.KEY_PRODUCT_SIZE, item.getSize());
                jsonObject.put(Constants.KEY_PRODUCT_COLOR, item.getColor());
                jsonObject.put(Constants.KEY_PRODUCT_COLOR_CODE, item.getColor_code());
                jsonObject.put(Constants.DEALER_ID, userId);
                jsonObject.put(Constants.KEY_PRODUCT_TYPE, product.getProduct_type());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Cart-Data::::-" + jsonObject.toString());

        } else {
            try {

                jsonObject.put(Constants.KEY_PRODUCT_ID, product.getProduct_id());
                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, pro_qua);
                jsonObject.put(Constants.KEY_USER_MOBILE, userMobile);
                jsonObject.put(Constants.DEALER_ID, userId);
                jsonObject.put(Constants.KEY_PRODUCT_TYPE, product.getProduct_type());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .addCart(bodyRequest)
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
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        cartViewModel.getCart(Cart.this);

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

    private void subTocart(int pro_qua, Product product, int cus_qua, CustomizeItem item) {

        cartViewModel.subItemToCart(product);

//      Log.d(TAG,"Cart-Data::::-"+" product_id:-"+product.getProduct_id()+", product_quantity:-"+String.valueOf(pro_qua-1)+", product_group_id:-"+item.getGroup_id()+", product_group_count:-"+cus_qua+", product_type:-"+product.getProduct_type());

        JSONObject jsonObject = new JSONObject();

        if (product.getProduct_type().equals("1")) {
            try {

                jsonObject.put(Constants.KEY_PRODUCT_ID, product.getProduct_id());
                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, pro_qua - 1);
                jsonObject.put(Constants.DEALER_ID, userId);
                jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, item.getGroup_id());
                jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, cus_qua);
                jsonObject.put(Constants.KEY_PRODUCT_TYPE, product.getProduct_type());


            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Cart-Data::::-" + jsonObject.toString());

        } else {
            try {

                jsonObject.put(Constants.KEY_PRODUCT_ID, product.getProduct_id());
                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, pro_qua);
                jsonObject.put(Constants.DEALER_ID, userId);
                jsonObject.put(Constants.KEY_PRODUCT_TYPE, product.getProduct_type());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());


        RxClient.getInstance()
                .subCart(bodyRequest)
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

                        if (!message.equalsIgnoreCase("Quantity Decreased!")) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                        cartViewModel.getCart(Cart.this);

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

    private void removeCart(int quantity, Product item, String type) {

        JSONObject jsonObject = new JSONObject();

        cartViewModel.subItemToCart(item);

        if (type.equalsIgnoreCase("customize")) {

            if (customizeList.size() > 0) {
                for (int i = 0; i < customizeList.size(); i++) {

                    if (Integer.parseInt(customizeList.get(i).getProduct_id()) == Integer.parseInt(item.getProduct_id())) {

                        try {
                            jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                            jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, quantity - 1);
                            jsonObject.put(Constants.DEALER_ID, sessionManager.getString(Constants.DEALER_ID));
                            jsonObject.put(Constants.KEY_PRODUCT_GROUP_ID, customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getGroup_id());
                            jsonObject.put(Constants.KEY_PRODUCT_GROUP_COUNT, customizeList.get(i).getList().get(customizeList.get(i).getList().size() - 1).getGroup_count());
                            jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                }

            }
        } else {
            try {

                jsonObject.put(Constants.KEY_PRODUCT_ID, item.getProduct_id());
                jsonObject.put(Constants.KEY_PRODUCT_QUANTITY, quantity);
                jsonObject.put(Constants.DEALER_ID, sessionManager.getString(Constants.DEALER_ID));
                jsonObject.put(Constants.KEY_PRODUCT_TYPE, item.getProduct_type());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CallRequest(jsonObject);
    }


    private void CallRequest(JSONObject jsonObject) {
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());


        RxClient.getInstance()
                .subCart(bodyRequest)
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
                        if (!message.equalsIgnoreCase("Quantity Decreased!")) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                        cartViewModel.getCart(Cart.this);
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
    public void onResume() {
        super.onResume();
        if (binding.cartRecycler != null) {
            dataset();
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        cartViewModel.getCart(Cart.this);
//    }
}


//        JsonParser jsonParser=new JsonParser();
//
//        Call<JsonObject> call=RetrofitClient.getInstance().getMyApi().cartSub( (JsonObject) jsonParser.parse(jsonObject.toString()));
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });