package com.shopping.bloom.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.model.shoppingbag.ProductEntity;

import java.util.List;

@Dao
public interface WishListProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToWishList(WishListItem item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllItems(List<WishListItem> items);

    @Delete
    void delete(WishListItem item);

    @Query("DELETE FROM wishListItem WHERE productId = :productId")
    void deleteProductWithId(String productId);

    @Query("DELETE FROM wishListItem")
    void deleteAll();

    @Query("SELECT productId FROM wishListItem")
    List<String> getAllItem();

}
