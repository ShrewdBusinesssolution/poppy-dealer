package com.shrewd.poppydealers.views.activity.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shrewd.poppydealers.model.CartItem;
import com.shrewd.poppydealers.model.Product;
import com.shrewd.poppydealers.repositories.CartRepo;

import java.util.List;

public class CartViewModel extends ViewModel {
    CartRepo cartRepo = new CartRepo();

    public LiveData<List<CartItem>> getCart(Context context) {
        return cartRepo.getCart(context);
    }

    public LiveData<Integer> getTotalPrice() {
        return cartRepo.getTotalPrice();
    }

    public boolean addItemToCart(Product product, int quantity) {
        return cartRepo.addItemToCart(product, quantity);
    }

    public boolean subItemToCart(Product product) {
        return cartRepo.subItemToCart(product);
    }

    public void removeItemFromCart(CartItem cartItem) {
        cartRepo.removeItemFromCart(cartItem);
    }

}