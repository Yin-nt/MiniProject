package com.example.miniproject2.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "theaters")
public class Theater {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String address;

    public Theater(String name, String address) {
        this.name = name;
        this.address = address;
    }
}