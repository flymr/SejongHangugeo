package com.flymr92gmail.sejonghangugeo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.flymr92gmail.sejonghangugeo.Adapters.LessonsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.ViewClickListener;
import com.flymr92gmail.sejonghangugeo.activities.LegendsActivity;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;


import java.util.ArrayList;

import java.util.Random;


import butterknife.BindView;
import butterknife.ButterKnife;



public class LessonsPageFragment extends Fragment implements ViewClickListener {


    @BindView(R.id.lessons_rv)
    RecyclerView lessonsRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private UserDataBase userDataBase;
    private ArrayList<Lesson> lessonArrayList;
    private LessonsAdapter lessonsAdapter;
    private Context context;
    private LessonsCreateFolder dialogCreateFragment;

    public static LessonsPageFragment newInstance() {
        return new LessonsPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lessons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        context = getActivity();
        setupRecyclerView();
        dialogCreateFragment = new LessonsCreateFolder();
        setupLessonCreateFolder(dialogCreateFragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCreateFragment.show(getActivity().getSupportFragmentManager(),"New lesson");
            }
        });

    }

    private void setupRecyclerView(){
        userDataBase = new UserDataBase(context);
        lessonArrayList = userDataBase.getAllLessons();
        if (lessonArrayList.size() == 0) {
            userDataBase.createNewLesson("title");
            lessonArrayList = userDataBase.getAllLessons();
        }
        userDataBase.close();
        lessonsAdapter = new LessonsAdapter(lessonArrayList, context, getDailyLegend(), this);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lessonsRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        lessonsRecyclerView.setAdapter(lessonsAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(lessonsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(lessonsRecyclerView);
        lessonsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, lessonsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {
                if (position!=0) {
                    Intent intent = new Intent(context, LessonActivity.class);
                    intent.putExtra("lessonId", lessonArrayList.get(position).getLessonId());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, final int position) {
            }
        }));
    }

    private void setupLessonCreateFolder(LessonsCreateFolder fragment){
        fragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                userDataBase = new UserDataBase(context);
                if (userDataBase.getAllLessons().size()!=lessonArrayList.size()) {
                    int dbLastItemPos = lessonArrayList.size();
                    lessonArrayList.add(dbLastItemPos, userDataBase.getAllLessons().get(dbLastItemPos));
                    lessonsAdapter.notifyItemInserted(dbLastItemPos);
                    lessonsRecyclerView.smoothScrollToPosition(lessonsAdapter.getItemCount()-1);
                }
                userDataBase.close();
            }
        });
    }

    private int getRandomInt(int distance){
        int randomInt;
        randomInt = new Random().nextInt(distance);
        return randomInt;
    }

    private Legend getDailyLegend(){
        AppDataBase appDataBase = new AppDataBase(context);
        Legend legend = appDataBase.getDailyLegend(getRandomInt(appDataBase.getLegends().size()));
        appDataBase.close();
        return legend;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LegendsActivity.class));
    }

    @Override
    public void deleteItem(int position) {
        PrefManager prefManager = new PrefManager(context);
        if (prefManager.getLastLessonID() == lessonArrayList.get(position).getLessonId())
            prefManager.saveLastLessonID(0);
        UserDataBase dataBase = new UserDataBase(context);
        dataBase.deleteLesson(lessonArrayList.get(position));
        dataBase.close();
        lessonArrayList.remove(position);
        lessonsAdapter.notifyItemRemoved(position);
    }
}
