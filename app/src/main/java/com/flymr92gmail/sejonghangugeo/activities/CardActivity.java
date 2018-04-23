package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.wnafee.vector.MorphButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import eu.davidea.flipview.FlipView;

public class CardActivity extends AppCompatActivity {
    private ArrayList<Word> words;
    private Lesson lesson;
    private Context mContext;
    private UserDataBase dataBase;
    private  RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SnapHelper helper;
    private Toolbar toolbar;
    private Menu menu;
    private CardRecyclerAdapter adapter;
    private Handler flipNextHandler;
    private Handler recyclerNextHandler;
    private MorphButton playButton, playButtonHoriz;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initialization();
        recyclerListener();
        playMode();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playButton.setState(MorphButton.MorphState.START);
                        playButtonHoriz.setState(MorphButton.MorphState.START);

                    }
                });
            }
        }, 500);
    }

    private void initialization(){
        Intent intent = getIntent();
       // lesson = (Lesson)intent.getSerializableExtra("lesson");
        dataBase = new UserDataBase(this);
        lesson = getLesson(intent);
        words = (ArrayList<Word>)intent.getSerializableExtra("words");
        playButton = findViewById(R.id.play_card_vertical);
        playButtonHoriz = findViewById(R.id.play_card_horizontal);
        recyclerView = findViewById(R.id.rv_lesson_card);
        sortWords();
        adapter = new CardRecyclerAdapter(words, this);
        recyclerView.setAdapter(adapter);
        toolbar = findViewById(R.id.toolbar_card);
        flipNextHandler = new Handler();
        recyclerNextHandler = new Handler();
        setSnapHelperMode();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    private void sortWords(){
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getPositionInCardAction()-o2.getPositionInCardAction();
            }
        });
    }

    private Lesson getLesson(Intent intent){
        return dataBase.getLessonByPrimaryId(intent.getIntExtra("lessonId",-1));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAnimItem();
    }

    private void setupToolbar(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            try {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }catch (NullPointerException n){
               n.printStackTrace();
            }
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

    private void setSnapHelperMode(){
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            linearLayoutManager = new LinearLayoutManager(this);
            helper = new LinearSnapHelper();
            helper.attachToRecyclerView(recyclerView);
            recyclerView.addItemDecoration(new SpaceItemDecoration());
            setupToolbar();
            playButtonHoriz.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            playButton.setState(MorphButton.MorphState.START);


        }else {
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            helper = new PagerSnapHelper();
            helper.attachToRecyclerView(recyclerView);
            setupToolbar();
            playButtonHoriz.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            playButtonHoriz.setState(MorphButton.MorphState.START);

        }
    }


    private void playMode(){
        playButton.setOnStateChangedListener(new MorphButton.OnStateChangedListener() {
            @Override
            public void onStateChanged(MorphButton.MorphState changedTo, boolean isAnimating) {
                if (isAnimating) {
                    switch (changedTo) {
                        case END:
                            if (words.size()-1 == linearLayoutManager.findFirstCompletelyVisibleItemPosition()){
                                recyclerView.smoothScrollToPosition(0);
                                adapter.notifyDataSetChanged();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                                startAnimItems();
                                            }
                                        });
                                    }
                                }, 1000);
                            }else {
                                adapter.notifyDataSetChanged();
                                startAnimItems();

                            }
                            break;
                        case START:
                            stopAnimItem();
                            break;
                    }
                }
            }
        });
        playButtonHoriz.setOnStateChangedListener(new MorphButton.OnStateChangedListener() {
            @Override
            public void onStateChanged(MorphButton.MorphState changedTo, boolean isAnimating) {
                if (isAnimating) {
                    switch (changedTo) {
                        case END:
                            if (words.size()-1 == linearLayoutManager.findFirstCompletelyVisibleItemPosition()){
                                recyclerView.smoothScrollToPosition(0);
                                adapter.notifyDataSetChanged();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                                startAnimItems();
                                            }
                                        });
                                    }
                                }, 1000);
                            }else {
                                adapter.notifyDataSetChanged();
                                startAnimItems();

                            }
                            break;
                        case START:
                            stopAnimItem();
                            break;
                    }
                }
            }
        });
    }

    private void startAnimItems(){
        flipNextHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            View view;
                            FlipView flipView;
                            view = recyclerView.findViewHolderForAdapterPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition()).itemView;
                            flipView = view.findViewById(R.id.mFlipView);
                            flipView.showNext(0);
                        }catch (NullPointerException n){
                           n.printStackTrace();
                        }


                    }
                });
            }
        }, 2000);
        if (words.size()-1 <= linearLayoutManager.findFirstCompletelyVisibleItemPosition()) return;
        recyclerNextHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (words.size()-1 != linearLayoutManager.findFirstCompletelyVisibleItemPosition())
                            recyclerView.smoothScrollToPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition()+1);
                        else playButton.setState(MorphButton.MorphState.START, true);
                    }
                });
                if (words.size()-1 != linearLayoutManager.findFirstCompletelyVisibleItemPosition()) startAnimItems();
            }
        }, 4000);
    }

    private void stopAnimItem(){
        flipNextHandler.removeCallbacksAndMessages(null);
        recyclerNextHandler.removeCallbacksAndMessages(null);
        if (playButton.getState() == MorphButton.MorphState.END)playButton.setState(MorphButton.MorphState.START, true);
        if (playButtonHoriz.getState() == MorphButton.MorphState.END)playButtonHoriz.setState(MorphButton.MorphState.START, true);

    }

    private void recyclerListener(){
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                stopAnimItem();
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            stopAnimItem();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
              stopAnimItem();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cards,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cardChangeLanguage:
                stopAnimItem();
                if (lesson.getCurrentLanguageCards()==0){
                    lesson.setCurrentLanguageCards(1);
                }else  {
                    lesson.setCurrentLanguageCards(0);
                }
                dataBase.editCurrentLanguagCards(lesson);
                int lastPos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                recyclerView.setAdapter(adapter);
                linearLayoutManager.scrollToPositionWithOffset(lastPos, 0);
                changePosInDB();
                break;
            case R.id.shuffleCard:
                stopAnimItem();
                Collections.shuffle(words);
                recyclerView.setAdapter(adapter);
                changePosInDB();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void changePosInDB(){
        for (int i = 0; i < words.size(); i ++){
            Word word = words.get(i);
            word.setPositionInCardAction(i);
            dataBase.editWordPositionInCardAction(lesson, word);
        }
    }

    class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder>{
        CardRecyclerAdapter(ArrayList<Word> words, Context context) {
            this.words= words;
            mContext = context;
        }
        ArrayList<Word> words;
        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
            return new CardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
            final Word word = words.get(position);
            holder.ivStar.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            //word.setPositionInCardAction(position);
            dataBase.editWordPositionInCardAction(lesson, word);
            if (lesson.getCurrentLanguageCards()==1){
                holder.tvKorWord.setText(word.getKoreanWord());
                holder.tvRusWord.setText(word.getRussianWord());
            }else {
                holder.tvRusWord.setText(word.getKoreanWord());
                holder.tvKorWord.setText(word.getRussianWord());
            }
            if (word.isSelected()==0){
                holder.ivStar.setColorFilter(getResources().getColor(R.color.grayM));
                holder.ivStar2.setColorFilter(getResources().getColor(R.color.grayM));

            } else {
                holder.ivStar.setColorFilter(getResources().getColor(R.color.yellow));
                holder.ivStar2.setColorFilter(getResources().getColor(R.color.yellow));
            }
            holder.ivStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(word.isSelected()==0){
                        holder.ivStar.setColorFilter(getResources().getColor(R.color.yellow));
                        holder.ivStar2.setColorFilter(getResources().getColor(R.color.yellow));
                        word.setSelected(1);
                        dataBase.editWordSelect(lesson,word);
                    }else {
                        holder.ivStar.setColorFilter(getResources().getColor(R.color.grayM));
                        holder.ivStar2.setColorFilter(getResources().getColor(R.color.grayM));
                        word.setSelected(0);
                        dataBase.editWordSelect(lesson,word);
                    }
                }
            });
            holder.ivStar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(word.isSelected()==0){
                        holder.ivStar.setColorFilter(getResources().getColor(R.color.yellow));
                        holder.ivStar2.setColorFilter(getResources().getColor(R.color.yellow));
                        word.setSelected(1);
                        dataBase.editWordSelect(lesson,word);
                    }else {
                        holder.ivStar.setColorFilter(getResources().getColor(R.color.grayM));
                        holder.ivStar2.setColorFilter(getResources().getColor(R.color.grayM));
                        word.setSelected(0);
                        dataBase.editWordSelect(lesson,word);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return words.size();
        }

        public class CardViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView tvKorWord,tvRusWord;
            ImageView ivStar, ivStar2;
            FlipView flipView;
            CardViewHolder(View itemView) {
                super(itemView);
                tvKorWord = itemView.findViewById(R.id.korean_card_tv);
                tvRusWord = itemView.findViewById(R.id.russian_card_tv);
                tvKorWord = itemView.findViewById(R.id.korean_card_tv);
                tvRusWord = itemView.findViewById(R.id.russian_card_tv);
                ivStar = itemView.findViewById(R.id.star_front);
                ivStar2 = itemView.findViewById(R.id.star_back);
                flipView = itemView.findViewById(R.id.mFlipView);

            }

        }


    }



    private class SpaceItemDecoration extends RecyclerView.ItemDecoration  {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int marginBot = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, view.getResources().getDisplayMetrics());
            int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, view.getResources().getDisplayMetrics());
            if(parent.getChildAdapterPosition(view) == 0){
                outRect.top = marginTop;
                outRect.bottom = 0;
            }
            if (parent.getChildAdapterPosition(view) == words.size()-1){
                outRect.top = 0;
                outRect.bottom = marginBot;
            }
        }
    }
}
