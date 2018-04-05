package com.flymr92gmail.sejonghangugeo.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.Helper;
import com.flymr92gmail.sejonghangugeo.Utils.WordsHelper;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LearnFinalResult extends AppCompatActivity {
    private UserDataBase dataBase;
    private ArrayList<Word> words;
    private Lesson lesson;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private float progress;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_final_result);
        initialization();
        sortWords();
        setupRecycler();
        setupToolbar();


    }


    private void initialization(){
        Intent intent = getIntent();
        lesson = (Lesson)intent.getSerializableExtra("lesson");
        dataBase = new UserDataBase(this);
        words = dataBase.getLearningWord(lesson);
        recyclerView = findViewById(R.id.rv_learn_final);
        toolbar = findViewById(R.id.toolbar_learn_final);
        layoutManager = new LinearLayoutManager(this);
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
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }
    private void setupRecycler(){
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new LearnFinalResultAdapter());
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void sortWords(){
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getMissCount()-o2.getMissCount();
            }
        });
        Collections.reverse(words);
        float i = 0.0f;
        for (Word word : words){
            i = i + (2.0f/(word.getMissCount()+2.0f));
        }
        progress = i/words.size()*100;
        lesson.setLessonProgress((int)progress);
        dataBase.editLessonProgress(lesson);
    }



    private boolean checkErrorWords(){
        boolean error = false;
        for (Word word : words){
            if (0 < word.getMissCount()){
                error = true;
                break;
            }
        }
        return error;
    }

    private boolean checkSelectedWords(){
        boolean selected = false;
        for (Word word : words){
            if (word.isSelected() == 1){
                selected = true;
                break;
            }
        }
        return selected;
    }

    private void learnAction(){
        Intent intent = new Intent(this, LearnActivity.class);
        intent.putExtra("lesson", lesson);
        startActivity(intent);
        finish();
    }

    private void clearWords(){
        for (Word word : words){
            word.setMissCount(0);
            word.setCorrectCount(0);
            dataBase.editWordMissCount(lesson, word);
            dataBase.editWordCorrectCount(lesson, word);
        }
    }

    private void clearLearningIndex(){
        for (Word word : words){
            word.setmIsLearning(0);
            dataBase.editWordLearning(lesson, word);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearWords();
    }

    private boolean checkErrorWord(){
        boolean error = false;
        for (Word word : words){
            if (0 < word.getMissCount()){
                error = true;
                break;
            }
        }
        return error;
    }

    private boolean checkSelectedWord(){
        boolean selected = false;
        for (Word word : words){
            if (word.isSelected() == 1){
                selected = true;
                break;
            }
        }
        return selected;
    }

    private class LearnFinalResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        static final int HEADER = 0;
        static final int NORMAL_ITEM = 1;
        static final int CONTENT_VH = 2;

        public LearnFinalResultAdapter(){

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == CONTENT_VH){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learn_final_content_item, parent, false);
                ContentViewHolder vh = new ContentViewHolder(view);
                return vh;
            }
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_item, parent, false);
            WordViewHolderFinal vh = new WordViewHolderFinal(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position)!=CONTENT_VH){
                Word word = words.get(position-1);
                WordViewHolderFinal viewHolder =(WordViewHolderFinal)holder;
                switch (getItemViewType(position)){
                    case HEADER:
                        String s;
                        if (word.getMissCount()!=0){
                            s = "Допущено ошибок: "+word.getMissCount();
                        }else {
                            s = "Без ошибок";
                        }
                        viewHolder.tvHeader.setVisibility(View.VISIBLE);
                        viewHolder.tvHeader.setText(s);
                        break;
                    case  NORMAL_ITEM:
                        viewHolder.tvHeader.setVisibility(View.GONE);
                        break;
                }
                viewHolder.tvKorean.setText(word.getKoreanWord());
                viewHolder.tvRuss.setText(word.getRussianWord());
                viewHolder.ivStar.setVisibility(View.VISIBLE);
                if (word.isSelected()==0){
                    viewHolder.ivStar.setColorFilter(getResources().getColor(R.color.grayM));
                } else {
                    viewHolder.ivStar.setColorFilter(getResources().getColor(R.color.yellow));
                }
            }else {
                ContentViewHolder contentHolder = (ContentViewHolder)holder;

            }

        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return CONTENT_VH;
            else if (position == 1) return HEADER;
            else if (words.get(position-1).getMissCount()!=words.get(position - 2).getMissCount())return HEADER;
            else return NORMAL_ITEM;
        }

        @Override
        public int getItemCount() {
            return words.size()+1;
        }

        public class WordViewHolderFinal extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView ivStar;
            TextView tvHeader, tvKorean, tvRuss;
            public WordViewHolderFinal(View itemView) {
                super(itemView);
                tvKorean = itemView.findViewById(R.id.korean_word_tv);
                tvRuss = itemView.findViewById(R.id.russian_word_tv);
                tvHeader = itemView.findViewById(R.id.learn_final_tv);
                ivStar = itemView.findViewById(R.id.star_iv);
                ivStar.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Word word = words.get(getAdapterPosition()-1);
                if (word.isSelected() == 0) {
                    ivStar.setColorFilter(getResources().getColor(R.color.yellow));
                    word.setSelected(1);
                    dataBase.editWordSelect(lesson, word);
                    if (checkSelectedWord()&&1==dataBase.getCountOfSelectedWords(lesson)){
                        View viewCard = recyclerView.findViewHolderForAdapterPosition(0).itemView;
                        CardView selectedCV = viewCard.findViewById(R.id.repeat_selected_words);
                        buttonAnim(selectedCV, 0.5f, 1f);

                    }
                } else {
                    ivStar.setColorFilter(getResources().getColor(R.color.grayM));
                    word.setSelected(0);
                    dataBase.editWordSelect(lesson, word);
                    if (!checkSelectedWord()){
                        View viewCard = recyclerView.findViewHolderForAdapterPosition(0).itemView;
                        CardView selectedCV = viewCard.findViewById(R.id.repeat_selected_words);
                        buttonAnim(selectedCV, 1f, 0.5f);
                    }
                }

            }
        }

        private void buttonAnim(View view,float start, float end){
            ObjectAnimator.ofFloat(view, View.ALPHA, start, end).setDuration(700).start();
        }
        public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ArcProgress circleProgressView;
            CardView allRepeatCv;
            CardView errorRepeatCv;
            CardView selectedRepeatCv;
            TextView errorTv, selectedTv;
            public ContentViewHolder(View itemView) {
                super(itemView);
                circleProgressView = itemView.findViewById(R.id.arc_progress);
                allRepeatCv = itemView.findViewById(R.id.repeat_all_words);
                errorRepeatCv = itemView.findViewById(R.id.repeat_error_words);
                selectedRepeatCv = itemView.findViewById(R.id.repeat_selected_words);
                errorTv = itemView.findViewById(R.id.error_rep_tv);
                selectedTv = itemView.findViewById(R.id.selected_rep_tv);

                if (!checkErrorWord())errorRepeatCv.setAlpha(0.5f);
                if (!checkSelectedWord())selectedRepeatCv.setAlpha(0.5f);
                setProgress();
                clickableButton();
                onClickButton();

            }

            @Override
            public void onClick(View view) {
               switch (view.getId()){
                   case R.id.repeat_all_words:
                       clearLearningIndex();
                       for (Word word : words){
                           word.setmIsLearning(1);
                           dataBase.editWordLearning(lesson, word);
                       }
                       clearWords();
                       learnAction();
                       break;
                   case R.id.repeat_error_words:
                       if (checkErrorWords()) {
                           clearLearningIndex();
                           for (Word word : words) {
                               if (0 < word.getMissCount()) {
                                   word.setmIsLearning(1);
                                   dataBase.editWordLearning(lesson, word);
                               }
                           }
                           clearWords();
                           learnAction();
                       }else {
                           Snackbar mSnackbar = Snackbar.make(view, "Вы не допустили ни одной ошибки", Snackbar.LENGTH_SHORT)
                                   .setAction("Action", null);
                           mSnackbar.show();
                       }
                       break;
                   case R.id.repeat_selected_words:
                       if (checkSelectedWords()) {
                           clearLearningIndex();

                           for (Word word : words) {
                               if (word.isSelected() == 1) {
                                   word.setmIsLearning(1);
                                   dataBase.editWordLearning(lesson, word);
                               }
                           }
                           clearWords();
                           learnAction();
                       } else {
                           Snackbar mSnackbar = Snackbar.make(view, "Нет избранных слов", Snackbar.LENGTH_SHORT)
                                   .setAction("Action", null);
                           mSnackbar.show();
                       }
                       break;
               }
            }

            private void setProgress(){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ObjectAnimator.ofInt(circleProgressView,"progress", 0, (int)progress)
                                        .setDuration(1000).start();
                            }
                        });

                    }
                }, 300);
                //circleProgressView.setProgress((int)progress);

            }


            private void onClickButton(){

                allRepeatCv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearLearningIndex();
                        for (Word word : words){
                            word.setmIsLearning(1);
                            dataBase.editWordLearning(lesson, word);
                        }
                        clearWords();
                        learnAction();
                    }
                });
                errorRepeatCv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkErrorWords()) {
                            clearLearningIndex();
                            for (Word word : words) {
                                if (0 < word.getMissCount()) {
                                    word.setmIsLearning(1);
                                    dataBase.editWordLearning(lesson, word);
                                }
                            }
                            clearWords();
                            learnAction();
                        }else {

                            Toast.makeText(LearnFinalResult.this, "Вы не допустили ни одной ошибки", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                selectedRepeatCv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkSelectedWords()) {
                            clearLearningIndex();

                            for (Word word : words) {
                                if (word.isSelected() == 1) {
                                    word.setmIsLearning(1);
                                    dataBase.editWordLearning(lesson, word);
                                }
                            }
                            clearWords();
                            learnAction();
                        } else {

                            Toast.makeText(LearnFinalResult.this, "Нет избранных слов", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private void clickableButton(){
                for (Word word : words){
                    if (0 < word.getMissCount()){
                        errorRepeatCv.setClickable(true);
                        errorTv.setTextColor(getResources().getColor(R.color.white));
                    }
                }
                for (Word word : words){
                    if (word.isSelected() == 1){
                        selectedRepeatCv.setClickable(true);
                        selectedTv.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            }


        }
    }

}
