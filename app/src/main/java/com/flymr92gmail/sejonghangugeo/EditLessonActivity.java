package com.flymr92gmail.sejonghangugeo;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.flymr92gmail.sejonghangugeo.Adapters.EditLessonAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;

public class EditLessonActivity extends AppCompatActivity {
       private Intent mIntent;
       private Lesson mLesson;
       private RecyclerView recyclerView;
       private UserDataBase dataBase;
       private EditLessonAdapter adapter;
       private LinearLayoutManager linearLayoutManager;
       private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lesson);
        initialization();
        setupToolbar();
        setupAdapter();


    }

   private void setupToolbar(){
       if (toolbar != null) {
           setSupportActionBar(toolbar);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setDisplayShowHomeEnabled(true);
           getSupportActionBar().setHomeButtonEnabled(true);
           getSupportActionBar().setDisplayShowTitleEnabled(false);
           toolbar.setTitle(mLesson.getLessonName());
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

    private void initialization(){
        toolbar = findViewById(R.id.toolbar_edit_lesson);
        mIntent = getIntent();
        recyclerView = findViewById(R.id.edit_lesson_rv);
        dataBase = new UserDataBase(this);
        mIntent.getStringExtra("lessonId");
        mLesson = dataBase.getLessonByPrimaryId(mIntent.getIntExtra("lessonId",-1));
    }


    private void setupAdapter(){
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new EditLessonAdapter(this, mLesson);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
       // recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
        linearLayoutManager.scrollToPositionWithOffset(adapter.getItemCount()-1,0);

    }



}
