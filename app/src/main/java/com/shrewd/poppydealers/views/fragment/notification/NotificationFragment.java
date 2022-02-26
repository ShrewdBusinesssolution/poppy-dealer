package com.shrewd.poppydealers.views.fragment.notification;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.adapters.NotificationListAdapter;
import com.shrewd.poppydealers.databinding.NotificationFragmentBinding;
import com.shrewd.poppydealers.listeners.BackPressListener;
import com.shrewd.poppydealers.model.Notify;
import com.shrewd.poppydealers.views.fragment.home.HomeFragment;

import java.util.List;

public class NotificationFragment extends Fragment implements BackPressListener {

    private static final String TAG = "NotificationFragment";
    public static BackPressListener listener;
    NotificationFragmentBinding binding;
    private NotificationViewModel mViewModel;
    private NotificationListAdapter notificationListAdapter;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NotificationFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {


            binding.notificationRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
            binding.notificationRecycler.setHasFixedSize(true);

            mViewModel = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);

            mViewModel.notifyRepo.getNotifications(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<Notify>>() {
                @Override
                public void onChanged(List<Notify> notifies) {

                    if (notifies.size() > 0) {
                        binding.emptyLayout.setVisibility(View.GONE);
                        binding.emptyAnimation.cancelAnimation();
                        binding.notificationRecycler.setVisibility(View.VISIBLE);
                    } else {
                        binding.notificationRecycler.setVisibility(View.GONE);
                        binding.emptyLayout.setVisibility(View.VISIBLE);
                        binding.emptyAnimation.playAnimation();
                    }

                    notificationListAdapter = new NotificationListAdapter(requireActivity(), notifies);
                    notificationListAdapter.notifyDataSetChanged();
                    binding.notificationRecycler.setAdapter(notificationListAdapter);
                }
            });
        }
    }

    @Override
    public void onPause() {
        listener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener = this;
    }

    @Override
    public void onBackPressed() {
        listener = null;
        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
    }

}