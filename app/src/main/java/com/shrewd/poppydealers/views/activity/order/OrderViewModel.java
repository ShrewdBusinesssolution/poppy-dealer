package com.shrewd.poppydealers.views.activity.order;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shrewd.poppydealers.model.Order;
import com.shrewd.poppydealers.repositories.OrderRepo;

import java.util.List;

public class OrderViewModel extends ViewModel {

    OrderRepo repo = new OrderRepo();

    public LiveData<List<Order>> getOrders(Context context, String status, String from, String to, String value) {
        return repo.getOrders(context, status, from, to, value);
    }
}