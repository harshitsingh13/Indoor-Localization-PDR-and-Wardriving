package com.mc2022.template;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {  Model.class, roomdata.class},version = 16)
public abstract class MyDatabase extends RoomDatabase {

    // database object
    public abstract MyDAO mydao();

    // instance of database
    // we should have single instance of database and should ensure that our database class should be singleton

    public static MyDatabase myDatabaseinstance;

    public static  MyDatabase getInstance(Context context){

        if(myDatabaseinstance == null){
            myDatabaseinstance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "my_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return myDatabaseinstance;
    }

}