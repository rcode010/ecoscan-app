package com.ecoscan.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantry_item")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String itemName;
    public String barcode;
    public long expiryDate;   // stored as timestamp (milliseconds)
    public long dateAdded;    // when you scanned it
    public boolean isExpired;

    public PantryItem(String itemName, String barcode, long expiryDate) {
        this.itemName = itemName;
        this.barcode = barcode;
        this.expiryDate = expiryDate;
        this.dateAdded = System.currentTimeMillis();
        this.isExpired = false;
    }
}