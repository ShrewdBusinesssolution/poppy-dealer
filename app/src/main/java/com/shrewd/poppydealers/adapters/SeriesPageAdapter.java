package com.shrewd.poppydealers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.databinding.SeriesItemBinding;
import com.shrewd.poppydealers.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SeriesPageAdapter extends RecyclerView.Adapter<SeriesPageAdapter.ProductPageHolder> {

    List<Product> productList = new ArrayList<>();
    SelectedListener selectedListener;

    public SeriesPageAdapter(List<Product> products, SelectedListener selectedListener) {
        this.productList = products;
        this.selectedListener = selectedListener;
    }

    @NonNull
    @Override

    public ProductPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SeriesItemBinding seriesItemBinding = SeriesItemBinding.inflate(layoutInflater, parent, false);
        return new ProductPageHolder(seriesItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPageHolder holder, int position) {
        Product product = productList.get(position);

        holder.seriesItemBinding.setProduct(product);
        holder.seriesItemBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface SelectedListener {
        void onItemClick(Product item);
    }

    public class ProductPageHolder extends RecyclerView.ViewHolder {

        SeriesItemBinding seriesItemBinding;

        public ProductPageHolder(@NonNull SeriesItemBinding itemView) {
            super(itemView.getRoot());
            this.seriesItemBinding = itemView;

            itemView.productItemAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedListener.onItemClick(productList.get(getAdapterPosition()));
                }
            });

        }
    }
}
