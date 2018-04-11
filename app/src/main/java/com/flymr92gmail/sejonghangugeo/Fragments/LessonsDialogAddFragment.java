package com.flymr92gmail.sejonghangugeo.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.Adapters.LessonsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.RecyclerItemClickListener;

import java.util.ArrayList;


public class LessonsDialogAddFragment extends DialogFragment {


    private View form=null;
    private UserDataBase dataBase;
    private ArrayList<Lesson> lessons;
    private RecyclerView recyclerView;
    private LessonsAdapter lessonsAdapter;
    private Word mWord;
    private Dialog dialog;
    private Button addNewLessonCardView;
    private ArrayList<Word> words;

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public void setWord(Word word) {
        this.mWord = word;
    }

    @Override
    @Nullable
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = null;
        dataBase = new UserDataBase(getContext());
            form= getActivity().getLayoutInflater()
                    .inflate(R.layout.lessons_dialog, null);
        addNewLessonCardView = form.findViewById(R.id.add_new_lesson_dialog_card_view);
        addNewLessonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LessonsDialogCreateFragment lessonsDialogCreateFragment =  new LessonsDialogCreateFragment();
                if (mWord!=null)
                    lessonsDialogCreateFragment.setWord(mWord);
                if (words!=null)
                    lessonsDialogCreateFragment.setWords(words);
               if (getFragmentManager()!=null)lessonsDialogCreateFragment.show(getFragmentManager(),"create lesson");
                dialog.dismiss();

            }
        });
        recyclerView = form.findViewById(R.id.lessons_dialog_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {
                Lesson lesson = lessons.get(position);
                if (mWord != null) {
                    dataBase.addNewWord(lesson.getLessonTable(), mWord);
                    //dialog.dismiss();
                    showSnackbar(view);
                }
                if (words != null) {
                    dataBase.addNewWords(lesson.getLessonTable(), words);
                    //dialog.dismiss();
                    showSnackbar(view);
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        dialog = (builder.setTitle(R.string.add_to_folder).setView(form)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create());
        return dialog;
    }
    private void setupAdapter(RecyclerView recyclerView){
        lessons = dataBase.getAllLessons();
        lessons.remove(0);
        lessonsAdapter = new LessonsAdapter(lessons,getContext());
        recyclerView.setAdapter(lessonsAdapter);
    }

    private void showSnackbar (View view) {
        Snackbar mSnackbar = Snackbar.make(view, "Добавлено", Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        View view1 = mSnackbar.getView();
        TextView tv = view1.findViewById(android.support.design.R.id.snackbar_text);
        view1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tv.setTextColor(Color.WHITE);
        mSnackbar.show();

        mSnackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                dialog.dismiss();
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d("FRAGMENT", "setUserVisibleHint: ");
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
       // new LessonsDialogAddFragment().show(getFragmentManager(),"Уроки");
        Log.d("FRAGMENT", "onResume: ");
        super.onResume();
    }
    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
}
