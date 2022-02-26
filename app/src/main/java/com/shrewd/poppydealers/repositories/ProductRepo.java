package com.shrewd.poppydealers.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.shrewd.poppydealers.model.BedSizeModal;
import com.shrewd.poppydealers.model.CustomModal;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.model.Series;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductRepo {
    private static final String TAG = "ProductRepo";
    public static MutableLiveData<List<Series>> mutableProductList = new MutableLiveData<>();
    public static MutableLiveData<List<CustomModal>> mutableCustomList = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    public LiveData<List<Series>> getProducts(String sort) {
        loadProducts(sort);
        return mutableProductList;
    }

    public LiveData<List<CustomModal>> GetCustoms(int ids, String catoegory_id, String modalAccessories, String dimesion) {
        mutableCustomList= GetCutomList(ids,catoegory_id,modalAccessories,dimesion);
        return mutableCustomList;
    }

    private MutableLiveData<List<CustomModal>> GetCutomList(int ids, String catoegory_id, String modalAccessories, String dim) {



        RxClient.getInstance()
                .getProductCustomizes(catoegory_id,String.valueOf(ids),modalAccessories,dim)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        String json = String.valueOf(apiResponses.get("product_data"));
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            List<CustomModal> Custome = GetCustomJsonArray(jsonObject);


                            mutableCustomList.postValue(Custome);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError" + e);
                        mutableCustomList.postValue(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return mutableCustomList;
    }

    private List<CustomModal> GetCustomJsonArray(JSONObject jsonObject) throws JSONException {
        Log.e("TAG", "GetJsonArray: "+jsonObject);
        Iterator<String> keys = jsonObject.keys();
        List<CustomModal> CustomList= new ArrayList<>();
        List<BedSizeModal> BedSize= new ArrayList<>();
        List<BedSizeModal> Dimension= new ArrayList<>();
        List<BedSizeModal> Thickness= new ArrayList<>();
        JSONArray size=jsonObject.getJSONArray("bed_type");
        JSONArray Dimensions=jsonObject.getJSONArray("dimension");
        try{

            if (jsonObject.has("thickness")){

            JSONArray thickness=jsonObject.getJSONArray("thickness");
            for (int k =0;k<thickness.length();k++){
                JSONObject gobject= thickness.getJSONObject(k);
                Thickness.add(new BedSizeModal(gobject.getString("thickness"),0));
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j =0;j<size.length();j++){
            JSONObject gobject= size.getJSONObject(j);
            BedSize.add(new BedSizeModal(gobject.getString("type"),0));
        }
        for (int l =0;l<Dimensions.length();l++){
            JSONObject gobject= Dimensions.getJSONObject(l);
            Dimension.add(new BedSizeModal(gobject.getString("dimension"),0));
        }


        CustomList.add(new CustomModal(Dimension,BedSize,Thickness) );

//        }

        return CustomList;
    }


    private void loadProducts(String sort) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_SORT, sort);
        } catch (Exception e) {
            e.printStackTrace();
        }



        RxClient.getInstance()
                .getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull JsonObject apiResponses) {

                        String json = String.valueOf(apiResponses.get("Product_data"));
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            List<Series> products = GetJsonArray(jsonObject);


                            mutableProductList.setValue(products);


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

    private List<Series> GetJsonArray(JSONObject jsonObject) throws JSONException {

        Iterator<String> keys = jsonObject.keys();
        List<Series> series = new ArrayList<>();

        while (keys.hasNext()) {
            String seriesName = keys.next();
            JSONArray object = jsonObject.getJSONArray(seriesName);

            List<Product> products = new ArrayList<>();
            for (int j = 0; j < object.length(); j++) {
                JSONObject gobject = object.getJSONObject(j);
                JSONArray jsonImage = gobject.getJSONArray("product_imageurl");


                products.add(new Product(gobject.getString("category_id"), gobject.getString("product_id"), gobject.getString("product_type"), gobject.getString("product_name"), gobject.getString("product_price"), jsonImage));


            }
            series.add(new Series(seriesName, products));
        }

        return series;
    }


}