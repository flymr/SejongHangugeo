package com.flymr92gmail.sejonghangugeo.Utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.flymr92gmail.sejonghangugeo.R;

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
            int result = textToSpeech.setLanguage(Locale.KOREAN);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");

            }
            Log.e("TTS", "All right");


        }
    }

    public void speechWord(String s){
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);

    }

    public boolean wordIsSpeech(){
       return textToSpeech.isSpeaking();
    }
}
