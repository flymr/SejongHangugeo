package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;

import java.util.ArrayList;

/**
 * Created by hp on 09.01.2018.
 */

public class LearnFinalResultWordAdapter extends RecyclerView.Adapter<LearnFinalResultWordAdapter.ViewHolder> {
   private ArrayList<Word> words;
   private Context context;
   private Lesson lesson;
   private UserDataBase dataBase;
   static final int HEADER = 0;
   static final int NORMAL_ITEM = 1;
   static final int CONTENT_ACTIVITY = 2;

   public LearnFinalResultWordAdapter(ArrayList<Word> words, Context context, Lesson lesson){
       this.lesson = lesson;
       this.words = words;
       this.context = context;
       dataBase = new UserDataBase(context);
   }

    @Override
    public LearnFinalResultWordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_item,parent,false);
        return new LearnFinalResultWordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LearnFinalResultWordAdapter.ViewHolder holder, int position) {
        Word word = words.get(position);
        switch (getItemViewType(position)){
            case HEADER:
                String s;
                if (word.getMissCount()!=0){
                    s = "Допущено ошибок: "+word.getMissCount();
                }else {
                    s = "Без ошибок";
                }
                holder.tvHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(s);
                break;
            case  NORMAL_ITEM:
                holder.tvHeader.setVisibility(View.GONE);
                break;
        }
        holder.tvKorean.setText(word.getKoreanWord());
        holder.tvRuss.setText(word.getRussianWord());
        holder.ivStar.setVisibility(View.VISIBLE);
        if (word.isSelected()==0){
            holder.ivStar.setColorFilter(context.getResources().getColor(R.color.grayM));
        } else {
            holder.ivStar.setColorFilter(context.getResources().getColor(R.color.yellow));
        }
    }

    @Override
    public int getItemViewType(int position) {
       if (position == 0) return HEADER;
       else if (words.get(position).getMissCount()!=words.get(position - 1).getMissCount())return HEADER;
       else return NORMAL_ITEM;
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       ImageView ivStar;
       TextView tvHeader, tvKorean, tvRuss;
        public ViewHolder(View itemView) {
            super(itemView);
            tvKorean = itemView.findViewById(R.id.korean_word_tv);
            tvRuss = itemView.findViewById(R.id.russian_word_tv);
            tvHeader = itemView.findViewById(R.id.learn_final_tv);
            ivStar = itemView.findViewById(R.id.star_iv);
            ivStar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Word word = words.get(getAdapterPosition());
            if (word.isSelected() == 0) {
                ivStar.setColorFilter(context.getResources().getColor(R.color.yellow));
                word.setSelected(1);
                dataBase.editWordSelect(lesson, word);

            } else {
                ivStar.setColorFilter(context.getResources().getColor(R.color.grayM));
                word.setSelected(0);
                dataBase.editWordSelect(lesson, word);

            }
        }
    }
}
