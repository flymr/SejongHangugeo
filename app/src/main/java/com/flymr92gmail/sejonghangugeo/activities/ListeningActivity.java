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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ListeningAdapter adapter;
    private MediaPlayer mp;
    private MorphButton playButton;
    private SeekBar audioSeekBar;
    private Handler handlerAudio;
    private TextView tvAudioStart, tvAudioEnd;
    private Runnable audioRun;
    private ImageView nextTestIv, prevTestIv;
    private ArrayList<Integer> chooseAnswers;
    private ArrayList<Integer> userChooseAnswers;
    private Button acceptAnswerBrn;
    private EditText acceptEt;
    private int testType = 0;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp.isPlaying()) mp.stop();
    }

    private void setupRvTestStandart(){
        adapter = new ListeningAdapter(currentTest.getmFirstAnswer(), currentTestImages, testType);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupRvTestChoose(){
        adapter = new ListeningAdapter(currentTest.getmFirstAnswer(), currentTestImages, testType);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupRvTestSequence(){
        adapter = new ListeningAdapter(currentTest.getmFirstAnswer(), currentTestImages, testType);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupRvTestText(){
        adapter = new ListeningAdapter(currentTest.getmFirstAnswer(), currentTestImages, testType);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initObj(){
        Intent intent = getIntent();
        page = intent.getIntExtra("page", -1);
        dataBase = new AppDataBase(this);
        tests = dataBase.getAudioTest(page);
        currentTestImages = new ArrayList<>();
        mp = new MediaPlayer();
        handlerAudio = new Handler();
        chooseAnswers = new ArrayList<>();
        userChooseAnswers = new ArrayList<>();

    }

    private void initUI(){
        recyclerView = findViewById(R.id.rv_listening);
        playButton = findViewById(R.id.listening_play_audio);
        audioSeekBar = findViewById(R.id.listening_seek_bar);
        tvAudioStart = findViewById(R.id.tv_listening_audio_start);
        tvAudioEnd = findViewById(R.id.tv_listening_audio_end);
        nextTestIv = findViewById(R.id.iv_next_test);
        prevTestIv = findViewById(R.id.iv_prev_test);
        acceptAnswerBrn = findViewById(R.id.accept_answer_btn);
        acceptEt = findViewById(R.id.accept_answer_et);
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
        currentTest = tests.get(currentTestCount);
        testType = currentTest.getmTestType();
        updateUI(testType);
        currentTestImages.clear();
        for (int i = 0; i < currentTest.getmImageCount(); i++){
            currentTestImages.add(getImageFromAssets(i+1));
        }
        stopAudio();
        setupLogic(testType);
        playAudio();
        currentTestCount++;
    }


    private void prevTest(){
      currentTestCount = currentTestCount -2;
      nextTest();
    }

    private void updateUI(int type){
        if (currentTestCount == tests.size()-1) nextTestIv.setVisibility(View.GONE);
        else if (currentTestCount ==0) prevTestIv.setVisibility(View.GONE);
        else {
            nextTestIv.setVisibility(View.VISIBLE);
            prevTestIv.setVisibility(View.VISIBLE);
        }
        switch (type){
            case 0:
                acceptAnswerBrn.setVisibility(View.GONE);
                acceptEt.setVisibility(View.GONE);
                break;
            case 1:
                acceptAnswerBrn.setVisibility(View.VISIBLE);
                acceptEt.setVisibility(View.GONE);

                break;
            case 2:
                acceptAnswerBrn.setVisibility(View.VISIBLE);
                acceptEt.setVisibility(View.GONE);

                break;
            case 3:
                acceptAnswerBrn.setVisibility(View.GONE);
                acceptEt.setVisibility(View.VISIBLE);

                break;
        }
    }

    private void setupLogic(int type){
        switch (type){
            case 0:
                setupRvTestStandart();
                break;
            case 1:
                getChooseAnswers();
                setupRvTestChoose();
                setupAcceptBtnListener();
                break;
            case 2:
                getChooseAnswers();
                setupRvTestSequence();
                setupAcceptBtnListener();
                break;
            case 3:

                setupRvTestText();
                setupAcceptEtActionDone();
                break;
        }
    }

    private void getChooseAnswers(){
        if (currentTest.getmFirstAnswer2()!= 0){
            chooseAnswers.add(currentTest.getmFirstAnswer());
            chooseAnswers.add(currentTest.getmFirstAnswer2());
            if (currentTest.getmFirstAnswer3() != 0)
                chooseAnswers.add(currentTest.getmFirstAnswer3());
        }else chooseAnswers.add(currentTest.getmFirstAnswer());
    }

    private void setupAcceptBtnListener(){
        acceptAnswerBrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testType ==1){
                    getUserChooseAnswers();
                    if (checkAnswerTypeChoose()){
                        acceptAnswerBrn.setText("Верно");
                    }else {
                        acceptAnswerBrn.setText("Неверно");
                    }
                }else if (testType ==2){
                    getUserSequenceAnswer();
                    if (checkAnswerTypeSequence()){
                        acceptAnswerBrn.setText("Верно");
                    }else {
                        acceptAnswerBrn.setText("Неверно");
                    }
                }
            }
        });
    }

    private void setupAcceptEtActionDone(){
        acceptEt.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (checkAnswerTypeText()){
                        Toast.makeText(ListeningActivity.this, "Верно", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ListeningActivity.this, "Неверно", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getUserChooseAnswers(){
        userChooseAnswers.clear();
        boolean[] selects = adapter.getSelects();
        for (int i= 0; i < selects.length; i++){
            if (selects[i]) userChooseAnswers.add(i+1);
        }
    }

    private void getUserSequenceAnswer(){
        userChooseAnswers.clear();
        int[] sequence = adapter.getSequence();
        for (int i =0; i < sequence.length; i++){
            userChooseAnswers.add(sequence[i]);
        }
    }

    private boolean checkAnswerTypeChoose(){
        if (chooseAnswers.size() != userChooseAnswers.size()) return false;
        for (int i = 0; i < userChooseAnswers.size(); i++){
            if (!userChooseAnswers.contains(chooseAnswers.get(i))) return false;
        }
        return true;
    }

    private boolean checkAnswerTypeSequence(){
        if (chooseAnswers.size() != userChooseAnswers.size())
            Toast.makeText(this,"Последовательность неполная", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < userChooseAnswers.size(); i++){
            int userAnswer = userChooseAnswers.get(i);
            int correctAnswer = chooseAnswers.get(i);
            if (userAnswer == correctAnswer) return false;
        }
        return true;
    }

    private boolean checkAnswerTypeText(){
       String userAnswer = acceptEt.getText().toString();
       return userAnswer.equals(currentTest.getmTextAnswer());
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
