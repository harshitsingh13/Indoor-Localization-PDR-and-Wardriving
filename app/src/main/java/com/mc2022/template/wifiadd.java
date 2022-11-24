package com.mc2022.template;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wifi_database")
public class wifiadd {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "WIFI NAME")
    private String wifiname;

    @ColumnInfo(name = "RSSI")
    private String rssi;

    @ColumnInfo(name = "ROOM NO.")
    private String room;

    // constructor
    public wifiadd(String wifiname, String rssi, String room)
    {
        this.wifiname = wifiname;
        this.rssi = rssi;
        this.room=room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWifiname() {
        return wifiname;
    }

    public void setWifiname(String wifiname) {
        this.wifiname = wifiname;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}