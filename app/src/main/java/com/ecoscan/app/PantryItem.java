package com.ecoscan.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantry_item")
public class PantryItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String itemName;
    public String barcode;
    public String expiryDate;
    public boolean isExpired;

    public PantryItem(String itemName, String barcode, String expiryDate) {
        this.itemName = itemName;
        this.barcode = barcode;
        this.expiryDate = expiryDate;
        this.isExpired = false;
    }
}
