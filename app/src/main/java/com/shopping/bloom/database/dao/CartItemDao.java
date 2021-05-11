package com.shopping.bloom.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.shopping.bloom.model.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {

    @Insert
    void addToCart(CartItem cartItem);

    @Insert
    void addToCart(List<CartItem> cartItems);

    @Update
    void update(CartItem cartItem);

    @Delete
    void removeItem(CartItem cartItem);

    /* Return the total number of items quantity in the cart*/
    @Query("SELECT SUM(quantity) FROM cart_item")
    int getCartSize();

    @Query("SELECT SUM(quantity) FROM cart_item")
    LiveData<Integer> changeCartIcon();

    @Query("SELECT * FROM cart_item WHERE parentId = :parentId AND childId = :childId")
    List<CartItem> getAllProductWith(String parentId, String childId);

    @Query("UPDATE cart_item SET quantity = quantity + 1 WHERE parentId = :parentId AND childId = :childId")
    void incrementQuantity(String parentId, String childId);

    @Query("SELECT * FROM cart_item ORDER BY id DESC")
    LiveData<List<CartItem>> getAllCartItem();

    @Query("DELETE FROM cart_item")
    void removeAll();
}
