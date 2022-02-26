package com.shrewd.poppydealers.views.fragment.profile;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shrewd.poppydealers.model.User;
import com.shrewd.poppydealers.repositories.ProfileRepo;

public class ProfileViewModel extends ViewModel {
    ProfileRepo repo = new ProfileRepo();

    public LiveData<User> getProfile(Context context) {
        return repo.getProfile(context);
    }
}