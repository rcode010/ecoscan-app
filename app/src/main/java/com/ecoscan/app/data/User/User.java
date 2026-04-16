package com.ecoscan.app.data.User;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public User(String name) {
        this.name = name;
    }
}
