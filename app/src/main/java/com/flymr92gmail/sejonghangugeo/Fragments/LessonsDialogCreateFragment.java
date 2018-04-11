package com.flymr92gmail.sejonghangugeo.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;

import java.util.ArrayList;


public class LessonsDialogCreateFragment extends DialogFragment {


    private View form=null;
    private UserDataBase dataBase;
    private Word word;
    private ArrayList<Word> words;

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public void setmLesson(Lesson mLesson) {
        this.mLesson = mLesson;
    }

    private Lesson mLesson;

    public void setWord(Word word) {
        this.word = word;
    }

    private Dialog dialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = null;
        dataBase = new UserDataBase(getContext());
        form= getActivity().getLayoutInflater().inflate(R.layout.create_lesson_dialog, null);
        final EditText editText = form.findViewById(R.id.lessonName_et);
        if (mLesson!=null){
            editText.setText(mLesson.getLessonName());
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        dialog = (builder.setTitle("Создать новый урок").setView(form)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mLesson!=null){
                            dataBase.editLessonName(mLesson,editText.getText().toString());
                        }else {
                            dataBase.createNewLesson(editText.getText().toString().trim());
                            dialog.dismiss();
                                LessonsDialogAddFragment lessonsDialogAddFragment = new LessonsDialogAddFragment();
                            if (word != null)
                                lessonsDialogAddFragment.setWord(word);
                            if (words != null)
                                lessonsDialogAddFragment.setWords(words);
                                lessonsDialogAddFragment.show(getFragmentManager(), "create lesson");
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (word!=null){
                            LessonsDialogAddFragment lessonsDialogAddFragment =new LessonsDialogAddFragment();
                            lessonsDialogAddFragment.setWord(word);
                            lessonsDialogAddFragment.show(getFragmentManager(),"create lesson");
                        }
                    }
                }).create());
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface unused) {
        try {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(this);
            trans.commit();
            manager.popBackStack();
        }catch (NullPointerException e){

        }
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
}
