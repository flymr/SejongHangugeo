package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.flymr92gmail.sejonghangugeo.Adapters.ListeningAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.AudioTest;
import com.flymr92gmail.sejonghangugeo.POJO.Test;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.TouchImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        initObj();
        nextTest();
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        recyclerView = findViewById(R.id.rv_listening);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
    }

    private void setupAdapter(){
        recyclerView.setAdapter(new ListeningAdapter(currentTest.getmFirstAnswer(), currentTestImages));
    }

    private void initObj(){
        Intent intent = getIntent();
        page = intent.getIntExtra("page", -1);
        dataBase = new AppDataBase(this);
        tests = dataBase.getAudioTest(page);
        currentTestImages = new ArrayList<>();

    }

    private Drawable getImageFromAssets(int imageId){
        Drawable d;
        try {
            InputStream ims = getAssets()
                    .open("images/listening/page"
                            +page+"_"+(currentTestCount+1)+"/"+imageId+".jpg");
            d = Drawable.createFromStream(ims, null);
            Log.d("complete drawable", "ok");
        }
        catch(IOException ex) {
            Log.d("complete drawable", "ne ok");
            return null;
        }
        return d;
    }

    private void nextTest(){
        if (currentTestCount == tests.size()) finish();
        else {
            currentTest = tests.get(currentTestCount);
            for (int i = 0; i < currentTest.getmImageCount(); i++){
                currentTestImages.add(getImageFromAssets(i+1));
            }
            Log.d("count of image:     ", "" + currentTest.getmImageCount());
            currentTestCount++;
        }
    }



    private boolean checkAnswer(AudioTest test, int variant){
        if (test.getmFirstAnswer() == variant)return true;
        else return false;
    }

    private void startAnim(boolean answerIsCorrect){

    }
}
