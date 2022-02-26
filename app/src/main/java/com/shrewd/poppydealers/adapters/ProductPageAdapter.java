package com.shrewd.poppydealers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shrewd.poppydealers.databinding.ProductItemBinding;
import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.rxjava.RxClient;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProductPageAdapter extends RecyclerView.Adapter<ProductPageAdapter.ProductPageHolder> {

    List<Product> productList = new ArrayList<>();
    List<CartItem> cart = new ArrayList<>();
    SelectedListener selectedListener;


    public ProductPageAdapter(List<Product> products, SelectedListener selectedListener) {
        this.productList = products;
        this.selectedListener = selectedListener;
    }


    public void CartData(List<CartItem> cart) {
        this.cart = cart;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public ProductPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
         ProductItemBinding productItemBinding = ProductItemBinding.inflate(layoutInflater, parent, false);
        return new ProductPageHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPageHolder holder, int position) {
        Product product = productList.get(position);

        holder.productItemBinding.setProduct(product);
        holder.productItemBinding.executePendingBindings();


        for (int z = 0; z < product.getImageArray().length(); z++) {
            String image = null;
//            try {
//                image = product.getImageArray().getString(0);
//                Glide.with(holder.productItemBinding.productItemImage).load(RxClient.BASE_URL + RxClient.IMAGE_BASE_URL + image).into(holder.productItemBinding.productItemImage);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }


        for (int i = 0; i < cart.size(); i++) {
            CartItem cartItem = cart.get(i);
            if (cartItem.getProduct().getProduct_id().equals(product.getProduct_id())) {
                holder.productItemBinding.productItemQuantity.setText(String.valueOf(cartItem.getQuantity()));

                if (cartItem.getQuantity() != 0) {
                    holder.productItemBinding.productItemAdd.setVisibility(View.GONE);
                    holder.productItemBinding.productItemQuantityLayout.setVisibility(View.VISIBLE);

                } else {
                    holder.productItemBinding.productItemAdd.setVisibility(View.VISIBLE);
                    holder.productItemBinding.productItemQuantityLayout.setVisibility(View.GONE);

                }

                break;
            }

        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface SelectedListener {
        void onItemClick(int quantity, Product item);

        void onAddItemClick(int quantity, Product dummy);

        void onPlusClick(int quantity, Product item);

        void onMinusClick(int quantity, Product item);

        void onRemoveClick(int quantity, Product product);

        void onImageClick(Product item);


    }

    public class ProductPageHolder extends RecyclerView.ViewHolder {

        ProductItemBinding productItemBinding;

        public ProductPageHolder(@NonNull ProductItemBinding itemView) {
            super(itemView.getRoot());
            this.productItemBinding = itemView;

            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(itemView.productItemQuantity.getText().toString());

                    selectedListener.onItemClick(quantity, productList.get(getAdapterPosition()));
                }
            });
            itemView.productItemAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.productItemAdd.setVisibility(View.GONE);
                    itemView.productItemQuantityLayout.setVisibility(View.VISIBLE);

                    int quantity = Integer.parseInt(itemView.productItemQuantity.getText().toString());

                    selectedListener.onAddItemClick(quantity, productList.get(getAdapterPosition()));
                }
            });
            itemView.productItemPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(itemView.productItemQuantity.getText().toString());
//                    if (quantity >= 1) {
//                        selectedListener.onPlusClick(quantity, productList.get(getAdapterPosition()));
//                    }

                    selectedListener.onPlusClick(quantity, productList.get(getAdapterPosition()));
                }
            });
            itemView.productItemMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(itemView.productItemQuantity.getText().toString()) - 1;
                    if (quantity >= 1) {
                        selectedListener.onMinusClick(quantity, productList.get(getAdapterPosition()));

                    } else {
                        itemView.productItemQuantityLayout.setVisibility(View.GONE);
                        itemView.productItemAdd.setVisibility(View.VISIBLE);

                        selectedListener.onRemoveClick(0, productList.get(getAdapterPosition()));
                    }
                }
            });
            itemView.productItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedListener.onImageClick(productList.get(getAdapterPosition()));
                }
            });

        }
    }

}


//for (int i = 0; i < cart.size(); i++) {
//        if (quantity != 0 && product.getProduct_id().equals(product_id)) {
//
//        holder.productItemBinding.productItemAdd.setVisibility(View.GONE);
//        holder.productItemBinding.productItemQuantityLayout.setVisibility(View.VISIBLE);
//        holder.productItemBinding.productItemQuantity.setText(String.valueOf(quantity));
//        } else {
//
//        holder.productItemBinding.productItemQuantityLayout.setVisibility(View.GONE);
//        holder.productItemBinding.productItemAdd.setVisibility(View.VISIBLE);
//        }
//        }