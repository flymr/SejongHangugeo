package com.flymr92gmail.sejonghangugeo.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;


import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Test;
import com.flymr92gmail.sejonghangugeo.POJO.Word;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Helper {
    public static Drawable getImageFromAssets(int imageId, Context context){
        Drawable d= null;
        // load image
        try {
            // get input stream
            InputStream ims = context.getAssets().open("images/"+imageId+".jpg");
            // load image as Drawable
             d = Drawable.createFromStream(ims, null);
            // set image to ImageView
        }
        catch(IOException ex) {
            return null;
        }
        return d;
    }
    public static int getTrackId(Context context, Test test, SoundPool sp){
        int trackId;
        trackId = 0;
        try {
            trackId = sp.load(context.getAssets().openFd("Audio/"+test.getTrackId()+".wma"),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trackId;
    }
    public static String getTrackAssetString(Test test){
        return "audio/"+test.getTrackId()+".wma";
    }
    public static ArrayList<Word> randomizeArray(ArrayList<Word> words){
        long seed = System.nanoTime();
        Collections.shuffle(words,new Random(seed));
        return words;
    }
    public static Drawable getImageFromAssetsPng(int imageId, Context context){
        Drawable d= null;
        // load image
        try {
            // get input stream
            InputStream ims = context.getAssets().open("images/"+imageId+".png");
            // load image as Drawable
            d = Drawable.createFromStream(ims, null);
            // set image to ImageView
        }
        catch(IOException ex) {
            return null;
        }
        return d;
    }

    public void wordComponentEditor(String userAnswer, String correctAnswer, int index){

    }



}
