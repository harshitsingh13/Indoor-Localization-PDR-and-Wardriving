package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Accelerometer_table")
public class Accelerometer {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "x-cord")
    private String xcord;

    @ColumnInfo(name = "y-cord")
    private String ycord;

    @ColumnInfo(name = "z-cord")
    private String zcord;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    // constructor
    public Accelerometer(String xcord,String ycord,String zcord, String timestamp)
    {
        this.xcord = xcord;
        this.ycord = ycord;
        this.zcord = zcord;
        this.timestamp=timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXcord() {
        return xcord;
    }

    public void setXcord(String xcord) {
        this.xcord = xcord;
    }

    public String getYcord() {
        return ycord;
    }

    public void setYcord(String ycord) {
        this.ycord = ycord;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getZcord() {
        return zcord;
    }

    public void setZcord(String zcord) {
        this.zcord = zcord;
    }
}