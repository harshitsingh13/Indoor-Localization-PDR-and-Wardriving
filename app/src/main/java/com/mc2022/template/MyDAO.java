package com.mc2022.template;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MyDAO {


    @Query("Select * from wifi_table Order by id")
    List<Model> getListModel();

    //@Query("Select * from wifi_database Order by id")
    //List<wifiadd> getListwifiadd();

    @Query("Select * from room_database Order by id")
    List<roomdata> getListroom();

    // Insert
    @Insert
    void insertwifi(Model model);


    // Insert
    //@Insert
    //void insertwifiadd(wifiadd wifiadd);

    // Insert
    @Insert
    void insertroom(roomdata roomdata);



    // Delete using id
    //@Query("DELETE from wifi_database where id = :id")
    //void deleteUsingID(int id);


    // Delete using id
    //@Query("DELETE from Light_table where id = :id")
    //void deleteUsingID(int id);

    // Delete using id
    //@Query("DELETE from Gps_table where id = :id")
    //void deleteGps(int id);

    // Delete using object
    //@Delete
    //void delete(Light light);



    // Update firstname
    //@Query("Update my_table set `z-cord` = :zcord, `x-cord` = :xcord, `y-cord` = :ycord where `id` = :id")
    //void update(i xcord,float ycord, float zcord);
}