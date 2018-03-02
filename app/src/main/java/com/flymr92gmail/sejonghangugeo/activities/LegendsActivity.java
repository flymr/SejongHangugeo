package com.flymr92gmail.sejonghangugeo.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.flymr92gmail.sejonghangugeo.Adapters.LegendsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LegendsActivity extends AppCompatActivity {
    @BindView(R.id.rv_legends)
    RecyclerView recyclerView;
    private AppDataBase dataBase;
    private LegendsAdapter adapter;
    private ArrayList<Legend> legends;
    @BindView(R.id.toolbar_legends)
    Toolbar toolbar;
    private int lastPosition;
    private LinearLayoutManager llManager;
    private PrefManager prefManager;
    @BindView(R.id.fab_up)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legends);
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);
        lastPosition = prefManager.getLastLegendsPoss();
        dataBase = new AppDataBase(this);
        legends = dataBase.getLegends();
        adapter = new LegendsAdapter(legends);
        llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        setupToolbar();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void setupToolbar(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle(R.string.culture);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    onBackPressed();
                }
            });
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        prefManager.setLastLegendsPoss(llManager.findLastVisibleItemPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        llManager.scrollToPositionWithOffset(lastPosition,0);
    }
}

