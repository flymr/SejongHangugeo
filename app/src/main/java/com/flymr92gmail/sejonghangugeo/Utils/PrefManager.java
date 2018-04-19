package com.flymr92gmail.sejonghangugeo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;
import java.util.logging.StreamHandler;

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

    public int getLastLegendsPoss(){
        return  preferences.getInt(LAST_LEGEND_POSS, 0);
    }

    public void setLastLegendsPoss(int position){
        editor.putInt(LAST_LEGEND_POSS, position);
        editor.apply();
        editor.commit();
    }

    public String getMailTitle(){
        return preferences.getString(MAIL_TITLE, "");
    }

    public void setMailTitle(String mailTitle){
        editor.putString(MAIL_TITLE, mailTitle);
        editor.apply();
        editor.commit();
    }

    public String getMailBody(){
        return preferences.getString(MAIL_BODY, "");
    }

    public void setMailBody(String mailBody){
        editor.putString(MAIL_BODY, mailBody);
        editor.apply();
        editor.commit();
    }

    public void setUserMail(String mail){
        editor.putString(USER_MAIL, mail);
        editor.apply();
        editor.commit();
    }

    public String getUserMail(){
        return preferences.getString(USER_MAIL, "");
    }

    public void setAppTheme(int themeIndex){
        editor.putInt(APP_THEME, themeIndex);
        editor.apply();
        editor.commit();
    }

    public int getAppTheme(){
        return preferences.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    public void saveLastLessonID(int lessonID){
        editor.putInt(LAST_LESSON_ID, lessonID);
        editor.apply();
        editor.commit();
    }

    public int getLastLessonID(){
        return preferences.getInt(LAST_LESSON_ID, 0);
    }

    public void saveLastBookPage(int pageNumber){
        Log.d(TAG, "saveLastBookPage: "+pageNumber);
        editor.putInt(BOOK,pageNumber);
        editor.apply();
        editor.commit();
    }
    public int getLastBookPage(){
        Log.d(TAG, "getLastBookPage: "+preferences.getInt(BOOK,32));
        return preferences.getInt(BOOK,36);
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
