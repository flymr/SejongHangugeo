package com.flymr92gmail.sejonghangugeo.Utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by hp on 12.02.2018.
 */

public class WordsSpeech implements TextToSpeech.OnInitListener{
    private Context context;
    private TextToSpeech textToSpeech;


    public WordsSpeech(Context context) {
        this.context = context;
        textToSpeech = new TextToSpeech(context, this);

    }

    @Override
    public void onInit(int status) {
       if (status == TextToSpeech.SUCCESS){
           textToSpeech.setLanguage(Locale.US);
           textToSpeech.speak("Apple", TextToSpeech.QUEUE_FLUSH, null);
       }
    }

    public void spechWord(String s){

    }
}
