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

public class LessonsPageFragment extends Fragment{
    @BindView(R.id.lessons_rv)
    RecyclerView lessonsRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private UserDataBase userDataBase;
    private ArrayList<Lesson> lessonArrayList;
    private LessonsAdapter lessonsAdapter;
    private PrefManager prefManager;
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
        prefManager = new PrefManager(context);
        userDataBase = new UserDataBase(context);
        lessonArrayList = userDataBase.getAllLessons();
        if (lessonArrayList.size() == 0) {
            userDataBase.createNewLesson("title");
            lessonArrayList = userDataBase.getAllLessons();
        }
        lessonsAdapter = new LessonsAdapter(lessonArrayList, context, getDailyLegend());
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
                   prefManager.saveLastLessonID(lessonArrayList.get(position).getLessonId());

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
                        if (userDataBase.getAllLessons().size()!=lessonArrayList.size()) {
                            int dbLastItemPos = lessonArrayList.size();
                            lessonArrayList.add(dbLastItemPos, userDataBase.getAllLessons().get(dbLastItemPos));
                            lessonsAdapter.notifyItemInserted(dbLastItemPos);
                            lessonsRecyclerView.smoothScrollToPosition(lessonsAdapter.getItemCount()-1);

                        }
                    }
                });
                dialogCreateFragment.show(getActivity().getSupportFragmentManager(),"New lesson");



            }
        });


    }

    private Legend getDailyLegend(){
        AppDataBase appDataBase = new AppDataBase(context);
        Legend legend;
        String json= prefManager.getAddedLegendsId();
            try {
                Log.d("lessons", "secondTry true");

                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("ids");
                JSONObject jsonObject1 = new JSONObject(appDataBase.getLegendsIds());
                JSONArray jsonArray1 = jsonObject1.getJSONArray("ids");
                ArrayList<Integer> allIds = new ArrayList<>();
                ArrayList<Integer> addedIds = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    addedIds.add(jsonArray.getInt(i));
                }
                for (int i = 0; i < jsonArray1.length(); i++) {
                    allIds.add(jsonArray1.getInt(i));
                }
                int randomInt;
                if (addedIds.size() < allIds.size()) {
                    allIds.removeAll(addedIds);
                    randomInt = getRandomInt(allIds.size());
                    allIds.get(randomInt);
                }
                else {
                    jsonArray = new JSONArray();
                    randomInt = getRandomInt(appDataBase.getLegends().size()-1);
                }
                legend = appDataBase.getLegendById(allIds.get(randomInt));
                jsonArray.put(allIds.get(randomInt));
                jsonObject = new JSONObject();
                jsonObject.put("ids", jsonArray);
                json = jsonObject.toString();
                prefManager.setAddedLegendsId(json);
                appDataBase.close();
                return legend;
            } catch (JSONException e) {
                Log.d("lessons", "secondTry false");
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try {
                    Log.d("lessons", "firstTry true");
                    legend = appDataBase.getLegends().get(getRandomInt(appDataBase.getLegends().size()-1));
                    jsonArray.put(legend.getmId());
                    jsonObject.put("ids", jsonArray);
                    json = jsonObject.toString();
                    prefManager.setAddedLegendsId(json);
                    appDataBase.close();
                    return legend;
                }catch (JSONException e2){
                    Log.d("lessons", "firstTry false");

                }
            }
        legend = appDataBase.getLegends().get(getRandomInt(appDataBase.getLegends().size()-1));
        appDataBase.close();
        return legend;
    }

    private int getRandomInt(int distance){
        int randomInt;
        randomInt = new Random().nextInt(distance);
        return randomInt;
    }



    @Override
    public void onResume() {
        super.onResume();
        String currentDateTimeString = (String) DateFormat.format("dd-MM-yyyy",new Date());
        if (!prefManager.getDateOfAddedLegend().equals(currentDateTimeString))
            lessonsAdapter = new LessonsAdapter(lessonArrayList, context, getDailyLegend());
    }
}
