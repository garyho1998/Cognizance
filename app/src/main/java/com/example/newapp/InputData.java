package com.example.newapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class InputData {

//    protected static List<Item> items = Arrays.asList(new Item("Candy"), new Item("Drink"), new Item("Soda"), new Item("Popcorn"), new Item("Snacks"));
    protected static List<Item> items;

    public static Map<User, HashMap<Item, Double>> initializeData(int numberOfUsers, JSONObject jo, String user) throws JSONException {
        Map<User, HashMap<Item, Double>> data = new HashMap<>();
        HashMap<Item, Double> newUser;
        Set<Item> newRecommendationSet;
        items = new ArrayList();
        items.clear();

        ArrayList<String> activity_list = new ArrayList<String>();
        JSONArray ja1 = (JSONArray) jo.get("monring_activities");
        JSONArray ja2 = (JSONArray) jo.get("night_activities");
        JSONArray ja3 = (JSONArray) jo.get("daytime_activities");
        for (int i = 0; i < ja1.length(); i++) {
            String temp = ja1.getJSONObject(i).getString("name");
            activity_list.add(temp);
        }
        for (int i = 0; i < ja2.length(); i++) {
            String temp = ja1.getJSONObject(i).getString("name");
            activity_list.add(temp);
        }
        for (int i = 0; i < ja3.length(); i++) {
            String temp = ja1.getJSONObject(i).getString("name");
            activity_list.add(temp);
        }
        for(String n : activity_list){
            System.out.println("n: "+n);
            items.add(new Item(n));
        }

        for (int i = 0; i < numberOfUsers; i++) {
            newUser = new HashMap<Item, Double>();
            newRecommendationSet = new HashSet<>();
            for (int j = 0; j < 15; j++) {
                newRecommendationSet.add(items.get((int) (Math.random() * items.size())));
            }
            for (Item item : newRecommendationSet) {
                newUser.put(item, Math.random());
            }
            data.put(new User("User" + i), newUser);
        }

        newUser = new HashMap<Item, Double>();
        newRecommendationSet = new HashSet<>();
        for (int j = 0; j < 5; j++) {
            newRecommendationSet.add(items.get((int) (Math.random() * items.size())));
        }
        for (Item item : newRecommendationSet) {
            newUser.put(item, Math.random());
        }
        data.put(new User(user), newUser);

        return data;
    }

}
