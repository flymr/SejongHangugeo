package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.Adapters.LearnResultWordsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;

import java.util.ArrayList;

public class LearnResult extends AppCompatActivity {
    private ArrayList<Word> learningWords;
    private UserDataBase dataBase;
    private RecyclerView recyclerView;
    private TextView tvUnlearned, tvLearning, tvLearned;
    private Lesson lesson;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_result);
        initialization();
        setLearningStatus();
        setupToolbar();
    }

    private void initialization(){
        Intent intent = getIntent();
        learningWords = (ArrayList<Word>)intent.getSerializableExtra("learningWords");
        lesson = (Lesson)intent.getSerializableExtra("lesson");
        dataBase = new UserDataBase(this);
        toolbar = findViewById(R.id.toolbar_learn_result);
        recyclerView = findViewById(R.id.rv_learn_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LearnResultWordsAdapter(learningWords, this));
        tvUnlearned = findViewById(R.id.tv_unlearned);
        tvLearning = findViewById(R.id.tv_learning);
        tvLearned = findViewById(R.id.tv_learned);
        findViewById(R.id.learn_next_round).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextRoundAction();

            }
        });
    }

    private void nextRoundAction(){
        Intent intent = new Intent(this, LearnActivity.class);
        intent.putExtra("lesson", lesson);
        startActivity(intent);
        finish();
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle(lesson.getLessonName());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    onBackPressed();
                }
            });
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    private void setLearningStatus(){
        int unlearnCount = 0;
        int learningCount = 0;
        int learnedCount = 0;
        for (Word word : dataBase.getLearningWord(lesson)){
            if (word.getCorrectCount()==0) unlearnCount++;
            else if (word.getCorrectCount()==1) learningCount++;
            else learnedCount++;
        }
        String s = unlearnCount + "";
        tvUnlearned.setText(s);
        s = learningCount + "";
        tvLearning.setText(s);
        s = learnedCount + "";
        tvLearned.setText(s);
    }

}
