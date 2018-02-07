package com.flymr92gmail.sejonghangugeo;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flymr92gmail.sejonghangugeo.Adapters.LessonsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 09.12.2017.
 */

public class LessonsPageFragment extends Fragment{
    @BindView(R.id.lessons_rv)
    RecyclerView lessonsRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private UserDataBase userDataBase;
    private ArrayList<Lesson> lessonArrayList;
    private LessonsAdapter lessonsAdapter;


    public static LessonsPageFragment newInstance() {
        return new LessonsPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lessons, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        userDataBase = new UserDataBase(getActivity());
        lessonArrayList = userDataBase.getAllLessons();
        if (lessonArrayList.size() == 0) {
            userDataBase.createNewLesson("title");
            lessonArrayList = userDataBase.getAllLessons();
        }
        lessonsAdapter = new LessonsAdapter(lessonArrayList, getActivity());
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lessonsRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        lessonsRecyclerView.setAdapter(lessonsAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(lessonsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(lessonsRecyclerView);
        lessonsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), lessonsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               /* for(int a =0; a < userDataBase.getAllLessons().size();a++){
                    Lesson lesson = userDataBase.getAllLessons().get(a);
                    lesson.setPositionIndex(position);
                    userDataBase.editLessonsPositionInArray(lesson);
                }*/
               if (position!=0) {
                   Intent intent = new Intent(getActivity(), LessonActivity.class);
                   intent.putExtra("lessonId", lessonArrayList.get(position).getLessonId());
                   startActivity(intent);
               }
            }

            @Override
            public void onLongItemClick(View view, final int position) {
               /* final String[] items = {"Удалить","Переименовать"};
                AlertDialog.Builder builder = new AlertDialog.Builder(LessonsActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        userDataBase.deleteLesson(lessonArrayList.get(position));
                                        lessonArrayList = userDataBase.getAllLessons();
                                        lessonsAdapter = new LessonsAdapter(lessonArrayList,LessonsActivity.this);
                                        lessonsRecyclerView.setAdapter(lessonsAdapter);
                                        break;
                                    case 1:
                                        LessonsDialogCreateFragment lessonsDialogCreateFragment = new LessonsDialogCreateFragment();
                                        lessonsDialogCreateFragment.setmLesson(lessonArrayList.get(position));
                                        lessonsDialogCreateFragment.show(getSupportFragmentManager(),"more");

                                        break;
                                }
                            }
                        })
                        .setCancelable(true);
                AlertDialog alert = builder.create();
                alert.show();*/
            }
        }));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Animation anim = android.view.animation.AnimationUtils.loadAnimation(fab.getContext(),  R.anim.myalpha);
                 anim.setDuration(200L);
                 fab.startAnimation(anim);*/

                //Toast.makeText(getActivity(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();

                LessonsCreateFolder dialogCreateFragment = new LessonsCreateFolder();
                dialogCreateFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //setupLessonsAdapter();
                        if (userDataBase.getAllLessons().size()!=lessonArrayList.size()) {
                            int newItem = userDataBase.getAllLessons().size()-1;
                            lessonArrayList.add(userDataBase.getAllLessons().get(newItem));
                            lessonsAdapter.notifyItemInserted(newItem);
                            lessonsRecyclerView.smoothScrollToPosition(lessonsRecyclerView.getAdapter().getItemCount() - 1);
                            lessonArrayList.clear();
                            lessonArrayList = userDataBase.getAllLessons();
                        }
                    }
                });
                dialogCreateFragment.show(getActivity().getSupportFragmentManager(),"Новый урок");


                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
            }
        });



        //setup materialviewpager




        //Use this now

    }

   public void setupLessonsAdapter(){
       userDataBase = new UserDataBase(getActivity());
       lessonArrayList = userDataBase.getAllLessons();
       lessonsAdapter = new LessonsAdapter(lessonArrayList, getActivity());
       lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       //lessonsRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
       lessonsRecyclerView.setAdapter(lessonsAdapter);
       ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(lessonsAdapter);
       ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
       touchHelper.attachToRecyclerView(lessonsRecyclerView);


   }



}