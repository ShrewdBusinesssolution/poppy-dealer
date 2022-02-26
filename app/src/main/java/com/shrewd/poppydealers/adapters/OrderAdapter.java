package com.shrewd.poppydealers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.HistoryItemBinding;
import com.shrewd.poppydealers.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.HistoryPageHolder> {

    List<Order> productList = new ArrayList<>();
    SelectedListener selectedListener;
    String  Class;

    public OrderAdapter(List<Order> products, SelectedListener selectedListener) {
        this.productList = products;
        this.selectedListener = selectedListener;
    }




    @NonNull
    @Override

    public HistoryPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HistoryItemBinding historyItemBinding = HistoryItemBinding.inflate(layoutInflater, parent, false);
        return new HistoryPageHolder(historyItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPageHolder holder, int position) {
        Order order = productList.get(position);

//        if (Type != null && !order.getOrder_status().equals(Type)) {
//
//            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//            layoutParams.height = 0;
//            layoutParams.width = 0;
//            holder.itemView.setLayoutParams(layoutParams);
//        } else {
//            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            holder.itemView.setLayoutParams(layoutParams);
//        }


        if (order.getOrder_status().equals("completed")) {
            holder.historyItemBinding.orderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_green));
        } else {
            holder.historyItemBinding.orderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_color_red));
        }

        holder.historyItemBinding.setHistory(order);
        holder.historyItemBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public interface SelectedListener {
        void onItemClick(Order item);
    }

    public class HistoryPageHolder extends RecyclerView.ViewHolder {

        HistoryItemBinding historyItemBinding;

        public HistoryPageHolder(@NonNull HistoryItemBinding itemView) {
            super(itemView.getRoot());
            this.historyItemBinding = itemView;

            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedListener.onItemClick(productList.get(getAdapterPosition()));
                }
            });

        }
    }
}