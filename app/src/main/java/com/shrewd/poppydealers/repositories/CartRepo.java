package com.shrewd.poppydealers.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.model.CartItem;
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

public class CartRepo {
    private static final String TAG = "CartRepo";
    public static MutableLiveData<List<CartItem>> mutableCart = new MutableLiveData<>();
    public static MutableLiveData<Integer> mutableTotalPrice = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private Context context;
    private SessionManager sessionManager;

    public LiveData<List<CartItem>> getCart(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);

        initCart();

        return mutableCart;
    }

    public void initCart() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_USER_MOBILE, sessionManager.getString(Constants.KEY_USER_MOBILE));
            jsonObject.put(Constants.DEALER_ID, sessionManager.getString(Constants.DEALER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        RxClient.getInstance()
                .getCart(bodyRequest)
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

                            if (!jsonData.has("product") && !jsonData.has("product_list")) {
                                sessionManager.cartClear(context);
                                sessionManager.customizeClear(context);

                                List products = new ArrayList();
                                mutableCart.setValue(products);

                            } else if (!jsonData.has("product")) {
                                sessionManager.cartClear(context);

                                List products = new ArrayList();
                                mutableCart.setValue(products);

                            } else if (!jsonData.has("product_customise")) {
                                sessionManager.customizeClear(context);
                            }
                           if (jsonData.has("product")){
                               String product = String.valueOf(jsonData.get("product"));

                               JSONArray jsonProduct = new JSONArray(product);
                               List<CartItem> products = getProductArray(jsonProduct);

                               mutableCart.setValue(products);
                               calculateCartTotal();

                               sessionManager.cartWrite(context, products);
                           }

                            if (jsonData.has("product_customise")){
                                String product_customise = String.valueOf(jsonData.get("product_customise"));
                                JSONArray jsonProductCustomize = new JSONArray(product_customise);
                                List<Customize> customize = getCustomizeArray(jsonProductCustomize);

                                sessionManager.customizeWrite(context, customize);
                            }



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

    private List<CartItem> getProductArray(JSONArray jsonArray) throws JSONException {

        List<CartItem> products = new ArrayList<>();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject object = jsonArray.getJSONObject(j);

            Product product = new Product(object.getString("product_id"), object.getString("product_name"), object.getString("product_rate"), object.getString("product_image"), object.getString("product_type"));
            products.add(new CartItem(product, Integer.parseInt(object.getString("product_quantity"))));
        }

        return products;
    }

    private void calculateCartTotal() {
        if (mutableCart.getValue() == null) return;

        int total = 0;
        List<CartItem> cartItemList = mutableCart.getValue();
        for (CartItem cartItem : cartItemList) {
            int val = cartItem.getProduct().getProduct_price();


            total += val * cartItem.getQuantity();
        }
        mutableTotalPrice.setValue(total);
    }

    public LiveData<Integer> getTotalPrice() {
        if (mutableTotalPrice.getValue() == null) {
            mutableTotalPrice.setValue(0);
        }
        return mutableTotalPrice;
    }


    public boolean addItemToCart(Product dummy, int quantity) {

        if (mutableCart.getValue() == null) {
            initCart();
        }
            List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
            for (CartItem cartItem : cartItemList) {
                if (cartItem.getProduct().getProduct_id().equals(dummy.getProduct_id())) {

                    int index = cartItemList.indexOf(cartItem);
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    cartItemList.set(index, cartItem);

                    mutableCart.setValue(cartItemList);
                    calculateCartTotal();


                    return true;
                }
            }
            CartItem cartItem = new CartItem(dummy, quantity);
            cartItemList.add(cartItem);
            mutableCart.setValue(cartItemList);






        return true;
    }

    public boolean subItemToCart(Product dummy) {

        if (mutableCart.getValue() == null) {
            initCart();
        }

        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
        for (CartItem cartItem : cartItemList) {
            if (cartItem.getProduct().getProduct_id().equals(dummy.getProduct_id())) {
                int index = cartItemList.indexOf(cartItem);
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemList.set(index, cartItem);


                mutableCart.setValue(cartItemList);
                calculateCartTotal();


                return true;
            }
        }

        return true;
    }

    public void removeItemFromCart(CartItem cartItem) {

        if (mutableCart.getValue() == null) {
            return;
        }


        List<CartItem> cartItemList = new ArrayList<>(mutableCart.getValue());
        cartItemList.remove(cartItem);

        mutableCart.setValue(cartItemList);
        calculateCartTotal();

    }
}