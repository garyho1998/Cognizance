package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class main_menu extends AppCompatActivity {

    Button account;
    Button logOut;
    ArrayList<JSONObject> random_list=new ArrayList<JSONObject>();
    ArrayList<JSONObject> random_list1=new ArrayList<JSONObject>();
    ArrayList<JSONObject> random_list2=new ArrayList<JSONObject>();
    ArrayList<JSONObject> random_list3=new ArrayList<JSONObject>();
    ArrayList<String> ordered_top4;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.main_menu);
        username = getIntent().getStringExtra("username");
        TextView welcomeTextView = (TextView)findViewById(R.id.welcome);
        welcomeTextView.setText("Welcome " + username);

        GetRecomActivityAndSetbutton(username);
        GetRandomActivityAndSetbutton();
        GetMonringActivitiesAndSetbutton();
        GetDaytimeActivitiesAndSetbutton();
        GetNightActivitiesAndSetbutton();
    }

    public HashMap<Item, Double> sortByValue(HashMap<Item, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Item, Double> > list =  new LinkedList<Map.Entry<Item, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Item, Double> >() {
            public int compare(Map.Entry<Item, Double> o1,
                               Map.Entry<Item, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Item, Double> temp = new LinkedHashMap<Item, Double>();
        for (Map.Entry<Item, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public void GetRecomActivityAndSetbutton(final String username){
        String activity_json = loadJSONFromAsset();
        try {
            JSONObject jsonObject = new JSONObject(activity_json);

            Map<User, HashMap<Item, Double>> inputData = InputData.initializeData(10, jsonObject, username);
            SlopeOne.slopeOne(inputData, 10);

            Map<User, HashMap<Item, Double>> outputData = SlopeOne.outputData;
            System.out.println("outputdata size:" + outputData.size());

            ArrayList<String> top4 = new ArrayList<>();
            for(Map.Entry<User, HashMap<Item, Double>> user: outputData.entrySet()){
                System.out.println("temp username:" + user.getKey().getUsername());
                System.out.println("username:" + username);

                if(username.contains(user.getKey().getUsername())) {
                    System.out.println("temp name:" + user.getKey().getUsername());
                    Map<Item, Double> sorted_user = sortByValue(user.getValue());
                    int counter=0;
                    for (Item j : sorted_user.keySet()) {
                        if(counter>sorted_user.size()-5){
                            System.out.println(" " + j.getItemName() + " --> " + sorted_user.get(j).doubleValue());
                            top4.add(j.getItemName());
                        }
                        counter++;
                    }
                    break;
                }
            }

            ordered_top4 = reverseArrayList(top4);

            Button recom_button1 = (Button)findViewById(R.id.recom_button1);
            recom_button1.setText(ordered_top4.get(0));
            recom_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(0));
                    extras.putString("username",username);
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            Button recom_button2 = (Button)findViewById(R.id.recom_button2);
            recom_button2.setText(ordered_top4.get(1));
            recom_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(1));
                    extras.putString("username",username);
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            Button recom_button3 = (Button)findViewById(R.id.recom_button3);
            recom_button3.setText(ordered_top4.get(2));
            recom_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(2));
                    extras.putString("username",username);
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            Button recom_button4 = (Button)findViewById(R.id.recom_button4);
            recom_button4.setText(ordered_top4.get(3));
            recom_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Bundle extras = new Bundle();
                    extras.putString("activity_name",ordered_top4.get(3));
                    extras.putString("username",username);
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<String> reverseArrayList(ArrayList<String> alist)
    {
        // Arraylist for storing reversed elements
        ArrayList<String> revArrayList = new ArrayList<String>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }

    public void GetRandomActivityAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja1 = (JSONArray) jsonObject.get("monring_activities");
            JSONArray ja2 = (JSONArray) jsonObject.get("night_activities");
            JSONArray ja3 = (JSONArray) jsonObject.get("daytime_activities");
            for (int i = 0; i < ja1.length(); i++) {
                JSONObject temp = ja1.getJSONObject(i);
                activity_list.add(temp);
            }
            for (int i = 0; i < ja2.length(); i++) {
                JSONObject temp = ja1.getJSONObject(i);
                activity_list.add(temp);
            }
            for (int i = 0; i < ja3.length(); i++) {
                JSONObject temp = ja1.getJSONObject(i);
                activity_list.add(temp);
            }
            random_list = getRandomElement(activity_list,1);

            Button start_button = (Button)findViewById(R.id.start);
            start_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public void GetMonringActivitiesAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja = (JSONArray) jsonObject.get("monring_activities");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject temp = ja.getJSONObject(i);
                activity_list.add(temp);
            }

            random_list1 = getRandomElement(activity_list,4);

            Button morning_button1 = (Button)findViewById(R.id.morning_button1);
            morning_button1.setText(random_list1.get(0).getString("name"));
            morning_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list1.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button morning_button2 = (Button)findViewById(R.id.morning_button2);
            morning_button2.setText(random_list1.get(1).getString("name"));
            morning_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list1.get(1).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button morning_button3 = (Button)findViewById(R.id.morning_button3);
            morning_button3.setText(random_list1.get(2).getString("name"));
            morning_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list1.get(2).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button morning_button4 = (Button)findViewById(R.id.morning_button4);
            morning_button4.setText(random_list1.get(3).getString("name"));
            morning_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list1.get(3).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public void GetDaytimeActivitiesAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja = (JSONArray) jsonObject.get("daytime_activities");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject temp = ja.getJSONObject(i);
                activity_list.add(temp);
            }

            random_list2 = getRandomElement(activity_list,4);

            Button day_button1 = (Button)findViewById(R.id.day_button1);

            day_button1.setText(random_list2.get(02).getString("name"));
            day_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list2.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button day_button2 = (Button)findViewById(R.id.day_button2);
            day_button2.setText(random_list2.get(1).getString("name"));
            day_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list2.get(1).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button day_button3 = (Button)findViewById(R.id.day_button3);
            day_button3.setText(random_list2.get(2).getString("name"));
            day_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list2.get(2).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button day_button4 = (Button)findViewById(R.id.day_button4);
            day_button4.setText(random_list2.get(3).getString("name"));
            day_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list2.get(3).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public void GetNightActivitiesAndSetbutton(){
        String activity_json = loadJSONFromAsset();
        try{
            ArrayList<JSONObject> activity_list = new ArrayList<JSONObject>();
            JSONObject jsonObject = new JSONObject(activity_json);
            JSONArray ja = (JSONArray) jsonObject.get("night_activities");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject temp = ja.getJSONObject(i);
                activity_list.add(temp);
            }

            random_list3 = getRandomElement(activity_list,4);

            Button night_button1 = (Button)findViewById(R.id.night_button1);
            night_button1.setText(random_list3.get(0).getString("name"));
            night_button1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list3.get(0).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button night_button2 = (Button)findViewById(R.id.night_button2);
            night_button2.setText(random_list3.get(1).getString("name"));
            night_button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list3.get(1).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button night_button3 = (Button)findViewById(R.id.night_button3);
            night_button3.setText(random_list3.get(2).getString("name"));
            night_button3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list3.get(2).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            Button night_button4 = (Button)findViewById(R.id.night_button4);
            night_button4.setText(random_list3.get(3).getString("name"));
            night_button4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(main_menu.this, exercise.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",username);
                    try {
                        extras.putString("activity_name",random_list3.get(3).getString("name"));
                        intent.putExtras(extras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public ArrayList<JSONObject> getRandomElement(ArrayList<JSONObject> list, int totalItems){
        Random rand = new Random();

        // create a temporary list for storing
        // selected element
        ArrayList<JSONObject> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {

            // take a raundom index between 0 to size
            // of given List
            int randomIndex = rand.nextInt(list.size());

            // add element in temporary list
            newList.add(list.get(randomIndex));

            // Remove selected element from orginal list
            list.remove(randomIndex);
        }
        return newList;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("json.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}