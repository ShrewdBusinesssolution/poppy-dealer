package com.shrewd.poppydealers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.NotificationItemBinding;
import com.shrewd.poppydealers.model.Notify;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotifyViewHolder> {
    private final List<Notify> mlist;
    private final Context mcontext;
    private final LayoutInflater minflater;
    private boolean expand = true;


    public NotificationListAdapter(Context context, List<Notify> list) {
        mcontext = context;
        mlist = list;
        minflater = LayoutInflater.from(context);
    }


    @NonNull
    @NotNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        NotificationItemBinding notificationItemBinding = NotificationItemBinding.inflate(minflater, parent, false);
        return new NotifyViewHolder(notificationItemBinding);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull @NotNull NotifyViewHolder holder, int position) {

        Notify notify = mlist.get(position);

        holder.notificationItemBinding.notificationTitle.setText(notify.getNotification_title());
        holder.notificationItemBinding.notificationDate.setText(notify.getNotification_date());
        holder.notificationItemBinding.notificationDescription.setText(notify.getNotification_body());

//        if (notify.getImage_url().equals("")) {
//            holder.notificationItemBinding.notificationImageView.setVisibility(View.GONE);
//            holder.notificationItemBinding.notificationMore.setVisibility(View.GONE);
//        } else {
//            holder.notificationItemBinding.notificationImageView.setVisibility(View.VISIBLE);
//            holder.notificationItemBinding.notificationMore.setVisibility(View.VISIBLE);
//            if (!notify.getImage_url().equals("")) {
//                Glide.with(mcontext).onLowMemory();
//                Glide.with(mcontext).load(notify.getImage_url()).into(holder.notificationItemBinding.notificationImage);
//            }
//        }
        holder.notificationItemBinding.notificationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand) {
                    holder.notificationItemBinding.notificationMore.setText("see less");
                    holder.notificationItemBinding.notificationImageView.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_arrow_up));
                    holder.notificationItemBinding.notificationImageLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.notificationItemBinding.notificationMore.setText("see more");
                    holder.notificationItemBinding.notificationImageView.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_arrow_down));
                    holder.notificationItemBinding.notificationImageLayout.setVisibility(View.GONE);
                }
                expand = !expand;
            }
        });
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    class NotifyViewHolder extends RecyclerView.ViewHolder {
        NotificationItemBinding notificationItemBinding;

        public NotifyViewHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;

        }
    }
}

//if (notify.getStatus().equals("Your Order Confirmed")) {
//        holder.notificationItemBinding.notificationTag.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.phpay)));
//
//        } else if (notify.getStatus().equals("Your Order Delivered")) {
//        holder.notificationItemBinding.notificationTag.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.status_green)));
//
//        } else if (notify.getStatus().equals("Payment Pending")) {
//        holder.notificationItemBinding.notificationTag.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.status_color_red)));
//
//        } else if (notify.getStatus().equals("Payment Successful")) {
//        holder.notificationItemBinding.notificationTag.setBackgroundTintList(ColorStateList.valueOf(mcontext.getColor(R.color.status_color_blue)));
//
//        }


