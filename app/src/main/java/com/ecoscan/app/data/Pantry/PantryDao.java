package com.ecoscan.app.data.Pantry;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDao {

    @Insert
    void insert(PantryItem item);

    @Update
    void update(PantryItem item);

    @Delete
    void delete(PantryItem item);

    // Get all items ordered by expiry date (soonest first)
    @Query("SELECT * FROM pantry_item ORDER BY expiryDate ASC")
    LiveData<List<PantryItem>> getAllItems();

    // Get items expiring within 2 days (for notifications)
    @Query("SELECT * FROM pantry_item WHERE expiryDate <= :twoDaysFromNow AND isExpired = 0")
    LiveData<List<PantryItem>> getItemsExpiringSoon(long twoDaysFromNow);

    // Get already expired items
    @Query("SELECT * FROM pantry_item WHERE expiryDate < :now AND isExpired = 0")
    List<PantryItem> getExpiredItems(long now);

    // Find item by barcode
    @Query("SELECT * FROM pantry_item WHERE barcode = :barcode LIMIT 1")
    PantryItem getItemByBarcode(String barcode);

    // Get total money saved (sum of price for consumed items)
    @Query("SELECT SUM(price) FROM pantry_item WHERE isConsumed = 1")
    LiveData<Double> getTotalMoneySaved();

    // Get total items tracked (not deleted/consumed, or maybe all?)
    @Query("SELECT COUNT(*) FROM pantry_item")
    LiveData<Integer> getTotalItemsTracked();

    @Query("SELECT COUNT(*) FROM pantry_item WHERE isConsumed = 1")
    LiveData<Integer> getConsumedItemsCount();
}