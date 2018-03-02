package com.flymr92gmail.sejonghangugeo;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.Adapters.BookAdapter;
import com.flymr92gmail.sejonghangugeo.Adapters.LessonsAdapter;
import com.flymr92gmail.sejonghangugeo.Adapters.NewWordsRecyclerAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.ViewClickListener;
import com.flymr92gmail.sejonghangugeo.activities.BookActivity;
import com.flymr92gmail.sejonghangugeo.activities.LegendsActivity;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;

public class MainPageFragment extends Fragment implements ViewClickListener{



    @BindView(R.id.main_rv)
    RecyclerView mRecyclerView;

    private PrefManager prefManager;
    private BookAdapter bookAdapter;
    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        prefManager = new PrefManager(getActivity());


        //setup materialviewpager

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setHasFixedSize(true);
        bookAdapter = new BookAdapter(getDailyLegend(), this);
        mRecyclerView.setAdapter(bookAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {
                if (position!=0){
                    startActivity(new Intent(getActivity(), BookActivity.class));


                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }



    private Legend getDailyLegend(){
        String currentDateTimeString = (String) DateFormat.format("dd-MM-yyyy",new Date());
        AppDataBase appDataBase = new AppDataBase(getActivity());
        Legend legend;
        String json= prefManager.getAddedLegendsId();
        if (!prefManager.getDateOfAddedLegend().equals(currentDateTimeString)){
            prefManager.setDateOfAddedLegend(currentDateTimeString);
            try {
                Log.d("main", "secondTry true");

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
                    randomInt = getRandomInt(3);
                }
                legend = appDataBase.getLegendById(allIds.get(randomInt));
                jsonArray.put(allIds.get(randomInt));
                jsonObject = new JSONObject();
                jsonObject.put("ids", jsonArray);
                json = jsonObject.toString();
                prefManager.setAddedLegendsId(json);
                return legend;
            } catch (JSONException e) {
                Log.d("main", "secondTry false");
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try {
                    Log.d("main", "firstTry true");
                    legend = appDataBase.getDailyLegend(getRandomInt(3));
                    jsonArray.put(legend.getmId());
                    jsonObject.put("ids", jsonArray);
                    json = jsonObject.toString();
                    prefManager.setAddedLegendsId(json);
                    return legend;
                }catch (JSONException e2){
                    Log.d("main", "firstTry false");

                }
            }

        }else {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("ids");
                return appDataBase.getLegendById(jsonArray.getInt(jsonArray.length() - 1));
            }catch (JSONException e){

            }

        }
       // return appDataBase.getLegends().get(getRandomInt(3));
    return null;
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
         bookAdapter = new BookAdapter(getDailyLegend(), this);
    }

    @Override
    public void onViewClicked() {
       startActivity(new Intent(getActivity(), LegendsActivity.class));
    }
}