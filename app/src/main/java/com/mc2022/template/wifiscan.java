package com.mc2022.template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class wifiscan extends AppCompatActivity {
    private WifiManager wifiManager;
    private ListView listView;
    private Button button;
    private int size=0;

    private ArrayList<String> arrayList=new ArrayList<>();
    private ArrayAdapter adapter;
    List<roomdata> arradd;
    List<Model> arrwifi=new ArrayList<>();
    MyDatabase database;
    EditText ed;
    Button b1,b2;
    TextView t1;
    String rssi;
    String wifi;
    int max=Integer.MIN_VALUE;
    String room="empty";
    int diff=Integer.MAX_VALUE;
    boolean flag=false;


    boolean check(String val){
        for(String str:MainActivity.wname){
            if(str.equals(val)==true){
                return true;
            }
        }
        return false;
    }

   public static List<ScanResult> NearbyPlaces() {

        String myLocation = null;
        // Sort locations-distances pairs based on minimum distances
        Collections.sort(MainActivity.results, new Comparator<ScanResult>() {
            public int compare(ScanResult gd1, ScanResult gd2) {
                return (Math.abs(gd1.level) > Math.abs(gd2.level) ? 1 : (Math.abs(gd1.level) == Math.abs(gd2.level) ? 0 : -1));
            }
        });

        return MainActivity.results;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifiscan);
        b1=(Button)findViewById(R.id.addwifi);
        b2=(Button)findViewById(R.id.findloc);

        ed=(EditText)findViewById(R.id.addroom);
        t1=(TextView)findViewById(R.id.findroom);


        database=MyDatabase.getInstance(wifiscan.this);

        button=(Button)findViewById(R.id.wifibtn);
        button.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        scanWifi();
                    }
                });

        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                //wifiadd obj=new wifiadd(wifi,rssi,ed.getText().toString());
                roomdata room=new roomdata(wifi,rssi,ed.getText().toString());
                database.mydao().insertroom(room);
                //Log.d("data ", String.valueOf(wifi)+" "+String.valueOf(rssi)+" "+ed.getText().toString());
                ed.setText("");
                //add=true;
                //arradd=database.mydao().getListwifiadd();
                //arrwifi=database.mydao().getListwifi();

            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {

                //Intent intent=new Intent(wifiscan.this,getroom.class);
                //startActivity(intent);
                arradd=database.mydao().getListroom();
                for(roomdata row1:arradd) {
                    for (ScanResult res : MainActivity.results) {
                        Log.d("calc", String.valueOf(Math.abs(Integer.valueOf(row1.getRssi()))) + " " + String.valueOf(Math.abs(res.level)) + " difference is: " + String.valueOf(diff));
                        Log.d("room", row1.getRoom());
                        if ((Math.abs(Integer.valueOf(row1.getRssi())) - Math.abs(res.level)) == 0) {
                            //Log.d("calc",String.valueOf(Math.abs(Integer.valueOf(row1.getRssi())))+" "+String.valueOf(Math.abs(res.level))+" difference is: "+String.valueOf(diff));
                            diff = Math.abs(Integer.valueOf(row1.getRssi())) - Math.abs(res.level);
                            if (!check(row1.getRoom())) {
                                room = row1.getRoom();
                                MainActivity.wname.add(row1.getRoom());
                            }

                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        break;
                }

                t1.setText("YOUR ROOM NO. IS: "+room);
            }
        });

        listView=(ListView) findViewById(R.id.wifilist);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(wifiscan.this, "wifi is disabled", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }
        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        scanWifi();
    }
    private void scanWifi(){
        arrayList.clear();
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(wifiscan.this, "Wifi scanning started", Toast.LENGTH_SHORT).show();
    }
    BroadcastReceiver wifiReciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.results=wifiManager.getScanResults();
            unregisterReceiver(this);
            max=Integer.MIN_VALUE;
            for(ScanResult res: MainActivity.results){
                if(Math.abs(res.level)>max){
                    max=Math.abs(res.level);
                    rssi=Integer.toString(res.level);
                    wifi=res.SSID;
                }

                arrayList.add("SSID --> "+res.SSID+" and "+"RSSI --> "+res.level);
                Model model=new Model(String.valueOf(res.SSID),String.valueOf(res.level));
                database.mydao().insertwifi(model);
                adapter.notifyDataSetChanged();

            }
        }
    };



}