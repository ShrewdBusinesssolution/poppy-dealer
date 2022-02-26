package com.shrewd.poppydealers.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.databinding.NestedCartItemBinding;
import com.shrewd.poppydealers.model.CustomizeItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NestedCartListAdapter extends RecyclerView.Adapter<NestedCartListAdapter.cartViewHolder> {
    CartInterface cartInterface;
    private final List<com.shrewd.poppydealers.model.CustomizeItem> cartList;
    private final String type;

    public NestedCartListAdapter(String Type, List<CustomizeItem> list, CartInterface Interface) {
        type = Type;
        cartList = list;
        cartInterface = Interface;

    }

    public NestedCartListAdapter(String Type, List<CustomizeItem> list) {
        type = Type;
        cartList = list;
    }

    @NonNull
    @NotNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NestedCartItemBinding nestedCartItemBinding = NestedCartItemBinding.inflate(layoutInflater, parent, false);
        return new cartViewHolder(nestedCartItemBinding);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull @NotNull cartViewHolder holder, int position) {

        CustomizeItem customizeItem = cartList.get(position);


        holder.cartItemBinding.nestedCartModel.setText(customizeItem.getInches() + ", ");
        holder.cartItemBinding.nestedCartColor.setText(customizeItem.getColor());
        holder.cartItemBinding.nestedCartSize.setText(", " + customizeItem.getSize());


        if (type.equalsIgnoreCase("cart")) {
            holder.cartItemBinding.nestedCartQuantityCount.setVisibility(View.GONE);
            holder.cartItemBinding.nestedCartAddLayout.setVisibility(View.VISIBLE);
            holder.cartItemBinding.nestedCartQuantity.setText(String.valueOf(customizeItem.getGroup_count()));
        } else {
            holder.cartItemBinding.nestedCartAddLayout.setVisibility(View.GONE);
            holder.cartItemBinding.nestedCartQuantityCount.setVisibility(View.VISIBLE);
            holder.cartItemBinding.nestedCartQuantityCount.setText("x" + customizeItem.getGroup_count());
        }


//        if (ID.equals(customizeItem.getProduct_id())) {
//
//
//
//            holder.cartItemBinding.nestedCartModel.setText(customizeItem.getInches() + ", ");
//            holder.cartItemBinding.nestedCartColor.setText(customizeItem.getColor());
//            holder.cartItemBinding.nestedCartSize.setText(", " + customizeItem.getSize());
//            holder.cartItemBinding.nestedCartAddLayout.setVisibility(View.GONE);
//            holder.cartItemBinding.nestedCartQuantityCount.setVisibility(View.VISIBLE);
//            holder.cartItemBinding.nestedCartQuantityCount.setText(String.valueOf(customizeItem.getGroup_count()));
//
//        } else {
//            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//            layoutParams.height = 0;
//            layoutParams.width = 0;
//            holder.itemView.setLayoutParams(layoutParams);
//        }


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public interface CartInterface {

        void plus(int quantity, CustomizeItem item);

        void minus(int quantity, CustomizeItem item);

        void remove(int quantity, CustomizeItem item);

    }

    class cartViewHolder extends RecyclerView.ViewHolder {
        NestedCartItemBinding cartItemBinding;

        public cartViewHolder(NestedCartItemBinding binding) {
            super(binding.getRoot());
            this.cartItemBinding = binding;

            cartItemBinding.nestedCartPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(cartItemBinding.nestedCartQuantity.getText().toString());
                    if (quantity >= 1) {
                        cartInterface.plus(quantity, cartList.get(getAdapterPosition()));
                    }

                }
            });
            cartItemBinding.nestedCartMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(cartItemBinding.nestedCartQuantity.getText().toString());
                    if (quantity >= 1) {
                        cartInterface.minus(quantity, cartList.get(getAdapterPosition()));
                    } else {
                        cartInterface.remove(0, cartList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}



