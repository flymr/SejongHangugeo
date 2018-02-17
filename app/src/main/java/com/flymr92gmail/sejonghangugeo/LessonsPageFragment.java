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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flymr92gmail.sejonghangugeo.Adapters.LessonsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.Date;
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

               if (position!=0) {
                   Intent intent = new Intent(getActivity(), LessonActivity.class);
                   intent.putExtra("lessonId", lessonArrayList.get(position).getLessonId());
                   startActivity(intent);
               }
            }

            @Override
            public void onLongItemClick(View view, final int position) {
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
                            int dbLastItemPos = lessonArrayList.size();
                            lessonArrayList.add(dbLastItemPos, userDataBase.getAllLessons().get(dbLastItemPos));
                            lessonsAdapter.notifyItemInserted(dbLastItemPos);
                            lessonsRecyclerView.smoothScrollToPosition(lessonsAdapter.getItemCount()-1);

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
