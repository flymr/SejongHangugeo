package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;

import java.util.ArrayList;

/**
 * Created by hp on 14.12.2017.
 */

public class EditLessonAdapter extends RecyclerView.Adapter<EditLessonAdapter.ViewHolder>{
    static final int TYPE_CURENT_WORD = 0;
    static final int TYPE_NEW_WORD = 1;
    private Context mContext;
    private Lesson mLesson;
    private UserDataBase mUserDataBase;
    private ArrayList<Word> words;
    private InputMethodManager imm;


    public EditLessonAdapter(Context mContext, Lesson mLesson){
      this.mContext = mContext;
      this.mLesson = mLesson;
      mUserDataBase = new UserDataBase(mContext);
      words = mUserDataBase.getWordsInLesson(mLesson.getLessonTable());

    }

    @Override
    public EditLessonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_word_item,parent,false);
         return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(EditLessonAdapter.ViewHolder holder, int position) {
    switch (getItemViewType(position)){
        case TYPE_CURENT_WORD:
            Word word = words.get(position);
            holder.editTextKor.setText("");
            holder.editTextKor.setText(word.getKoreanWord());
            holder.editTextRus.setText("");
            holder.editTextRus.setText(word.getRussianWord());

            break;
        case  TYPE_NEW_WORD:
            holder.editTextKor.requestFocus();
            holder.editTextKor.setHint(mContext.getResources().getString(R.string.slovo));
            holder.editTextRus.setHint(mContext.getResources().getString(R.string.perevod));
           // holder.editTextKor.performClick();
           // if (holder.editTextKor.getText().toString().equals("")) holder.editTextKor.requestFocus();
          //  else holder.editTextRus.requestFocus();
            holder.headerAdd.setVisibility(View.VISIBLE);
            break;
    }


    }

    @Override
    public int getItemCount() {
        return words.size()+1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnFocusChangeListener, EditText.OnEditorActionListener{
        EditText editTextKor;
        EditText editTextRus;
        View separator;
        TextView headerAdd;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            separator = itemView.findViewById(R.id.word_edit_separate);
            editTextKor = itemView.findViewById(R.id.korean_word_et);
            editTextRus = itemView.findViewById(R.id.russian_word_et);
            headerAdd = itemView.findViewById(R.id.tv_add_new);
            cardView = itemView.findViewById(R.id.edit_word_cv);
            editTextKor.setHorizontallyScrolling(false);
            editTextKor.setMaxLines(Integer.MAX_VALUE);
            editTextRus.setHorizontallyScrolling(false);
            editTextRus.setMaxLines(Integer.MAX_VALUE);
           // editTextRus.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
            imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            editTextRus.setOnFocusChangeListener(this);
            editTextKor.setOnFocusChangeListener(this);
            editTextKor.setOnEditorActionListener(this);
            editTextRus.setOnEditorActionListener(this);


        }




        @Override
        public void onFocusChange(View view, boolean b) {
            if(view.getId()==R.id.korean_word_et) onFocus(b, editTextKor);
            else if (view.getId()==R.id.russian_word_et) onFocus(b, editTextRus);
            switch (getItemViewType()){
                case TYPE_CURENT_WORD:
                    if (!b){
                        editWord(editTextKor);
                        editWord(editTextRus);

                    }
                    break;
                case  TYPE_NEW_WORD:

                    break;
            }
        }

        private void onFocus(boolean isFocus , EditText editText){
            if (isFocus){
               // separator.setBackgroundColor(mContext.getResources().getColor(R.color.redM));
               // editText.setTextColor(mContext.getResources().getColor(R.color.redM));
                cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.md_purple));
            } else {
               // separator.setBackgroundColor(mContext.getResources().getColor(R.color.white));
               // editText.setTextColor(mContext.getResources().getColor(R.color.white));
                cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.navigationBarColor));
            }
        }


        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (textView.getId() == R.id.korean_word_et){
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    editTextRus.requestFocus();
                   // imm.showSoftInput(editTextKor,InputMethodManager.SHOW_IMPLICIT);
                    return true;
                }
            }
           if (textView == editTextRus){
               if (actionId == EditorInfo.IME_ACTION_DONE) {
                   switch (getItemViewType()){
                       case TYPE_CURENT_WORD:
                           cardView.requestFocus();

                           break;
                       case  TYPE_NEW_WORD:
                           if (addWord()) {
                               //cardView.requestFocus();
                                notifyItemInserted(words.size()-1);
                               // notifyDataSetChanged();
                               editTextKor.requestFocus();


                           }
                           break;
                   }
                   return true;
               }
           }
            return false;
        }

        private boolean addWord(){
            Word word = new Word();
            if (!editTextKor.getText().toString().equals("")&&!editTextRus.getText().toString().equals("")) {
                word.setKoreanWord(editTextKor.getText().toString());
                word.setRussianWord(editTextRus.getText().toString());
                mUserDataBase.addNewWord(mLesson.getLessonTable(), word);
                words = mUserDataBase.getWordsInLesson(mLesson.getLessonTable());
                editTextKor.setText("");
                editTextRus.setText("");

                return true;
            }else {
                Toast.makeText(mContext,"Пустое полe", Toast.LENGTH_SHORT).show();
                if (editTextKor.getText().toString().equals("")) editTextKor.requestFocus();
                else editTextRus.requestFocus();
                return false;
            }
        }

        private void editWord(EditText editText){
            Word word = words.get(getAdapterPosition());
            if (!editText.getText().toString().equals("")) {
                if (editText==editTextKor)word.setKoreanWord(editText.getText().toString());
                else if (editText==editTextRus)word.setRussianWord(editText.getText().toString());
                mUserDataBase.editWord(mLesson.getLessonTable(), word);
            }else {
                editTextKor.setText(word.getKoreanWord());
                editTextRus.setText(word.getRussianWord());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position<words.size()){
            return TYPE_CURENT_WORD;
        }
        else {
            return TYPE_NEW_WORD;
        }
    }


}
