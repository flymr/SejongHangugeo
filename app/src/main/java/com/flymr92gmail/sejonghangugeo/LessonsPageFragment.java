package com.flymr92gmail.sejonghangugeo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flymr92gmail.sejonghangugeo.Adapters.LessonsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.ViewClickListener;
import com.flymr92gmail.sejonghangugeo.activities.LegendsActivity;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by hp on 09.12.2017.
 */

public class LessonsPageFragment extends Fragment implements ViewClickListener {
    @BindView(R.id.lessons_rv)
    RecyclerView lessonsRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private UserDataBase userDataBase;
    private ArrayList<Lesson> lessonArrayList;
    private LessonsAdapter lessonsAdapter;
    private Context context;


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


        context = getActivity();
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LessonsCreateFolder dialogCreateFragment = new LessonsCreateFolder();
                dialogCreateFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //setupLessonsAdapter();
                        userDataBase = new UserDataBase(context);
                        if (userDataBase.getAllLessons().size()!=lessonArrayList.size()) {
                            int dbLastItemPos = lessonArrayList.size();
                            lessonArrayList.add(dbLastItemPos, userDataBase.getAllLessons().get(dbLastItemPos));
                            lessonsAdapter.notifyItemInserted(dbLastItemPos);
                            lessonsRecyclerView.smoothScrollToPosition(lessonsAdapter.getItemCount()-1);
                            userDataBase.close();
                        }
                    }
                });
                dialogCreateFragment.show(getActivity().getSupportFragmentManager(),"New lesson");



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
        Legend legend = appDataBase.getDailyLegend(getRandomInt(238));
        appDataBase.close();
        return legend;
    }


    @Override
    public void onResume() {
        super.onResume();
        //String currentDateTimeString = (String) DateFormat.format("dd-MM-yyyy",new Date());
       // if (!prefManager.getDateOfAddedLegend().equals(currentDateTimeString))lessonsAdapter = new LessonsAdapter(lessonArrayList, context, getDailyLegend());
    }

    @Override
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LegendsActivity.class));
    }
}
