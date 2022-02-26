package com.shrewd.poppydealers.views.fragment.placeholder;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shrewd.poppydealers.model.CustomModal;
import com.shrewd.poppydealers.model.Series;
import com.shrewd.poppydealers.repositories.ProductRepo;
import com.shrewd.poppydealers.rxjava.RxClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceHolderViewModel extends ViewModel {

    MutableLiveData<> indexName = new MutableLiveData<>();
    ProductRepo productRepo = new ProductRepo();
    ProductRepo CustomRepo = new ProductRepo();
    private final CompositeDisposable disposable = new CompositeDisposable();
    public static MutableLiveData <String> mCuctomrate=null;
    private final LiveData<Integer> indexNames = Transformations.map(indexName, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return input;
        }
    });


    public LiveData<List<Series>> getProducts(String sort) {
        return productRepo.getProducts(sort);
    }
    public LiveData<List<CustomModal>> GetCustomModel(int ids, String catoegory_id, String modalAccessories, String Dimension) {
        return CustomRepo.GetCustoms(ids,catoegory_id,modalAccessories,Dimension);
    }
    public LiveData<String> InitGetCUSRate(int ids, String catoegoryId, String bedType, String Dimension,String thickness) {
        mCuctomrate = new MutableLiveData<>();
        mCuctomrate= getInitCutomrate(ids,catoegoryId,bedType,Dimension,thickness);
        return mCuctomrate;
    }

    private MutableLiveData<String> getInitCutomrate(int ids, String catoegoryId, String bedType, String dimension, String thickness) {

        RxClient.getInstance()
                .getProductCustomizePrize(String.valueOf(ids),catoegoryId,bedType,dimension,thickness)
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

                            JSONObject jsonObject= new JSONObject(json);
                            String Price = jsonObject.getString("price");
                            if (!Price.isEmpty() && Price!=null){
                                mCuctomrate.postValue(Price);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d("TAG", "In onError" + e);
                        mCuctomrate.postValue(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return mCuctomrate;
    }

    public LiveData<Integer> getIndexName() {
        return indexNames;
    }

    public void setIndexName(int index) {
        indexName.setValue(index);
    }


}
