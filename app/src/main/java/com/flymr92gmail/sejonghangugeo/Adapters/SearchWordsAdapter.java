package com.flymr92gmail.sejonghangugeo.Adapters;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;

import android.text.SpannableString;
import android.text.Spanned;

import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.Fragments.LessonsDialogAddFragment;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.Constants;
import com.flymr92gmail.sejonghangugeo.Utils.SpeechActionListener;

import java.util.ArrayList;


public class SearchWordsAdapter extends RecyclerView.Adapter<SearchWordsAdapter.ViewHolder> {
    private ArrayList<Word> mWords;
    private Context mContext;
    private String searchingText;
    private Constants.Language language;
    private FragmentManager fragmentManager;
    private SpeechActionListener speechActionListener;


    public SearchWordsAdapter(ArrayList<Word> words, Context context, String searchingText, Constants.Language language, FragmentManager fragmentManager, SpeechActionListener speechActionListener) {
        this.mWords=words;
        this.mContext = context;
        this.searchingText = searchingText;
        this.language = language;
        this.fragmentManager = fragmentManager;
        this.speechActionListener = speechActionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item_book,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Word word = mWords.get(position);
        if (language == Constants.Language.Korean){
            Spannable kor = new SpannableString(word.getKoreanWord());
            int start = word.getKoreanWord().indexOf(searchingText);
            if (start < 0){
                start = word.getRussianWord().toLowerCase().indexOf(searchingText.toLowerCase());
            }
            kor.setSpan(new BackgroundColorSpan(mContext.getResources().getColor(R.color.colorAccent)), start,
                    start + searchingText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            kor.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.white)), start,
                    start+ searchingText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvKorWord.setText(kor);
            holder.tvRusWord.setText(word.getRussianWord());
        }else {
            Spannable rus = new SpannableString(word.getRussianWord());
            int start = word.getRussianWord().indexOf(searchingText);
            if (start < 0){
                start = word.getRussianWord().toLowerCase().indexOf(searchingText.toLowerCase());
            }
            rus.setSpan(new BackgroundColorSpan(mContext.getResources().getColor(R.color.colorAccent)), start,
                    start + searchingText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            rus.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.white)), start,
                    start+ searchingText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvRusWord.setText(rus);
            holder.tvKorWord.setText(word.getKoreanWord());
        }
        Log.d("VIEWHOLDER", "onBindViewHolder: " + word.getCorrectCount());
    }


    @Override
    public int getItemCount () {
        return mWords.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvKorWord,tvRusWord;
        ImageView ivAdd, ivSpeech;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvKorWord = itemView.findViewById(R.id.korean_word_tv);
            tvRusWord = itemView.findViewById(R.id.russian_word_tv);
            ivAdd = itemView.findViewById(R.id.iv_add);
            ivSpeech = itemView.findViewById(R.id.speech_iv);
            cardView = itemView.findViewById(R.id.word_item_cv);
            ivAdd.setOnClickListener(this);
            ivSpeech.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_add:
                    LessonsDialogAddFragment lessonsDialogAddFragment = new LessonsDialogAddFragment();
                    lessonsDialogAddFragment.setWord(mWords.get(getAdapterPosition()));
                    lessonsDialogAddFragment.show(fragmentManager,"Choise Lesson");
                    break;
                case R.id.speech_iv:
                    if (speechActionListener != null){
                        speechActionListener.onSpeechClick(getAdapterPosition(), v);
                    }
                    break;
            }
        }
    }
}

