package com.shrewd.poppydealers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shrewd.poppydealers.databinding.OrderViewItemBinding;
import com.shrewd.poppydealers.model.Customize;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.rxjava.RxClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderViewListAdapter extends RecyclerView.Adapter<OrderViewListAdapter.orderViewHolder> {
    private final List<Product> productList;
    private final List<Customize> customizeList;
    private final Context mcontext;
    private final LayoutInflater minflater;
    NestedCartListAdapter orderListAdapter;
    private final boolean expand = true;


    public OrderViewListAdapter(Context context, List<Product> list, List<Customize> customize) {
        mcontext = context;
        productList = list;
        customizeList = customize;
        minflater = LayoutInflater.from(context);
    }

    @NonNull
    @NotNull
    @Override
    public orderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        OrderViewItemBinding orderViewItemBinding = OrderViewItemBinding.inflate(minflater, parent, false);
        return new orderViewHolder(orderViewItemBinding);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull @NotNull orderViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.orderViewItemBinding.orderItemName.setText(product.getProduct_name());
        holder.orderViewItemBinding.orderItemPrice.setText("₹ " + product.getProduct_price());
        holder.orderViewItemBinding.orderItemQuantity.setText("x" + product.getProduct_quantity());


        Glide.with(holder.orderViewItemBinding.orderItemImage).load(RxClient.BASE_URL + RxClient.IMAGE_BASE_URL + product.getProduct_image()).into(holder.orderViewItemBinding.orderItemImage);

        if (product.getProduct_type().equals("0")) {
            holder.orderViewItemBinding.orderSubLayout.setVisibility(View.GONE);
        } else {
            holder.orderViewItemBinding.orderSubLayout.setVisibility(View.VISIBLE);

            holder.orderViewItemBinding.orderCustomise.setLayoutManager(new LinearLayoutManager(mcontext, RecyclerView.VERTICAL, false));

            for (Customize customize : customizeList) {
                if (product.getProduct_id().equalsIgnoreCase(customize.getProduct_id())) {

                    orderListAdapter = new NestedCartListAdapter("order", customize.getList());
                    orderListAdapter.notifyDataSetChanged();
                    holder.orderViewItemBinding.orderCustomise.setAdapter(orderListAdapter);

                }

            }


        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class orderViewHolder extends RecyclerView.ViewHolder {
        OrderViewItemBinding orderViewItemBinding;

        public orderViewHolder(OrderViewItemBinding binding) {
            super(binding.getRoot());
            this.orderViewItemBinding = binding;

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


