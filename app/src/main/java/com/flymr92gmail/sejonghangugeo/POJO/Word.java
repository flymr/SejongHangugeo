package com.flymr92gmail.sejonghangugeo.POJO;

import java.io.Serializable;


public class Word implements Serializable {
    private String mRussianWord;
    private String mKoreanWord;
    private int mPage;
    private int mId;
    private int mCorrectCount = 0;
    private int mIsSelected = 0;
    private int mMissCount = 0;
    private int mIsLearning = 0;

    public int isSelected() {
        return mIsSelected;
    }

    public void setSelected(int mIsSelected) {
        this.mIsSelected = mIsSelected;
    }


    public int getCorrectCount() {
        return mCorrectCount;
    }

    public void setCorrectCount(int mCorrectCount) {
        this.mCorrectCount = mCorrectCount;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getRussianWord() {
        return mRussianWord;
    }

    public void setRussianWord(String mRussianWord) {
        this.mRussianWord = mRussianWord;
    }

    public String getKoreanWord() {
        return mKoreanWord;
    }

    public void setKoreanWord(String mKoreanWord) {
        this.mKoreanWord = mKoreanWord;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int mPage) {
        this.mPage = mPage;
    }

    public int getMissCount() {
        return mMissCount;
    }

    public void setMissCount(int mMissCount) {
        this.mMissCount = mMissCount;
    }

    public int getmIsLearning() {return mIsLearning;
    }

    public void setmIsLearning(int mIsLearning) {
        this.mIsLearning = mIsLearning;
    }
}
