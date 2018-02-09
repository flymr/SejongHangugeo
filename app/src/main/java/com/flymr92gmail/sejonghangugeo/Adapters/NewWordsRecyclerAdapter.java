package com.flymr92gmail.sejonghangugeo.Adapters;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
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
    private Animation plusToCross;
    private Animation croosToPlus;
    public interface OnRecyclerViewItemClickListener {
        void onAddClick(boolean[] selects, int position);
    }
    private OnRecyclerViewItemClickListener mClickListener;

    public NewWordsRecyclerAdapter(ArrayList<Word> words, Context context, OnRecyclerViewItemClickListener clickListener) {
        this.mWords=words;
        this.mContext = context;
        selects = new boolean[mWords.size()];
        this.mClickListener = clickListener;
        plusToCross = AnimationUtils.loadAnimation(mContext, R.anim.plus_to_cross);
        croosToPlus = AnimationUtils.loadAnimation(mContext, R.anim.cross_to_plus);
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

            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ivAdd.getAnimation() == plusToCross){
                        colorAnimator(ivAdd, "colorFilter", R.color.redM,
                                R.color.navigationBarColor, 300, croosToPlus);
                        selects[getAdapterPosition()] = false;
                    }else {
                        colorAnimator(ivAdd, "colorFilter", R.color.navigationBarColor,
                                R.color.redM, 300, plusToCross);
                        selects[getAdapterPosition()] = true;
                    }
                    if (mClickListener != null) {
                        mClickListener.onAddClick(selects, getAdapterPosition());
                    }
                }
            });

        }
        private void colorAnimator(View view, String propertyName, int firstColor, int secondColor, int duration, Animation anim){
            ObjectAnimator.ofObject(view, propertyName, new ArgbEvaluator(), mContext.getResources().getColor(firstColor),
                    mContext.getResources().getColor(secondColor)).setDuration(duration).start();
            view.startAnimation(anim);
        }
    }
}
