package com.flymr92gmail.sejonghangugeo.DataBases.External;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.POJO.Test;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.POJO.Audio;
import com.flymr92gmail.sejonghangugeo.Utils.Constants;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;


public class AppDataBase extends SQLiteAssetHelper implements Constants {
    SQLiteDatabase db;
    private static final String DATABASE_NAME = "vordsbase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_WORDS = "words";
    private static final String TABLE_TESTS = "exercise";
    private static final String TABLE_AUDIO = "audio";
    private static final String TABLE_LEGENDS= "legends";

    public AppDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getReadableDatabase();
    }
    public ArrayList<Word> getAllWords() {
        ArrayList<Word> arrayList = new ArrayList<>();
        String[] columns = {"_id", "korean", "russian","page"};
        Cursor cursor = db.query(TABLE_WORDS, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            word.setPage(cursor.getInt(cursor.getColumnIndex("page")));
            word.setRussianWord(cursor.getString(cursor.getColumnIndex("russian")));
            word.setKoreanWord(cursor.getString(cursor.getColumnIndex("korean")));
            arrayList.add(word);
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Legend> getLegends(){
        ArrayList<Legend> legends = new ArrayList<>();
        String[] columns = {"_id", "header", "headerTranslate","legendText"};
        Cursor cursor = db.query(TABLE_LEGENDS, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Legend legend = new Legend();
            legend.setmId(cursor.getInt(cursor.getColumnIndex("_id")));
            legend.setName(cursor.getString(cursor.getColumnIndex("header")));
            legend.setNameTranslate(cursor.getString(cursor.getColumnIndex("headerTranslate")));
            legend.setLegendText(cursor.getString(cursor.getColumnIndex("legendText")));
            legends.add(legend);
        }
        cursor.close();
        return legends;
    }

    public ArrayList<Word> getSearchResult(String searchWord, Language language) {
        ArrayList<Word> words = new ArrayList<>();
        Cursor cursorWord = null;
        String myQuery=null;
            if (language== Language.Russian){
                myQuery = "SELECT * FROM words WHERE " + "russian" + " LIKE '%"+ searchWord +"%'";
            }
            else {
                myQuery = "SELECT * FROM words WHERE " + "korean" + " LIKE '%" + searchWord + "%'";
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


}
