package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.flymr92gmail.sejonghangugeo.DeleteModListener;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.SpeechActionListener;
import com.flymr92gmail.sejonghangugeo.Utils.WordsSpeech;


import java.util.ArrayList;


public class LessonWordsSelectableRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int FOOTER_VIEW = 1;

    private ArrayList<Word> mWords;
    private Context mContext;
    private UserDataBase dataBase;
    private Lesson lesson;
    private boolean deleteMod = false;
    private SpeechActionListener speechActionListener;

    public LessonWordsSelectableRecyclerAdapter(ArrayList<Word> words, Context context, Lesson lesson, SpeechActionListener speechActionListener) {
        this.mWords=words;
        this.mContext = context;
        dataBase = new UserDataBase(context);
        this.lesson = lesson;
        this.speechActionListener = speechActionListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item_footer, parent, false);

            FooterViewHolder vh = new FooterViewHolder(view);

            return vh;
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.words_item, parent, false);
        WordViewHolder vh = new WordViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {


        switch (getItemViewType(position)){
            case FOOTER_VIEW:
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                break;
            default:
                WordViewHolder viewHolder =(WordViewHolder)holder;
                Word word = mWords.get(position);
                if (deleteMod){
                    viewHolder.ivStar.setVisibility(View.GONE);
                    viewHolder.ivDelete.setVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.ivDelete.setVisibility(View.GONE);
                    viewHolder.ivStar.setVisibility(View.VISIBLE);
                }
                viewHolder.ivSpeech.setVisibility(View.VISIBLE);
                viewHolder.tvKorWord.setText(word.getKoreanWord());
                viewHolder.tvRusWord.setText(word.getRussianWord());
                if (word.isSelected()==0){
                    viewHolder.ivStar.setColorFilter(mContext.getResources().getColor(R.color.grayM));

                } else {
                    viewHolder.ivStar.setColorFilter(mContext.getResources().getColor(R.color.yellow));

                }
                break;
        }




    }

    public void deleteMod(boolean deleteMod){
        this.deleteMod = deleteMod;
    }

    @Override
    public int getItemCount() {
        return mWords.size()+1;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvKorWord, tvRusWord;
        ImageView ivStar;
        ImageView ivDelete;
        ImageView ivSpeech;
        @Override
        public String toString() {
            return super.toString();
        }

        WordViewHolder(View itemView) {
            super(itemView);
                tvKorWord = itemView.findViewById(R.id.korean_word_tv);
                tvRusWord = itemView.findViewById(R.id.russian_word_tv);
                ivStar = itemView.findViewById(R.id.star_iv);
                ivDelete = itemView.findViewById(R.id.delete_iv);
                ivSpeech = itemView.findViewById(R.id.speech_iv);
                ivSpeech.setOnClickListener(this);
                ivStar.setOnClickListener(this);
                ivDelete.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()) {
                case R.id.speech_iv:
                    if (speechActionListener != null){
                        speechActionListener.onSpeechClick(getAdapterPosition());
                    }
                    break;
                case R.id.star_iv:

                    Word word = mWords.get(position);
                    if (word.isSelected() == 0) {
                        ivStar.setColorFilter(mContext.getResources().getColor(R.color.yellow));
                        word.setSelected(1);
                        dataBase.editWordSelect(lesson, word);

                    } else {
                        ivStar.setColorFilter(mContext.getResources().getColor(R.color.grayM));
                        word.setSelected(0);
                        dataBase.editWordSelect(lesson, word);
                        if (lesson.getLessonTabIndex()==1){
                            mWords.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                    break;
                case R.id.delete_iv:
                    removeAt(getAdapterPosition());
                    break;
            }
        }


    }
    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void removeAt(int position) {
       try {
           dataBase.deleteLessonWord(lesson.getLessonTable(), mWords.get(position));
           mWords.remove(position);
           notifyItemRemoved(position);
       }catch (ArrayIndexOutOfBoundsException n){

       }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mWords.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

}

