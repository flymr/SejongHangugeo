package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.AudioTest;
import com.flymr92gmail.sejonghangugeo.POJO.Test;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.TouchImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListeningActivity extends AppCompatActivity implements View.OnClickListener{
    private int page;
    private AppDataBase dataBase;
    private int currentTestCount = 0;
    private ArrayList<AudioTest> tests;
    private TouchImageView image1, image2, image3;
    private AudioTest currentTest;
    private ArrayList<TouchImageView> currentTestImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        initUI();
        initObj();
    }

    private void initUI(){
        image1 = findViewById(R.id.iv_1);
        image2 = findViewById(R.id.iv_2);
        image3 = findViewById(R.id.iv_3);
    }

    private void initObj(){
        Intent intent = getIntent();
        page = intent.getIntExtra("page", -1);
        dataBase = new AppDataBase(this);
        tests = dataBase.getAudioTest(page);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        currentTestImages = new ArrayList<>();
        currentTestImages.add(image1);
        currentTestImages.add(image2);
        currentTestImages.add(image3);
        nextTest();
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
        if (currentTestCount == tests.size()) finish();
        else {
            currentTest = tests.get(currentTestCount);
            for (int i = 0; i < currentTest.getmImageCount(); i++){
                TouchImageView image = currentTestImages.get(i);
                image.setVisibility(View.VISIBLE);
                image.setImageDrawable(getImageFromAssets(i));
            }

            currentTestCount++;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_1:
                checkAnswer(currentTest, 1);

                break;
            case R.id.iv_2:
                checkAnswer(currentTest, 2);

                break;
            case R.id.iv_3:
                checkAnswer(currentTest, 3);

                break;
        }
    }

    private void checkAnswer(AudioTest test, int variant){
        if (test.getmFirstAnswer() == variant){

        }else {

        }
    }


}
