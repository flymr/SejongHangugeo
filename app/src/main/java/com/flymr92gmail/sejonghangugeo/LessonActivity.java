package com.flymr92gmail.sejonghangugeo;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.flymr92gmail.sejonghangugeo.activities.CardActivity;
import com.flymr92gmail.sejonghangugeo.activities.LearnActivity;
import com.flymr92gmail.sejonghangugeo.Adapters.LessonWordsSelectableRecyclerAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;


import java.util.ArrayList;


public class LessonActivity extends AppCompatActivity {

    private UserDataBase dataBase;
    private RecyclerView recyclerView;
    private ArrayList<Word> words;
    private LessonWordsSelectableRecyclerAdapter adapter;
    private Intent mIntent;
    private Toolbar toolbar;
    private Lesson lesson;
    private boolean deleteMode=false;
    private FloatingToolbar floatingToolbar;
    private NavigationTabStrip mBottomNavigationTabStrip;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton fab;
    private static final int SWIPE_MIN_DISTANCE = 200;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private int lastFirstVisiblePosition;
    private Animation plusToCross, croosToPlus;
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        mIntent = getIntent();
        initialization();
        lesson=getLesson(mIntent);
        setupAdapter(lesson);
        setupToolbar();
        setupFloatingToolbarListener();
        setupTabStrip();
        setupRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAdapter(lesson);
        linearLayoutManager.scrollToPositionWithOffset(lastFirstVisiblePosition,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lastFirstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
    }

    private void initialization(){
        linearLayoutManager = new LinearLayoutManager(this);
        mBottomNavigationTabStrip = findViewById(R.id.nts_top);
        floatingToolbar = findViewById(R.id.floatingToolbar);
        fab = findViewById(R.id.fabLesson);
        floatingToolbar.attachFab(fab);
        toolbar = findViewById(R.id.toolbar_lesson);
        dataBase = new UserDataBase(this);
        recyclerView = findViewById(R.id.lesson_rv);
        recyclerView.setLayoutManager(linearLayoutManager);
        floatingToolbar.attachRecyclerView(recyclerView);
        lastFirstVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        plusToCross = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plus_to_cross);
        croosToPlus = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cross_to_plus);
    }

    private void setupToolbar(){
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

    private Lesson getLesson(Intent intent){
        intent.getStringExtra("lessonId");
        Lesson lesson = dataBase.getLessonByPrimaryId(intent.getIntExtra("lessonId",-1));
        return lesson;
    }

    private void setupAdapter(Lesson lesson){
        if (lesson.getLessonTabIndex() == 1) words = dataBase.getSelectedWordsInLesson(lesson);
        else words = dataBase.getWordsInLesson(lesson.getLessonTable());
        adapter = new LessonWordsSelectableRecyclerAdapter(words,this,lesson);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setupAdapter(lesson);
        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(lastFirstVisiblePosition,0);
    }

    private void editLessonAction(){
        Intent intent = new Intent(this, EditLessonActivity.class);
        intent.putExtra("lessonId", lesson.getLessonId());
        startActivity(intent);
    }

    private void learAction(){
        if (words.size()!=0){
            for (Word word : words){
                word.setmIsLearning(1);
                dataBase.editWordLearning(lesson, word);
            }
            Intent intent = new Intent(this, LearnActivity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);
        }

    }

    private void cardAction(){
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("words", words);
        intent.putExtra("lesson", lesson);
        startActivity(intent);
    }
    private void setupFloatingToolbarListener(){

        floatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
               switch (item.getItemId()){
                   case R.id.actionAddNewWord:{
                       editLessonAction();
                       break;
                   }case R.id.actionWrite:{
                       learAction();
                       break;
                   }case R.id.actionDelete:{
                       setDeleteMode(true);
                       fab.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               if (deleteMode) {
                                   setDeleteMode(false);
                                   fab.startAnimation(croosToPlus);
                                   floatingToolbar.attachFab(fab);
                               }
                           }
                       });
                       break;
                   }case R.id.actionCard:{
                       cardAction();
                       break;
                   }
                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });
        floatingToolbar.addMorphListener(new FloatingToolbar.MorphListener() {
            @Override
            public void onMorphEnd() {

            }

            @Override
            public void onMorphStart() {

            }

            @Override
            public void onUnmorphStart() {

            }

            @Override
            public void onUnmorphEnd() {
//                floatingToolbar.detachFab();
            if (deleteMode)fab.startAnimation(plusToCross);
            }
        });
        croosToPlus.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    private void setupTabStrip() {

        mBottomNavigationTabStrip.setTabIndex(lesson.getLessonTabIndex(), true);
        mBottomNavigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final String title, final int index) {
                linearLayoutManager.scrollToPositionWithOffset(lastFirstVisiblePosition,0);
                runTabChange(index);

            }
            @Override
            public void onEndTabSelected(final String title, final int index) {
                //setDeleteMode(false);

            }
        });
    }

    private void runTabChange(final int tabIndex){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wordsArrayChanger(tabIndex);
                    }
                });
               // mBottomNavigationTabStrip.setTabIndex(tabIndex);

            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        if (deleteMode){
            setDeleteMode(false);
            fab.startAnimation(croosToPlus);
            floatingToolbar.attachFab(fab);
        }else super.onBackPressed();
    }

    private void doShowSelected(int tabIndex){
        lesson.setLessonTabIndex(tabIndex);
        dataBase.editLessonTabIndex(lesson);
        floatingToolbar.hide();
        if (tabIndex==1){
            for (int i = words.size()-1; 0<=i; i--) {
                Word word = words.get(i);
                if (word.isSelected() == 0) {
                    words.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        }else {
            ArrayList<Word> allWords = dataBase.getWordsInLesson(lesson.getLessonTable());
            for (int i = 0; i < allWords.size(); i++){
                Word word = allWords.get(i);
                if (word.isSelected()==0) {
                    words.add(i, word);
                    adapter.notifyItemInserted(i);
                }
            }
        }
    }

    private void wordsArrayChanger(int tabStripIndex){

        lesson.setLessonTabIndex(tabStripIndex);
        dataBase.editLessonTabIndex(lesson);
        floatingToolbar.hide();
        if (tabStripIndex==1){
            for (int i = words.size()-1; 0<=i; i--) {
                Word word = words.get(i);
                if (word.isSelected() == 0) {
                    words.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        }else {
            ArrayList<Word> allWords = dataBase.getWordsInLesson(lesson.getLessonTable());
            for (int i = 0; i < allWords.size(); i++){
                Word word = allWords.get(i);
                if (word.isSelected()==0) {
                    words.add(i, word);
                    adapter.notifyItemInserted(i);
                }
            }
            linearLayoutManager.scrollToPositionWithOffset(lastFirstVisiblePosition,0);
        }

    }

    private void setDeleteMode(boolean deleteMode){
        this.deleteMode = deleteMode;
        adapter.deleteMod(deleteMode);
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
              if (linearLayoutManager.getChildCount() != linearLayoutManager.getItemCount()&&!deleteMode) {
                  if (!recyclerView.canScrollVertically(1)) {
                        floatingToolbar.show();
                        //floatingToolbar.detachRecyclerView();
                  }
              }

            }

        });
        final GestureDetector gdt = new GestureDetector(this, new GestureListener());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gdt.onTouchEvent(motionEvent);
               return false;
            }
        });

    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                mBottomNavigationTabStrip.setTabIndex(1);
                return false; // справа налево
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                mBottomNavigationTabStrip.setTabIndex(0);
                return false; // слева направо
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // снизу вверх
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // сверху вниз
            }
            return false;
        }
    }
}
