package com.flymr92gmail.sejonghangugeo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.SpeechActionListener;
import com.flymr92gmail.sejonghangugeo.activities.CardActivity;
import com.flymr92gmail.sejonghangugeo.activities.LearnActivity;
import com.flymr92gmail.sejonghangugeo.Adapters.LessonWordsSelectableRecyclerAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;


import java.util.ArrayList;
import java.util.Locale;


public class LessonActivity extends AppCompatActivity implements SpeechActionListener, TextToSpeech.OnInitListener{

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
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        mIntent = getIntent();
        initialization();
        lesson=getLesson(mIntent);
        PrefManager prefManager = new PrefManager(this);
        prefManager.saveLastLessonID(lesson.getLessonId());
        setupAdapter(lesson);
        setupToolbar();
        setupFloatingToolbarListener();
        setupTabStrip();
        setupRecyclerViewListener();
        setupGesture();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
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

        dataBase = new UserDataBase(this);
        recyclerView = findViewById(R.id.lesson_rv);
        recyclerView.setLayoutManager(linearLayoutManager);
        floatingToolbar.attachRecyclerView(recyclerView);
        lastFirstVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        plusToCross = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plus_to_cross);
        croosToPlus = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cross_to_plus);

        textToSpeech = new TextToSpeech(this, this);
    }

    private void setupToolbar(){
        toolbar = findViewById(R.id.toolbar_lesson);
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
        return dataBase.getLessonByPrimaryId(intent.getIntExtra("lessonId",-1));
    }

    private void setupAdapter(Lesson lesson){
        if (lesson.getLessonTabIndex() == 1) words = dataBase.getSelectedWordsInLesson(lesson);
        else words = dataBase.getWordsInLesson(lesson.getLessonTable());
        adapter = new LessonWordsSelectableRecyclerAdapter(words,this,lesson, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void editLessonAction(){
        Intent intent = new Intent(this, EditLessonActivity.class);
        intent.putExtra("lessonId", lesson.getLessonId());
        startActivity(intent);
    }

    private void learAction(){
        if (words.size()!=0){
            for (Word word : dataBase.getWordsInLesson(lesson.getLessonTable())){
                word.setmIsLearning(0);
                dataBase.editWordLearning(lesson, word);
            }
            for (Word word : words){
                word.setmIsLearning(1);
                dataBase.editWordLearning(lesson, word);
            }
            Intent intent = new Intent(this, LearnActivity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);
        }else {
            Toast.makeText(this, getText(R.string.firstly_add_words), Toast.LENGTH_SHORT).show();
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

            }
        }, 100);
        floatingToolbar.hide();
    }

    @Override
    public void onBackPressed() {
        if (deleteMode){
            setDeleteMode(false);
            fab.startAnimation(croosToPlus);
            floatingToolbar.attachFab(fab);
        }else super.onBackPressed();
    }


    private void wordsArrayChanger(int tabStripIndex){

        lesson.setLessonTabIndex(tabStripIndex);
        dataBase.editLessonTabIndex(lesson);
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

    private void setupRecyclerViewListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
              if (!deleteMode &&!recyclerView.canScrollVertically(1)
                      && allItemNotVisible()){
                  floatingToolbar.show();

              }
            }

        });

    }

    private boolean allItemNotVisible(){
        return linearLayoutManager.findLastCompletelyVisibleItemPosition() + linearLayoutManager.findFirstCompletelyVisibleItemPosition() > adapter.getItemCount()-1;
    }

    private void setupGesture(){
        final GestureDetector gdt = new GestureDetector(this, new GestureListener());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gdt.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    @Override
    public void onSpeechClick(int position, View view) {
        String kor = words.get(position).getKoreanWord();
      // View view = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        final ImageView imageView = view.findViewById(R.id.speech_iv);
        ObjectAnimator.ofObject(imageView, "colorFilter", new ArgbEvaluator(), getResources().getColor(R.color.grayM),
                getResources().getColor(R.color.yellow)).setDuration(100).start();
        speechWord(kor);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        while (words.size() != 0){
                            if (!wordIsSpeech()){
                                ObjectAnimator.ofObject(imageView, "colorFilter", new ArgbEvaluator(), getResources().getColor(R.color.yellow),
                                        getResources().getColor(R.color.grayM)).setDuration(300).start();
                                return;
                            }
                        }
                    }
                });
                // mBottomNavigationTabStrip.setTabIndex(tabIndex);

            }
        }, 100);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            int result = textToSpeech.setLanguage(Locale.KOREAN);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
              /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.havent_tts)
                        .setMessage(R.string.go_to_tts_setting);
                builder.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Intent intent = new Intent();
                        intent.setAction("com.android.settings.TTS_SETTINGS");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                AlertDialog alert = builder.create();
                alert.show();*/
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
