package com.ecoscan.app.data.Pantry;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantry_item")
public class PantryItem implements Parcelable {

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

    protected PantryItem(Parcel in) {
        id = in.readInt();
        itemName = in.readString();
        barcode = in.readString();
        expiryDate = in.readLong();
        dateAdded = in.readLong();
        isExpired = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(itemName);
        parcel.writeString(barcode);
        parcel.writeLong(expiryDate);
        parcel.writeLong(dateAdded);
        parcel.writeByte((byte) (isExpired ? 1 : 0));
    }

    public static final Creator<PantryItem> CREATOR = new Creator<PantryItem>() {
        @Override
        public PantryItem createFromParcel(Parcel in) {
            return new PantryItem(in);
        }

        @Override
        public PantryItem[] newArray(int size) {
            return new PantryItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}