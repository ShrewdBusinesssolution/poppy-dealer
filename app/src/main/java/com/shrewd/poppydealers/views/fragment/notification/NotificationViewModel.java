package com.shrewd.poppydealers.views.fragment.notification;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shrewd.poppydealers.model.Notify;
import com.shrewd.poppydealers.repositories.NotifyRepo;

import java.util.List;

public class NotificationViewModel extends ViewModel {
    NotifyRepo notifyRepo = new NotifyRepo();

    public LiveData<List<Notify>> getNotifications(Context context) {
        return notifyRepo.getNotifications(context);
    }


}