package com.flymr92gmail.sejonghangugeo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;

public class PrefManager implements Constants {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    String TAG = "PREF";
    public PrefManager(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void saveLastBookPage(int pageNumber){
        Log.d(TAG, "saveLastBookPage: "+pageNumber);
        editor.putInt(BOOK,pageNumber);
        editor.apply();
        editor.commit();
    }
    public int getLastBookPage(){
        Log.d(TAG, "getLastBookPage: "+preferences.getInt(BOOK,32));
        return preferences.getInt(BOOK,34);
    }

    public String getDateOfAddedLegend(){
        String currentDateTimeString = (String) DateFormat.format("dd-MM-yyyy",new Date());
        return preferences.getString(DATE_LEGEND, currentDateTimeString);
    }

    public void setDateOfAddedLegend(String newDate){
        editor.putString(DATE_LEGEND, newDate);
        editor.apply();
        editor.commit();
    }

    public String getAddedLegendsId(){
        return preferences.getString(ADDED_LEGENDS_ID, "0");
    }

    public void setAddedLegendsId(String addedArray){
        editor.putString(ADDED_LEGENDS_ID, addedArray);
        editor.apply();
        editor.commit();
    }

    public void  saveLastGramPage(int pageNumber){
        editor.putInt(GRAM,pageNumber);
        editor.apply();
        editor.commit();
    }
    public int getLastGramPage(){
        return preferences.getInt(GRAM,0);
    }

   public int getCurrentLearnLanguage() {

       return preferences.getInt(LANGUAGE_LEARN, 1);
   }

    public void saveCurrentLearnLanguage(int number){
        editor.putInt(LANGUAGE_LEARN, number);
        editor.apply();
        editor.commit();
    }



}
