package com.shrewd.poppydealers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.databinding.LoginItemBinding;
import com.shrewd.poppydealers.model.User;

import java.util.ArrayList;
import java.util.List;

public class LoginListAdapter extends RecyclerView.Adapter<LoginListAdapter.LoginPageHolder> {

    List<User> profileList = new ArrayList<>();
    User profile = new User();
    SelectedListener selectedListener;

    public LoginListAdapter(List<User> users, SelectedListener selectedListener) {
        this.profileList = users;
        this.selectedListener = selectedListener;
    }

    public void changeProfile(User user) {
        this.profile = user;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public LoginPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LoginItemBinding loginItemBinding = LoginItemBinding.inflate(layoutInflater, parent, false);
        return new LoginPageHolder(loginItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginPageHolder holder, int position) {
        User user = profileList.get(position);

        if (profile.getStatus() != null) {

            if (profile.getId() == user.getId()) {
                int index = profileList.indexOf(user);
                user.setStatus("active");
                profileList.set(index, user);
                holder.loginItemBinding.loginStatus.setText("active");
            } else {
                if (!user.getStatus().equals("pending")) {
                    int index = profileList.indexOf(user);
                    user.setStatus("inactive");
                    profileList.set(index, user);
                    holder.loginItemBinding.loginStatus.setText("inactive");
                }

            }
        }

        holder.loginItemBinding.setUser(user);
        holder.loginItemBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }


    public interface SelectedListener {
        void onItemClick(User user);
    }

    public class LoginPageHolder extends RecyclerView.ViewHolder {

        LoginItemBinding loginItemBinding;

        public LoginPageHolder(@NonNull LoginItemBinding itemView) {
            super(itemView.getRoot());
            this.loginItemBinding = itemView;

            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedListener.onItemClick(profileList.get(getAdapterPosition()));
                }
            });

        }
    }
}