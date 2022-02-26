package com.shrewd.poppydealers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.PaymentItemBinding;
import com.shrewd.poppydealers.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.HistoryPageHolder> {

    List<Payment> productList = new ArrayList<>();
    SelectedListener selectedListener;
    String Type;

    public PaymentAdapter(List<Payment> products, SelectedListener selectedListener) {
        this.productList = products;
        this.selectedListener = selectedListener;
    }

    public void filterAdapter(String type) {
        Type = type;
    }

    @NonNull
    @Override

    public HistoryPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PaymentItemBinding paymentItemBinding = PaymentItemBinding.inflate(layoutInflater, parent, false);
        return new HistoryPageHolder(paymentItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPageHolder holder, int position) {
        Payment product = productList.get(position);


        if (product.getPayment_status().equals("paid")) {
            holder.paymentItemBinding.paymentStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_green));
        } else {
            holder.paymentItemBinding.paymentStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_color_red));
        }

        holder.paymentItemBinding.setHistory(product);
        holder.paymentItemBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface SelectedListener {
        void onItemClick(Payment item);
    }

    public class HistoryPageHolder extends RecyclerView.ViewHolder {

        PaymentItemBinding paymentItemBinding;

        public HistoryPageHolder(@NonNull PaymentItemBinding itemView) {
            super(itemView.getRoot());
            this.paymentItemBinding = itemView;

            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedListener.onItemClick(productList.get(getAdapterPosition()));
                }
            });

        }
    }
}