package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;

import java.util.ArrayList;


public class NewWordsRecyclerAdapter extends RecyclerView.Adapter<NewWordsRecyclerAdapter.ViewHolder> {
    private ArrayList<Word> mWords;
    private Context mContext;
    public boolean[] selects;
    public NewWordsRecyclerAdapter(ArrayList<Word> words, Context context) {
        mWords=words;
        mContext = context;
       selects = new boolean[mWords.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item_book,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Word word = mWords.get(position);
        //holder.ivAdd.setVisibility(View.VISIBLE);
        holder.tvKorWord.setText(word.getKoreanWord());
        holder.tvRusWord.setText(word.getRussianWord());
        Log.d("VIEWHOLDER", "onBindViewHolder: " + word.getCorrectCount());
    }
    @Override
    public int getItemCount () {
        return mWords.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvKorWord,tvRusWord;
        ImageView ivAdd;
        public ViewHolder(View itemView) {
            super(itemView);
            tvKorWord = itemView.findViewById(R.id.korean_word_tv);
            tvRusWord = itemView.findViewById(R.id.russian_word_tv);
            ivAdd = itemView.findViewById(R.id.iv_add);
            final Animation plusToCross = AnimationUtils.loadAnimation(mContext, R.anim.plus_to_cross);
            final Animation croosToPlus = AnimationUtils.loadAnimation(mContext, R.anim.cross_to_plus);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ivAdd.getAnimation() == plusToCross){
                        ivAdd.startAnimation(croosToPlus);
                        selects[getAdapterPosition()] = false;
                    }else {
                        ivAdd.startAnimation(plusToCross);
                        selects[getAdapterPosition()] = true;
                    }
                }
            });
        }

    }
}
