package com.flymr92gmail.sejonghangugeo.Utils;

import com.flymr92gmail.sejonghangugeo.POJO.Word;

import java.util.ArrayList;

/**
 * Created by hp on 13.02.2018.
 */

public class WordsHelper {
    public static int getLessonProgress(ArrayList<Word> words){
        float progress;
        float i = 0.0f;
        for (Word word : words){
            i = i + (2.0f/(word.getMissCount()+2.0f));
        }
        progress = i/words.size()*100;
        return (int)progress;
    }

    public static int getLessonProgress(ArrayList<Word> words, int oldProgress){
        float progress;
        float i = 0.0f;
        for (Word word : words){
            i = i + (2.0f/(word.getMissCount()+2.0f));
        }
        progress = i/(words.size()+oldProgress)*100;
        return (int)progress;
    }
}
