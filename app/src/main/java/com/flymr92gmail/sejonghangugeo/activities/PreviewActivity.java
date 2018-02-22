package com.flymr92gmail.sejonghangugeo.activities;

import android.animation.ArgbEvaluator;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flymr92gmail.sejonghangugeo.Adapters.PreviewAdapter;
import com.flymr92gmail.sejonghangugeo.R;
import com.gigamole.navigationtabstrip.NavigationTabStrip;



public class PreviewActivity extends AppCompatActivity {
    private ImageView iv_close;
    private ImageView iv_next;
    private RecyclerView recyclerView;
    private NavigationTabStrip nts;
    private LinearLayoutManager llManager;
    private FrameLayout fl;
    private int[] colors;
    private ObjectAnimator objAnimator;
    private boolean scroolIsNext = true;
    private int startIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        iv_close = findViewById(R.id.close_preview);
        iv_next = findViewById(R.id.next_page_preview);
        fl = findViewById(R.id.fl_preview);
        recyclerView = findViewById(R.id.rv_preview);
        nts = findViewById(R.id.nts_preview);
        getColorArray();
        setupRecyclerView();
        setupNts();
        setupIvClickListener();

    }

    private void getColorArray(){
        TypedArray ta = getResources().obtainTypedArray(R.array.preview_color);
        colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();
        fl.setBackgroundColor(colors[0]);
    }

    private void setupRecyclerView(){
        llManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llManager);
        recyclerView.setAdapter(new PreviewAdapter(this));
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0){
                    nts.setTabIndex(llManager.findLastVisibleItemPosition());
                    scroolIsNext = true;
                } else if (0 > dx) {
                    nts.setTabIndex(llManager.findFirstVisibleItemPosition());
                    scroolIsNext = false;
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("newState      ", "" + newState);
                if (newState == 0){
                    nts.setTabIndex(llManager.findFirstCompletelyVisibleItemPosition());
                }
            }
        });

    }

    private void setupNts(){
        nts.setTabIndex(0);
        nts.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                startIndex = llManager.findFirstVisibleItemPosition();
                recyclerView.smoothScrollToPosition(index);
                startObjAnimator(startIndex, index);

            }

            @Override
            public void onEndTabSelected(String title, int index) {

            }
        });
    }

    private void setupIvClickListener(){
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(
                        llManager.findFirstCompletelyVisibleItemPosition()+1);


            }
        });
    }

    private void startObjAnimator(int start, int end){

        objAnimator = ObjectAnimator.ofObject(fl,
                "backgroundColor", new ArgbEvaluator(), colors[start], colors[end]);
        objAnimator.setDuration(500);
        objAnimator.start();

    }

}