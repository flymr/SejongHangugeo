package com.flymr92gmail.sejonghangugeo;

/**
 * Created by xyarim on 3/6/17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.EditText;


import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;

import java.util.ArrayList;

public class LessonsCreateFolder extends DialogFragment {


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
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = null;
        dataBase = new UserDataBase(getContext());
        form= getActivity().getLayoutInflater()
                .inflate(R.layout.create_lesson_dialog, null);
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
                          //  mListener.onDialogPositiveClick(LessonsCreateFolder.this);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // mListener.onDialogNegativeClick(LessonsCreateFolder.this);
                        dialog.dismiss();
                    }
                }).create());
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //final Activity activity = getActivity();
       // if (activity instanceof DialogInterface.OnDismissListener) {
          // ((DialogInterface.OnDismissListener) activity).onDismiss(dialog); }
       // Fragment parentFragment = getParentFragment();
     //   if (parentFragment instanceof DialogInterface.OnDismissListener) {
       //     ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
      //  }
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }

}
