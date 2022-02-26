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
import com.shrewd.poppydealers.databinding.CartItemBinding;
import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.model.Customize;
import com.shrewd.poppydealers.model.CustomizeItem;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.rxjava.RxClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.cartViewHolder> {
    private List<CartItem> mlist;
    private List<Customize> mCartList;
    private Context mcontext;
    private LayoutInflater minflater;
    CartInterface cartInterface;
    NestedCartListAdapter cartListAdapter;


    public CartListAdapter(Context context, List<CartItem> list, List<Customize> cartList, CartInterface cartInterface) {
        mcontext = context;
        mlist = list;
        mCartList = cartList;
        minflater = LayoutInflater.from(context);
        this.cartInterface = cartInterface;
    }

    public void refresh(List<Customize> cartList) {
        mCartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CartItemBinding cartItemBinding = CartItemBinding.inflate(minflater, parent, false);
        return new cartViewHolder(cartItemBinding);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull @NotNull cartViewHolder holder, int position) {

        CartItem cartItem = mlist.get(position);

        holder.cartItemBinding.cartItemName.setText(cartItem.getProduct().getProduct_name());
        holder.cartItemBinding.cartItemPrice.setText("₹ " + cartItem.getProduct().getProduct_price());
        holder.cartItemBinding.cartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));

        if (cartItem.getProduct().getProduct_type().equals("0")) {
            holder.cartItemBinding.cartItemQuantityLayout.setVisibility(View.VISIBLE);
        } else {
            holder.cartItemBinding.cartItemQuantityLayout.setVisibility(View.GONE);
        }

        Glide.with(holder.cartItemBinding.cartItemImage).load(RxClient.BASE_URL + RxClient.IMAGE_BASE_URL + cartItem.getProduct().getProduct_image()).into(holder.cartItemBinding.cartItemImage);


        for (Customize item : mCartList) {

            if (cartItem.getProduct().getProduct_id().equals(item.getProduct_id())) {

                holder.cartItemBinding.cartSubLayout.setVisibility(View.VISIBLE);
                holder.cartItemBinding.cartNestedRecycler.setLayoutManager(new LinearLayoutManager(mcontext, RecyclerView.VERTICAL, false));

                cartListAdapter = new NestedCartListAdapter("cart", item.getList(), new NestedCartListAdapter.CartInterface() {
                    @Override
                    public void plus(int quantity, CustomizeItem item) {
                        item.setGroup_count(item.getGroup_count() + 1);

                        cartInterface.nestedAdd(cartItem.getQuantity(), cartItem.getProduct(), item.getGroup_count(), item);


                        cartListAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void minus(int quantity, CustomizeItem item) {

                        item.setGroup_count(item.getGroup_count() - 1);

                        cartInterface.nestedSub(cartItem.getQuantity(), cartItem.getProduct(), item.getGroup_count(), item);


                        cartListAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void remove(int quantity, CustomizeItem item) {

                        cartInterface.nestedRemove(cartItem.getQuantity(), cartItem.getProduct(), quantity, item);
                        cartListAdapter.notifyDataSetChanged();

                    }
                });

                cartListAdapter.notifyDataSetChanged();
                holder.cartItemBinding.cartNestedRecycler.setAdapter(cartListAdapter);

                break;
            } else {
                holder.cartItemBinding.cartSubLayout.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public interface CartInterface {

        void onPlusClick(int quantity, CartItem cartItem);

        void onMinusClick(int quantity, CartItem cartItem);

        void onDelete(CartItem cartItem);

        void nestedAdd(int pro_qua, Product product, int cus_qua, CustomizeItem item);

        void nestedSub(int pro_qua, Product product, int cus_qua, CustomizeItem item);

        void nestedRemove(int pro_qua, Product product, int cus_qua, CustomizeItem item);


    }

    class cartViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding cartItemBinding;

        public cartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.cartItemBinding = binding;


            cartItemBinding.cartItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInterface.onDelete(mlist.get(getAdapterPosition()));
                }
            });
            cartItemBinding.cartItemPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(cartItemBinding.cartItemQuantity.getText().toString()) + 1;
                    if (quantity >= 1) {
                        cartInterface.onPlusClick(quantity, mlist.get(getAdapterPosition()));
                    }
                }
            });
            cartItemBinding.cartItemMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(cartItemBinding.cartItemQuantity.getText().toString()) - 1;
                    if (quantity >= 1) {
                        cartInterface.onMinusClick(quantity, mlist.get(getAdapterPosition()));

                    } else {
                        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                        layoutParams.height = 0;
                        layoutParams.width = 0;
                        itemView.setLayoutParams(layoutParams);

                        cartInterface.onDelete(mlist.get(getAdapterPosition()));
                    }
                }
            });

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


