package com.flymr92gmail.sejonghangugeo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flymr92gmail.sejonghangugeo.Adapters.BookAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.ViewClickListener;
import com.flymr92gmail.sejonghangugeo.activities.BookActivity;
import com.flymr92gmail.sejonghangugeo.activities.GramBookActivity;
import com.flymr92gmail.sejonghangugeo.activities.LegendsActivity;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPageFragment extends Fragment implements ViewClickListener{



    @BindView(R.id.main_rv)
    RecyclerView mRecyclerView;

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setHasFixedSize(true);
        bookAdapter = new BookAdapter(getDailyLegend(), this);
        mRecyclerView.setAdapter(bookAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {
                if (position == 0){

                }else if (position == 1){
                    startActivity(new Intent(getActivity(), GramBookActivity.class));
                }else if (position == 2){
                    startActivity(new Intent(getActivity(), BookActivity.class));
                }else {
                    Toast.makeText(getActivity(),"Еще не готово :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }


    private Legend getDailyLegend(){
        AppDataBase appDataBase = new AppDataBase(getActivity());
        Legend legend = appDataBase.getDailyLegend(getRandomInt(238));
        appDataBase.close();
        return legend;
    }


    private int getRandomInt(int distance){
        long seed = System.nanoTime();
        int randomInt;
        randomInt = new Random(seed).nextInt(distance);
        return randomInt;
    }

    @Override
    public void onResume() {
        super.onResume();
        bookAdapter = new BookAdapter(getDailyLegend(), this);
        mRecyclerView.setAdapter(bookAdapter);
    }

    @Override
    public void onViewClicked() {
       startActivity(new Intent(getActivity(), LegendsActivity.class));
    }

    @Override
    public void deleteItem(int position) {

    }
}