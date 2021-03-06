package com.example.guestuser.findmyword;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Karol Zdebel on 2017-03-04.
 */

public class WordsJson {

    public WordsJson(Context context){
        this.context = context;
        try{
            wordsJson = new JSONObject(loadJSONFromAsset("words.json",context));
        }catch(org.json.JSONException e){
            wordsJson = null;
            Log.d("debug_tag","Could not get file");
            return;
        }
        leafs = new ArrayList<>();
        this.createJSONStructure();
        json = new JSONObject();
        this.json = this.createJson(jsonMain);
        this.FillLeafWords(leafs);

        Log.d("debug_tag","Final JSON File: "+json);
    }

    private String loadJSONFromAsset(String name, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(name);
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


    class JSONCategory{
        ArrayList<JSONCategory> subcategories;
        String name;

        public JSONCategory(String name){
            subcategories = new ArrayList<>();
            this.name = name;
        }
    }
    private JSONObject wordsJson;
    private Context context;
    private JSONCategory jsonMain;
    private JSONObject json;
    private ArrayList<JSONObject> leafs;
    private JSONObject jsonUse;

    public JSONObject getJson(){
        return json;
    }

    public void FillLeafWords(ArrayList<JSONObject> o){
        JSONObject jsonObj = wordsJson;

        JSONArray jsonarr = null;
        try{
            jsonarr = jsonObj.getJSONArray("Words");
        }catch(org.json.JSONException e){
            Log.d("debug_karol","failed to get words from json");
            return;
        }

        try{
            for (int i=0;i<jsonarr.length();i++){
                for (int j=0;j<o.size();j++){
                    if (o.get(j).get("Name").equals(jsonarr.getJSONObject(i).getString("Name"))){
                        o.get(j).put("Words",jsonarr.getJSONObject(i).getJSONArray("Words"));
                        Log.d("debug_karol","JSON Leaf after parsing dictionary:"+o.get(j).toString());
                    }
                }
            }
        }catch(org.json.JSONException e){
            Log.d("debug_karol","failed to get words from json");
        }

    }

    private void writeToFile(String string){
        String filename = "output.txt";
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JSONObject createJson(JSONCategory main){
        final JSONObject jsonObj = new JSONObject();

        try{
            //Insert name
            jsonObj.put("Name",main.name);

            //Check to see if categories exist if so recursive call

            Log.d("debug_karol","Size: "+main.subcategories.size());
            if (main.subcategories.size() > 0){
                JSONArray arr = new JSONArray();
                for (int i=0;i<main.subcategories.size();i++){
                    arr.put(createJson(main.subcategories.get(i)));
                }
                jsonObj.put("Categories",arr);
                Log.d("debug_karol","112: "+jsonObj.toString());
            }

            //Leaf node, call query for words unless hard-coded
            else{
                leafs.add(jsonObj);
            }

        }catch(org.json.JSONException e){
            Log.d("debug_karol","Error inputing json");
        }

        Log.d("debug_karol","returning json obj: "+jsonObj);

        return jsonObj;

    }

    private void createJSONStructure(){

        /*Main categories*/
        jsonMain = new JSONCategory("All");
        JSONCategory clothes = new JSONCategory("Clothes");
        JSONCategory activities = new JSONCategory("Activities");
        JSONCategory food = new JSONCategory("Food");
        JSONCategory places = new JSONCategory("Places");
        JSONCategory feelings = new JSONCategory("Feelings");
        JSONCategory people = new JSONCategory("People");

        /*Add main categories to jsonMain*/
        jsonMain.subcategories.add(clothes);
        jsonMain.subcategories.add(activities);
        jsonMain.subcategories.add(food);
        jsonMain.subcategories.add(places);
        jsonMain.subcategories.add(feelings);
        jsonMain.subcategories.add(people);

        /*Home categories*/
        JSONCategory kitchen = new JSONCategory("Kitchen");
        JSONCategory washroom = new JSONCategory("Washroom");
        JSONCategory garage = new JSONCategory("Garage");
        JSONCategory bedroom = new JSONCategory("Bedroom");
        JSONCategory backyard = new JSONCategory("Backyard");

        /*Clothes*/
        JSONCategory jacket = new JSONCategory("Jacket");
        JSONCategory shirt = new JSONCategory("Shirt");
        JSONCategory pants = new JSONCategory("Pants");

        /*Activities*/
        JSONCategory sports = new JSONCategory("Sports");
        JSONCategory eat = new JSONCategory("Eat");

        /*Food*/
        JSONCategory meat = new JSONCategory("Meat");
        JSONCategory fruit = new JSONCategory("Fruit");
        JSONCategory vegetable = new JSONCategory("Vegetable");
        JSONCategory candy = new JSONCategory("Candy");
        JSONCategory soup = new JSONCategory("Soup");

        /*Places*/
        JSONCategory restaurant = new JSONCategory("Restaurant");
        JSONCategory park = new JSONCategory("Park");
        JSONCategory home = new JSONCategory("Home");
        JSONCategory church = new JSONCategory("Church");

        /*Feelings*/
        JSONCategory anger = new JSONCategory("Anger");
        JSONCategory sad = new JSONCategory("Sad");
        JSONCategory happy = new JSONCategory("Happy");

        /*People*/
        JSONCategory  family = new JSONCategory("Family");
        JSONCategory politician = new JSONCategory("Politician");
        JSONCategory caregiver = new JSONCategory("Caregiver");

        /*Kitchen*/
        JSONCategory appliances = new JSONCategory("Appliances");
        JSONCategory kitchenUtensils = new JSONCategory("Kitchen Utensils");

        /*Washroom*/
        JSONCategory toiletries = new JSONCategory("Toiletries");
        JSONCategory bathroomAppliances = new JSONCategory("Bathroom");

        /*Add subcategories to home categories*/
        home.subcategories.add(kitchen);
        home.subcategories.add(washroom);
        home.subcategories.add(garage);
        home.subcategories.add(bedroom);
        home.subcategories.add(backyard);

        /*Add subcategories to clothes categories*/
        clothes.subcategories.add(jacket);
        clothes.subcategories.add(shirt);
        clothes.subcategories.add(pants);

        /*Add subcategories to clothes categories*/
        activities.subcategories.add(washroom);
        activities.subcategories.add(sports);
        activities.subcategories.add(eat);

        /*Add subcategories to Food*/
        food.subcategories.add(meat);
        food.subcategories.add(fruit);
        food.subcategories.add(vegetable);
        food.subcategories.add(candy);
        food.subcategories.add(soup);

        /*Add subcategories to Places*/
        places.subcategories.add(restaurant);
        places.subcategories.add(park);
        places.subcategories.add(home);
        places.subcategories.add(church);

        /*Add subcategories to people*/
        people.subcategories.add(family);
        people.subcategories.add(politician);
        people.subcategories.add(caregiver);

        /*Add subcategories to Kitchen*/
        kitchen.subcategories.add(appliances);
        kitchen.subcategories.add(kitchenUtensils);
        kitchen.subcategories.add(food);

        /*Add subcategories to washroom*/
        washroom.subcategories.add(toiletries);
        washroom.subcategories.add(bathroomAppliances);

        /*Add subcategories to eat*/
        eat.subcategories.add(food);
        eat.subcategories.add(restaurant);
    }

}