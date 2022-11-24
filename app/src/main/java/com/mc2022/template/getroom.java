package com.mc2022.template;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class getroom extends AppCompatActivity {

    TextView t1;
    List<roomdata> arradd=new ArrayList<>();
    List<Model> arrwifi=new ArrayList<>();
    MyDatabase dbms;
    int diff=Integer.MAX_VALUE;
    String room="empty";
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getroom);

        dbms=MyDatabase.getInstance(getroom.this);
        t1=(TextView)findViewById(R.id.roomlocation);

        arradd=dbms.mydao().getListroom();
        arrwifi=dbms.mydao().getListModel();
        Log.d("length of list ", String.valueOf(MainActivity.results.size()));

        for(roomdata row1:arradd){
            for(ScanResult res:MainActivity.results){
                Log.d("calc",String.valueOf(Math.abs(Integer.valueOf(row1.getRssi())))+" "+String.valueOf(Math.abs(res.level))+" difference is: "+String.valueOf(diff));
                Log.d("room",row1.getRoom());
                if((Math.abs(Integer.valueOf(row1.getRssi()))-Math.abs(res.level))==0){
                    //Log.d("calc",String.valueOf(Math.abs(Integer.valueOf(row1.getRssi())))+" "+String.valueOf(Math.abs(res.level))+" difference is: "+String.valueOf(diff));
                    diff=Math.abs(Integer.valueOf(row1.getRssi()))-Math.abs(res.level);
                    if(!check(row1.getRoom())){
                            room=row1.getRoom();
                            MainActivity.wname.add(row1.getRoom());
                    }

                    //Log.d("room",row1.getRoom());
                    flag=true;
                    break;
                }
            }

            if(flag)
                break;
        }

        t1.setText("YOU ARE AT ROOM: "+room);

    }

    boolean check(String val){
        for(String str:MainActivity.wname){
            if(str.equals(val)==true){
                return true;
            }
        }
        return false;
    }
}