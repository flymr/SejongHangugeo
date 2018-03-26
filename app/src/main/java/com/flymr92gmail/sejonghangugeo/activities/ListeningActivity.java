package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.Adapters.ListeningAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.AudioTest;
import com.flymr92gmail.sejonghangugeo.POJO.Test;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.TouchImageView;
import com.wnafee.vector.MorphButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListeningActivity extends AppCompatActivity{
    private int page;
    private AppDataBase dataBase;
    private int currentTestCount = 0;
    private ArrayList<AudioTest> tests;
    private AudioTest currentTest;
    private ArrayList<Drawable> currentTestImages;
    private RecyclerView recyclerView;
    private MediaPlayer mp;
    private MorphButton playButton;
    private SeekBar audioSeekBar;
    private Handler handlerAudio;
    private TextView tvAudioStart, tvAudioEnd;
    private Runnable audioRun;
    private ImageView nextTestIv, prevTestIv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        initUI();
        initObj();
        setupAudioPlayer();
        nextTest();
        //setupRecyclerView();
        setupNextPrevBtn();
    }

    private void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListeningAdapter(currentTest.getmFirstAnswer(), currentTestImages));
        // setupAdapter();
    }


    private void initObj(){
        Intent intent = getIntent();
        page = intent.getIntExtra("page", -1);
        dataBase = new AppDataBase(this);
        tests = dataBase.getAudioTest(page);
        currentTestImages = new ArrayList<>();
        mp = new MediaPlayer();
        handlerAudio = new Handler();

    }

    private void initUI(){
        recyclerView = findViewById(R.id.rv_listening);
        playButton = findViewById(R.id.listening_play_audio);
        audioSeekBar = findViewById(R.id.listening_seek_bar);
        tvAudioStart = findViewById(R.id.tv_listening_audio_start);
        tvAudioEnd = findViewById(R.id.tv_listening_audio_end);
        nextTestIv = findViewById(R.id.iv_next_test);
        prevTestIv = findViewById(R.id.iv_prev_test);
    }

    private void setupNextPrevBtn(){
        nextTestIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextTest();
            }
        });
        prevTestIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevTest();
            }
        });
    }

    private Drawable getImageFromAssets(int imageId){
        Drawable d;
        try {
            InputStream ims = getAssets()
                    .open("images/listening/page"
                            +page+"_"+(currentTestCount+1)+"/"+imageId+".jpg");
            d = Drawable.createFromStream(ims, null);
        }
        catch(IOException ex) {
            return null;
        }
        return d;
    }

    private void nextTest(){
        if (currentTestCount == tests.size()-1) nextTestIv.setVisibility(View.GONE);
        else if (currentTestCount ==0) prevTestIv.setVisibility(View.GONE);
        else {
            nextTestIv.setVisibility(View.VISIBLE);
            prevTestIv.setVisibility(View.VISIBLE);
        }
        currentTest = tests.get(currentTestCount);
        currentTestImages.clear();
        for (int i = 0; i < currentTest.getmImageCount(); i++){
            currentTestImages.add(getImageFromAssets(i+1));
        }
        stopAudio();
        setupRecyclerView();
        playAudio();
        currentTestCount++;
    }


    private void prevTest(){
      currentTestCount = currentTestCount -2;
      nextTest();
    }

    private void setupAudioPlayer(){
        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mp!=null && b){
                    mp.seekTo(i*10);
                    //if (i == mp.getDuration()/10) stopAudio();

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//
            }
        });
        playButton.setOnStateChangedListener(new MorphButton.OnStateChangedListener() {
            @Override
            public void onStateChanged(MorphButton.MorphState changedTo, boolean isAnimating) {
                if (isAnimating) {
                    switch (changedTo) {
                        case START:
                            if (mp != null) mp.start();
                            break;
                        case END:
                            if (mp != null) mp.pause();
                            break;

                    }
                }
            }
        });

    }

    private void playAudio(){
        try {
            mp = new MediaPlayer();
            AssetFileDescriptor descriptor = getAssets().openFd("Audio/"+currentTest.getmTrackId()+".wma");
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playButton.setState(MorphButton.MorphState.END, true);
                    //audioSeekBar.setProgress(0);
                    mp.seekTo(0);
                }
            });
            descriptor.close();
            mp.prepare();
            mp.setVolume(1f, 1f);
            mp.setLooping(false);
            mp.start();




            getAudioStats();
            initializeSeekBar();
        } catch (Exception e) {

        }
    }

    protected void stopAudio(){
            if(mp!=null){
                mp.stop();
                mp.release();
                mp = null;
                if(handlerAudio!=null){
                    handlerAudio.removeCallbacks(audioRun);
                }
            }
            audioSeekBar.setProgress(0);
            if (playButton.getState() == MorphButton.MorphState.END) playButton.setState(MorphButton.MorphState.START);

    }



    private void getAudioStats(){
        int duration  = mp.getDuration()/1000; // In milliseconds
        int due = (mp.getDuration() - mp.getCurrentPosition())/1000;
        int pass = duration - due;

        tvAudioStart.setText(getDuration(pass));
        tvAudioEnd.setText(getDuration(due));
    }


    private String getDuration(int duration){
        int seconds;
        int minutes = 0;
        if (60 < duration){
            for (int i =60; i < duration; i = i +60){
                minutes++;
            }
            seconds = duration - (minutes*60);
        }else {
            seconds = duration;
        }
        if (seconds < 10) return minutes + ":0" + seconds;
        return minutes + ":" + seconds;
    }



    private void initializeSeekBar(){
        audioSeekBar.setMax(mp.getDuration()/10);
        audioRun = new Runnable() {
            @Override
            public void run() {
                if(mp!=null){
                    int mCurrentPosition = mp.getCurrentPosition()/10; // In milliseconds
                    audioSeekBar.setProgress(mCurrentPosition);
                    getAudioStats();

                }
                handlerAudio.postDelayed(audioRun,10);
            }
        };
        handlerAudio.postDelayed(audioRun,10);
    }

}
