package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;

import java.util.ArrayList;


public class LearnResultWordsAdapter extends RecyclerView.Adapter<LearnResultWordsAdapter.ViewHolder> {
    private ArrayList<Word> mWords;
    private Context mContext;
    public LearnResultWordsAdapter(ArrayList<Word> words, Context context) {
        mWords=words;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Word word = mWords.get(position);
        holder.layoutCircles.setVisibility(View.VISIBLE);
        holder.tvKorWord.setText(word.getKoreanWord());
        holder.tvRusWord.setText(word.getRussianWord());
        Log.d("VIEWHOLDER", "onBindViewHolder: "+word.getCorrectCount());
        if (word.getCorrectCount()==0){
            holder.firstCircle.setColorFilter(mContext.getResources().getColor(R.color.redM));
            holder.secondCircle.setColorFilter(mContext.getResources().getColor(R.color.grayM));
            holder.tvKorWord.setTextColor(Color.RED);
            holder.tvRusWord.setTextColor(Color.RED);
            holder.separate.setBackgroundColor(Color.RED);
        }else if (word.getCorrectCount()==1){
            holder.firstCircle.setColorFilter(mContext.getResources().getColor(R.color.green));
            holder.secondCircle.setColorFilter(mContext.getResources().getColor(R.color.grayM));
        }else if (word.getCorrectCount()==2){
            holder.firstCircle.setColorFilter(mContext.getResources().getColor(R.color.green));
            holder.secondCircle.setColorFilter(mContext.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return mWords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvKorWord,tvRusWord;
        ImageView firstCircle;
        ImageView secondCircle;
        LinearLayout layoutCircles;
        View separate;
        public ViewHolder(View itemView) {
            super(itemView);
            tvKorWord = itemView.findViewById(R.id.korean_word_tv);
            tvRusWord = itemView.findViewById(R.id.russian_word_tv);
            firstCircle = itemView.findViewById(R.id.firstCircle);
            secondCircle = itemView.findViewById(R.id.secondCircle);
            layoutCircles = itemView.findViewById(R.id.layout_circles);
            separate = itemView.findViewById(R.id.word_separate);
        }

    }
}
