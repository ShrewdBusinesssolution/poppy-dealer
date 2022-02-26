package com.shrewd.poppydealers.views.activity.order;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.adapters.OrderViewListAdapter;
import com.shrewd.poppydealers.databinding.ActivityOrderViewBinding;
import com.shrewd.poppydealers.model.Customize;
import com.shrewd.poppydealers.model.CustomizeItem;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;
import com.shrewd.poppydealers.utilities.SessionManager;

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

public class OrderView extends AppCompatActivity {
    private static final String TAG = OrderView.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    ActivityOrderViewBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.orderPageToolbar.commonTitle.setText("Order View");
        binding.orderPageToolbar.commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        sessionManager = new SessionManager(OrderView.this);


        binding.orderViewRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
                jsonObject.put(Constants.KEY_ORDER_ID, bundle.getString(Constants.KEY_ORDER_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }


            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

            RxClient.getInstance()
                    .getOrderView(bodyRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {


                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                            String data = String.valueOf(apiResponses.get("data"));
                            try {
                                JSONObject jsonData = new JSONObject(data);

                                String order_date = String.valueOf(jsonData.get("order_date"));
                                String gst = String.valueOf(jsonData.get("gst"));
                                String shop_name = String.valueOf(jsonData.get("shop_name"));
                                String shop_location = String.valueOf(jsonData.get("shop_location"));

                                binding.orderDate.setText(order_date);
                                binding.orderShopName.setText(shop_name);
                                binding.orderShopAddress.setText(shop_location);

                                String product = String.valueOf(jsonData.get("product"));

                                JSONArray jsonProduct = new JSONArray(product);
                                List<Product> products = getProductArray(jsonProduct);


                                String product_customise = String.valueOf(jsonData.get("product_customise"));
                                JSONArray jsonProductCustomize = new JSONArray(product_customise);
                                List<Customize> customize = getCustomizeArray(jsonProductCustomize);


                                OrderViewListAdapter adapter = new OrderViewListAdapter(OrderView.this, products, customize);
                                binding.orderViewRecycler.setAdapter(adapter);

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


    }

    private List<Customize> getCustomizeArray(JSONArray jsonProductCustomize) throws JSONException {

        List<Customize> products = new ArrayList<>();
        for (int i = 0; i < jsonProductCustomize.length(); i++) {

            JSONObject object = jsonProductCustomize.getJSONObject(i);
            String product_id = object.getString("product_id");
            String product_list = String.valueOf(object.get("product_list"));

            JSONArray array = new JSONArray(product_list);

            List<CustomizeItem> list = new ArrayList<>();
            for (int j = 0; j < array.length(); j++) {
                JSONObject objectList = array.getJSONObject(j);

                list.add(new CustomizeItem(objectList.getInt("group_id"), objectList.getInt("group_count"), objectList.getString("inches"), objectList.getString("color"), objectList.getString("color_code"), objectList.getString("size")));
            }

            products.add(new Customize(product_id, list));

        }

        return products;
    }

    private List<Product> getProductArray(JSONArray jsonArray) throws JSONException {

        List<Product> products = new ArrayList<>();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject object = jsonArray.getJSONObject(j);

            products.add(new Product(object.getString("product_id"), object.getString("product_name"), object.getString("product_rate"), object.getString("product_image"), object.getString("product_type"), object.getString("product_quantity")));
        }

        return products;
    }
}

