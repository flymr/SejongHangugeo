package com.flymr92gmail.sejonghangugeo.DataBases.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;


import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.POJO.Audio;
import com.flymr92gmail.sejonghangugeo.Utils.Constants;

public class UserDataBase extends SQLiteOpenHelper implements Constants{
    private SQLiteDatabase db;
    public UserDataBase(Context context) {
        super(context, "userdatabase", null, 1);
        db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LESSONS_TABLE);
    }

    private void createNewLessonTable(String tableName){

        db.execSQL("CREATE TABLE "+tableName+" (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "primaryId INTEGER," +
                "russian TEXT," +
                "korean TEXT," +
                "correctCount INTEGER," +
                "isSelected INTEGER," +
                "isLearning INTEGER," +
                "positionInCardAction INTEGER," +
                "missCount INTEGER);");
    }
    private void deleteLessonTable(String tableName){
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
   /*public ArrayList<Lesson> getAllLessons(){
        ArrayList<Lesson> lessons = new ArrayList<>();
        String[] columns = {"_id", "lessonName","lessonTable"};
        Cursor cursor = db.query(USER_LESSONS_TABLE,columns,null,null,null,null,null);
        while (cursor.moveToNext()){
            Lesson lesson = new Lesson();
            lesson.setLessonId(cursor.getInt(cursor.getColumnIndex("_id")));
            lesson.setLessonName(cursor.getString(cursor.getColumnIndex("lessonName")));
            lesson.setLessonTable(cursor.getString(cursor.getColumnIndex("lessonTable")));
            lessons.add(lesson);
        }
        cursor.close();
        return lessons;
    }*/
   public ArrayList<Lesson> getAllLessons(){
       ArrayList<Lesson> lessons = new ArrayList<>();
       String query = "SELECT * FROM " + USER_LESSONS_TABLE + " ORDER BY positionIndex";
       Cursor cursor = db.rawQuery(query,null);
       while (cursor.moveToNext()){
           Lesson lesson = new Lesson();
           lesson.setLessonId(cursor.getInt(cursor.getColumnIndex("_id")));
           lesson.setLessonName(cursor.getString(cursor.getColumnIndex("lessonName")));
           lesson.setLessonTable(cursor.getString(cursor.getColumnIndex("lessonTable")));
           lesson.setDateOfCreated(cursor.getString(cursor.getColumnIndex("dateOfCreated")));
           lesson.setLessonTabIndex(cursor.getInt(cursor.getColumnIndex("lessonTabIndex")));
           lesson.setCurrentLanguage(cursor.getInt(cursor.getColumnIndex("currentLanguage")));
           lesson.setCurrentLanguageCards(cursor.getInt(cursor.getColumnIndex("currentLanguageCards")));
           lesson.setLessonProgress(cursor.getInt(cursor.getColumnIndex("lessonProgress")));
           lesson.setPositionIndex(cursor.getInt(cursor.getColumnIndex("positionIndex")));
           lessons.add(lesson);
       }
       cursor.close();
       return lessons;
   }

    public Lesson getLessonByPrimaryId(int primaryId){
        Lesson lesson = new Lesson();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_LESSONS_TABLE + " where _id = " + primaryId, null);
        if (cursor.moveToNext()) {
            lesson.setLessonId(cursor.getInt(cursor.getColumnIndex("_id")));
            lesson.setLessonName(cursor.getString(cursor.getColumnIndex("lessonName")));
            lesson.setLessonTable(cursor.getString(cursor.getColumnIndex("lessonTable")));
            lesson.setDateOfCreated(cursor.getString(cursor.getColumnIndex("dateOfCreated")));
            lesson.setLessonTabIndex(cursor.getInt(cursor.getColumnIndex("lessonTabIndex")));
            lesson.setCurrentLanguage(cursor.getInt(cursor.getColumnIndex("currentLanguage")));
            lesson.setCurrentLanguageCards(cursor.getInt(cursor.getColumnIndex("currentLanguageCards")));
            lesson.setLessonProgress(cursor.getInt(cursor.getColumnIndex("lessonProgress")));
            lesson.setPositionIndex(cursor.getInt(cursor.getColumnIndex("positionIndex")));

        }
        cursor.close();
        return lesson;

    }
    //Методы для создания и удаления урока
    public void createNewLesson(String lessonName){
        Random random = new Random();
        //генерируем название таблици, изменяя заглавные буквы на маленькие(метод toLowerCase) и удаляем пробелы(метод replaseAll)
        String lessonTableName = "les"+lessonName.toLowerCase().replaceAll(" ","")+random.nextInt(1000000);
        //создаем таблицу со словами
        createNewLessonTable(lessonTableName);
        //добавляем название таблици урока в таблицу с уроками
        String currentDateTimeString = (String) DateFormat.format("dd-MM-yyyy kk:mm:ss",new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("lessonName",lessonName);
        contentValues.put("lessonTable",lessonTableName);
        contentValues.put("dateOfCreated", currentDateTimeString);
        contentValues.put("lessonTabIndex", 0);
        contentValues.put("currentLanguage", 0);
        contentValues.put("currentLanguageCards", 0);
        contentValues.put("lessonProgress", 0);
        contentValues.put("positionIndex", getAllLessons().size());
        db.insertWithOnConflict(USER_LESSONS_TABLE,null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteLessonWord(String lessonTableName, Word word){
        db.delete(lessonTableName,"_id="+word.getId(),null);
    }
    public void deleteLesson(Lesson lesson){
        db.execSQL("DROP TABLE IF EXISTS "+lesson.getLessonTable());
        db.delete(USER_LESSONS_TABLE,"_id ="+lesson.getLessonId(),null);
    }
    public  void editLessonName(Lesson lesson,String name){
        ContentValues cv = new ContentValues();
        cv.put("lessonName",name);
        db.update(USER_LESSONS_TABLE,cv,"_id="+lesson.getLessonId(),null);
    }
    public void editCurrentLanguage(Lesson lesson){
        ContentValues cv = new ContentValues();
        cv.put("currentLanguage", lesson.getCurrentLanguage());
        db.update(USER_LESSONS_TABLE,cv,"_id="+lesson.getLessonId(),null);
    }
    public void editCurrentLanguagCards(Lesson lesson){
        ContentValues cv = new ContentValues();
        cv.put("currentLanguageCards", lesson.getCurrentLanguageCards());
        db.update(USER_LESSONS_TABLE,cv,"_id="+lesson.getLessonId(),null);
    }

    public void editLessonProgress(Lesson lesson){
        ContentValues cv = new ContentValues();
        cv.put("lessonProgress", lesson.getLessonProgress());
        db.update(USER_LESSONS_TABLE,cv,"_id="+lesson.getLessonId(),null);

    }

    public void addNewWord(String tableName, Word word){
        ContentValues contentValues = new ContentValues();
        contentValues.put("russian",word.getRussianWord());
        contentValues.put("korean",word.getKoreanWord());
        contentValues.put("primaryId",word.getId());
        db.insertWithOnConflict(tableName,null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void editWord(String tableName, Word word){
        ContentValues cv = new ContentValues();
        cv.put("korean", word.getKoreanWord());
        cv.put("russian", word.getRussianWord());
        db.update(tableName,cv,"_id="+word.getId(),null);
    }

    public void addNewWords(String tableName, ArrayList<Word> words){
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put("russian",word.getRussianWord());
            contentValues.put("korean",word.getKoreanWord());
            contentValues.put("primaryId",word.getId());
            db.insertWithOnConflict(tableName,null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }
    public void deleteWord(String tableName, int wordId){
        db.delete(tableName,"wordId = " + wordId,null);
    }
    public ArrayList<Word> getWordsInLesson(String tableName){
        ArrayList<Word> words = new ArrayList<>();
        String[] columns = {"_id", "korean", "russian","primaryId","correctCount","isSelected","isLearning","positionInCardAction","missCount"};
        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            word.setPage(cursor.getInt(cursor.getColumnIndex("primaryId")));
            word.setRussianWord(cursor.getString(cursor.getColumnIndex("russian")));
            word.setKoreanWord(cursor.getString(cursor.getColumnIndex("korean")));
            word.setCorrectCount(cursor.getInt(cursor.getColumnIndex("correctCount")));
            word.setSelected(cursor.getInt(cursor.getColumnIndex("isSelected")));
            word.setmIsLearning(cursor.getInt(cursor.getColumnIndex("isLearning")));
            word.setPositionInCardAction(cursor.getInt(cursor.getColumnIndex("positionInCardAction")));
            word.setMissCount(cursor.getInt(cursor.getColumnIndex("missCount")));
            words.add(word);
        }
        cursor.close();
        return words;
    }

    public void editLessonsPositionInArray(Lesson lesson){
        ContentValues cv = new ContentValues();
        cv.put("positionIndex", lesson.getPositionIndex());
        db.update(USER_LESSONS_TABLE, cv, "_id="+lesson.getLessonId(),null);
    }

    public void editLessonTabIndex(Lesson lesson){
        ContentValues cv = new ContentValues();
        cv.put("lessonTabIndex", lesson.getLessonTabIndex());
        db.update(USER_LESSONS_TABLE, cv, "_id="+lesson.getLessonId(), null);
    }

    public void editWordCorrectCount(Lesson lesson, Word word){
        ContentValues cv = new ContentValues();
        cv.put("correctCount",word.getCorrectCount());

        db.update(lesson.getLessonTable(),cv,"_id="+word.getId(),null);
    }

    public void editWordMissCount(Lesson lesson, Word word){
        ContentValues cv = new ContentValues();
        cv.put("missCount", word.getMissCount());
        db.update(lesson.getLessonTable(),cv,"_id="+word.getId(),null);
    }

   public void editWordPositionInCardAction(Lesson lesson, Word word){
       ContentValues cv = new ContentValues();
       cv.put("positionInCardAction", word.getPositionInCardAction());
       db.update(lesson.getLessonTable(),cv,"_id="+word.getId(),null);

   }

    public void editWordSelect(Lesson lesson, Word word){
        ContentValues cv = new ContentValues();
        cv.put("isSelected",word.isSelected());
        db.update(lesson.getLessonTable(),cv,"_id="+word.getId(),null);
    }
    public int getCountOfSelectedWords(Lesson lesson){
        int count = 0;
        String[] columns = {"isSelected"};
        Cursor cursor = db.query(lesson.getLessonTable(),columns,null,null,null,null,null);
        while (cursor.moveToNext()){
            if (cursor.getInt(cursor.getColumnIndex("isSelected"))==1){
                count++;
            }
        }
        cursor.close();
        return count;
    }


    public ArrayList<Word> getSelectedWordsInLesson(Lesson lesson){
        ArrayList<Word> words = new ArrayList<>();
        String[] columns = {"_id", "korean", "russian","primaryId","correctCount","isSelected","isLearning","positionInCardAction","missCount"};
        Cursor cursor = db.query(lesson.getLessonTable(), columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            word.setPage(cursor.getInt(cursor.getColumnIndex("primaryId")));
            word.setRussianWord(cursor.getString(cursor.getColumnIndex("russian")));
            word.setKoreanWord(cursor.getString(cursor.getColumnIndex("korean")));
            word.setCorrectCount(cursor.getInt(cursor.getColumnIndex("correctCount")));
            word.setSelected(cursor.getInt(cursor.getColumnIndex("isSelected")));
            word.setmIsLearning(cursor.getInt(cursor.getColumnIndex("isLearning")));
            word.setPositionInCardAction(cursor.getInt(cursor.getColumnIndex("positionInCardAction")));
            word.setMissCount(cursor.getInt(cursor.getColumnIndex("missCount")));
            if (word.isSelected()==1)
            words.add(word);
        }
        cursor.close();
        return words;
    }

    public void editWordLearning(Lesson lesson, Word word){
        ContentValues cv = new ContentValues();
        cv.put("isLearning",word.getmIsLearning());
        db.update(lesson.getLessonTable(),cv,"_id="+word.getId(),null);
    }

    public ArrayList<Word> getLearningWord(Lesson lesson){
        ArrayList<Word> words = new ArrayList<>();
        String[] columns = {"_id", "korean", "russian","primaryId","correctCount","isSelected","isLearning","positionInCardAction","missCount"};
        Cursor cursor = db.query(lesson.getLessonTable(), columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            word.setPage(cursor.getInt(cursor.getColumnIndex("primaryId")));
            word.setRussianWord(cursor.getString(cursor.getColumnIndex("russian")));
            word.setKoreanWord(cursor.getString(cursor.getColumnIndex("korean")));
            word.setCorrectCount(cursor.getInt(cursor.getColumnIndex("correctCount")));
            word.setSelected(cursor.getInt(cursor.getColumnIndex("isSelected")));
            word.setmIsLearning(cursor.getInt(cursor.getColumnIndex("isLearning")));
            word.setPositionInCardAction(cursor.getInt(cursor.getColumnIndex("positionInCardAction")));
            word.setMissCount(cursor.getInt(cursor.getColumnIndex("missCount")));
            if (word.getmIsLearning()==1)
                words.add(word);
        }
        cursor.close();
        return words;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
