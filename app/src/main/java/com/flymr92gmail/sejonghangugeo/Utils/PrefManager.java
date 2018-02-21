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
        Log.d(TAG, "getDate "+preferences.getString(DATE_LEGEND, "0"));
        return preferences.getString(DATE_LEGEND, "0");
    }

    public void setDateOfAddedLegend(String newDate){
        Log.d(TAG, "setDate: "+ newDate);
        editor.putString(DATE_LEGEND, newDate);
        editor.apply();
        editor.commit();
    }

    public String getAddedLegendsId(){
        Log.d(TAG, "getIds: "+preferences.getString(ADDED_LEGENDS_ID, "0"));
        return preferences.getString(ADDED_LEGENDS_ID, "0");
    }

    public void setAddedLegendsId(String addedArray){
        Log.d(TAG, "setIds: "+ addedArray);
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

   public boolean getIsFirstAppActivation(){
        return preferences.getBoolean(FIRST_APP_ACTIVATION, true);
   }

   public void setIsFirstAppActivation(boolean b){
       editor.putBoolean(FIRST_APP_ACTIVATION, b);
       editor.apply();
       editor.commit();
   }
}
