package com.flymr92gmail.sejonghangugeo.DataBases.External;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.flymr92gmail.sejonghangugeo.POJO.AudioTest;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.POJO.Test;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.POJO.Audio;
import com.flymr92gmail.sejonghangugeo.Utils.Constants;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;


public class AppDataBase extends SQLiteAssetHelper implements Constants {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "vordsbase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_WORDS = "words";
    private static final String TABLE_TESTS = "exercise";
    private static final String TABLE_AUDIO = "audio";
    private static final String TABLE_LEGENDS= "legends";
    private static final String TABLE_AUDIO_TESTS = "exerciseAudio";

    public AppDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getReadableDatabase();
    }

    public ArrayList<Legend> getLegends(){
        ArrayList<Legend> legends = new ArrayList<>();
        String[] columns = {"_id", "header", "headerTranslate","legendText", "category"};
        Cursor cursor = db.query(TABLE_LEGENDS, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Legend legend = new Legend();
            legend.setmId(cursor.getInt(cursor.getColumnIndex("_id")));
            legend.setName(cursor.getString(cursor.getColumnIndex("header")));
            legend.setNameTranslate(cursor.getString(cursor.getColumnIndex("headerTranslate")));
            legend.setLegendText(cursor.getString(cursor.getColumnIndex("legendText")));
            legend.setLegendCategory(cursor.getString(cursor.getColumnIndex("category")));
            legends.add(legend);
        }
        cursor.close();
        return legends;
    }

    public Legend getDailyLegend (int position){
        Legend legend = new Legend();
        String[] columns = {"_id", "header", "headerTranslate","legendText", "category"};
        Cursor cursor = db.query(TABLE_LEGENDS, columns, null, null, null, null, null);
         cursor.moveToPosition(position);
            legend.setmId(cursor.getInt(cursor.getColumnIndex("_id")));
            legend.setName(cursor.getString(cursor.getColumnIndex("header")));
            legend.setNameTranslate(cursor.getString(cursor.getColumnIndex("headerTranslate")));
            legend.setLegendText(cursor.getString(cursor.getColumnIndex("legendText")));
            legend.setLegendCategory(cursor.getString(cursor.getColumnIndex("category")));



        cursor.close();
        return legend;
    }


    public ArrayList<Word> getSearchResult(String searchWord, Language language) {
        ArrayList<Word> words = new ArrayList<>();
        Cursor cursorWord;
        String myQuery;
            if (language== Language.Russian){
                myQuery = "SELECT * FROM " +TABLE_WORDS + " WHERE " + "russian" + " LIKE '%"+ searchWord +"%'";
            }
            else {
                myQuery = "SELECT * FROM " +TABLE_WORDS + " WHERE " + "korean" + " LIKE '%" + searchWord + "%'";
            }
        cursorWord=db.rawQuery(myQuery,null);
            while (cursorWord.moveToNext()){
                Word word = new Word();
                word.setId(cursorWord.getInt(cursorWord.getColumnIndex("_id")));
                word.setRussianWord(cursorWord.getString(cursorWord.getColumnIndex("russian")));
                word.setKoreanWord(cursorWord.getString(cursorWord.getColumnIndex("korean")));
                words.add(word);
                Log.d("DBLOG", "getSearchResult: " +word.getKoreanWord());

            }
        cursorWord.close();
    return words;
}
    public ArrayList<Word> getPageWords(int page){
        ArrayList<Word> wordArrayList = new ArrayList<>();
        String query = "SELECT * FROM words WHERE page="+page;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            word.setPage(cursor.getInt(cursor.getColumnIndex("page")));
            word.setRussianWord(cursor.getString(cursor.getColumnIndex("russian")));
            word.setKoreanWord(cursor.getString(cursor.getColumnIndex("korean")));
            wordArrayList.add(word);
        }
        cursor.close();
        return wordArrayList;
    }
    public ArrayList<Word> getGramPageWords(int page ){
        ArrayList<Word> wordArrayList = new ArrayList<>();
        String query = "SELECT * FROM words WHERE page2="+page;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            word.setPage(cursor.getInt(cursor.getColumnIndex("page")));
            word.setRussianWord(cursor.getString(cursor.getColumnIndex("russian")));
            word.setKoreanWord(cursor.getString(cursor.getColumnIndex("korean")));
            wordArrayList.add(word);
        }
        cursor.close();
        return wordArrayList;
    }
    public Audio getPageAudio(int page){
        String query = "SELECT * FROM "+TABLE_AUDIO+" WHERE page="+page;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            Audio audio = new Audio();
            audio.setmId(cursor.getInt(cursor.getColumnIndex("_id")));
            audio.setmPage(page);
            audio.setmTrackId(cursor.getInt(cursor.getColumnIndex("track")));
            return audio;
        }
        cursor.close();
        return null;
    }
    public ArrayList<Test> getTest(int page){
        ArrayList<Test> tests = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_TESTS+" WHERE page="+page;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            Test test = new Test();
            test.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            test.setPage(cursor.getInt(cursor.getColumnIndex("page")));
            test.setFirstAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            test.setSecondAnswer(cursor.getString(cursor.getColumnIndex("answer2")));
            test.setImage(cursor.getInt(cursor.getColumnIndex("image")));
            test.setTrackId(cursor.getInt(cursor.getColumnIndex("track")));
            test.setFirstAnothAnswer(cursor.getString(cursor.getColumnIndex("anothAnsver")));
            test.setSecondAnothAnswer(cursor.getString(cursor.getColumnIndex("anothAnsver2")));
            test.setFirstHint(cursor.getString(cursor.getColumnIndex("hint")));
            test.setSecondHint(cursor.getString(cursor.getColumnIndex("hint2")));
            Log.w("DATABASELOG",test.getTrackId()+" kek");
            tests.add(test);
        }
        cursor.close();
        return tests;
    }


    public ArrayList<AudioTest> getAudioTest(int page){
        ArrayList<AudioTest> tests = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_AUDIO_TESTS+" WHERE page="+page;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            AudioTest test = new AudioTest();
            test.setmId(cursor.getInt(cursor.getColumnIndex("_id")));
            test.setmTestType(cursor.getInt(cursor.getColumnIndex("testType")));
            test.setmImageCount(cursor.getInt(cursor.getColumnIndex("imageCount")));
            test.setmFirstAnswer(cursor.getInt(cursor.getColumnIndex("answer")));
            test.setmFirstAnswer2(cursor.getInt(cursor.getColumnIndex("answer2")));
            test.setmFirstAnswer3(cursor.getInt(cursor.getColumnIndex("answer3")));
            test.setmTextAnswer(cursor.getString(cursor.getColumnIndex("textAnswer")));
            test.setmTrackId(cursor.getInt(cursor.getColumnIndex("track")));
            test.setmTextAudio(cursor.getString(cursor.getColumnIndex("text")));
            tests.add(test);
        }
        cursor.close();
        return tests;
    }

}
