package com.example.newapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class exercise extends AppCompatActivity {

    String activity_name;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        String activity_name = getIntent().getExtras().getString("activity_name");
        username = getIntent().getExtras().getString("username");
        System.out.println("exercise:"+username );
        try{
            String activity_json = loadJSONFromAsset();
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
            String intro = "";
            for( JSONObject jo : activity_list){
                if(activity_name.contains(jo.getString("name"))){
                    intro = jo.getString("intro");
                    break;
                }
            }

            TextView title = (TextView)findViewById(R.id.title);
            title.setText(activity_name);
            TextView description = (TextView)findViewById(R.id.description);
            description.setText(intro);

            Button submit_button = (Button)findViewById(R.id.submit_button);
            submit_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(exercise.this, main_menu.class);
                    intent.putExtra("username", username);
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
}