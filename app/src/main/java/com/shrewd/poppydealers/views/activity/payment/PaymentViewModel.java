package com.shrewd.poppydealers.views.activity.payment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shrewd.poppydealers.model.Payment;
import com.shrewd.poppydealers.repositories.PaymentRepo;

import java.util.List;

public class PaymentViewModel extends ViewModel {

    PaymentRepo repo = new PaymentRepo();

    public LiveData<List<Payment>> getPayments(Context context) {
        return repo.getPayments(context);
    }

}