package com.flymr92gmail.sejonghangugeo.POJO;

import java.io.Serializable;


public class Lesson  implements Serializable {
    private String mLessonName;
    private String mLessonTable;
    private int mLessonId;
    private int mPositionIndex;
    private int mLessonTabIndex;
    private int currentLanguage;
    private int currentLanguageCards;
    private int lessonProgress;
    private String dateOfCreated;
    public String getLessonName() {
        return mLessonName;
    }

    public void setLessonName(String mLessonName) {
        this.mLessonName = mLessonName;
    }

    public String getLessonTable() {
        return mLessonTable;
    }

    public void setLessonTable(String mLessonTable) {
        this.mLessonTable = mLessonTable;
    }

    public int getLessonId() {
        return mLessonId;
    }

    public void setLessonId(int mLessonId) {
        this.mLessonId = mLessonId;
    }

    public int getPositionIndex(){return mPositionIndex;}

    public void setPositionIndex(int mPositionIndex){this.mPositionIndex = mPositionIndex;}

    public void setLessonTabIndex(int mLessonTabIndex){ this.mLessonTabIndex = mLessonTabIndex;}

    public int getLessonTabIndex(){return mLessonTabIndex;}

    public int getCurrentLanguage(){
        return currentLanguage;
    }

    public void setCurrentLanguage(int currentLanguage){
        this.currentLanguage = currentLanguage;
    }

    public void setCurrentLanguageCards(int currentLanguageCards){
        this.currentLanguageCards = currentLanguageCards;
    }

    public int getCurrentLanguageCards(){return currentLanguageCards;}

    public void setLessonProgress(int lessonProgress){this.lessonProgress = lessonProgress;}

    public int getLessonProgress(){
        return lessonProgress;
    }

    public void setDateOfCreated(String dateOfCreated){this.dateOfCreated = dateOfCreated;}

    public String getDateOfCreated(){return dateOfCreated;}

}
