package com.flymr92gmail.sejonghangugeo.Utils;

public interface Constants {
    //Preferences:
    String LANGUAGE_LEARN = "word_numb";
    String PREF_NAME = "koreanBookpref";
    String BOOK = "book_";
    String GRAM = "gram";
    String DATE_LEGEND = "date_legend";
    String ADDED_LEGENDS_ID = "added_legends_id";
    String FIRST_APP_ACTIVATION = "first_app_activation";
    String LAST_LESSON_ID = "last_lesson_id";
    String APP_THEME = "app_theme";
    String USER_MAIL = "user_mail";
    String MAIL_BODY = "mail_body";
    String MAIL_TITLE = "mail_title";
    String LAST_LEGEND_POSS = "last_legends_poss";
    //Data Bases
    final String TABLE_FAVORITE = "favorite";
    final String CREATE_TABLE_FAVORITE = "CREATE TABLE "+TABLE_FAVORITE+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "primaryId INTEGER," +
            "russian TEXT," +
            "korean TEXT);";

    String USER_LESSONS_TABLE = "userlessonstable";
    String CREATE_LESSONS_TABLE = "CREATE TABLE "+USER_LESSONS_TABLE+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "lessonName TEXT," +
            "lessonTable TEXT,"+
            "dateOfCreated TEXT,"+
            "lessonTabIndex INTEGER,"+
            "currentLanguage INTEGER,"+
            "currentLanguageCards INTEGER,"+
            "lessonProgress INTEGER,"+
            "positionIndex INTEGER);";
    //Dictionary
    public enum Language{
        Korean,Russian
    }


}
