package com.flymr92gmail.sejonghangugeo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;

import com.flymr92gmail.sejonghangugeo.Adapters.PreviewAdapter;
import com.flymr92gmail.sejonghangugeo.R;
import com.gigamole.navigationtabstrip.NavigationTabStrip;


public class PreviewActivity extends AppCompatActivity {
    ImageView iv_close;
    ImageView iv_next;
    RecyclerView recyclerView;
    NavigationTabStrip nts;
    private LinearLayoutManager llManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        iv_close = findViewById(R.id.close_preview);
        iv_next = findViewById(R.id.next_page_preview);
         recyclerView = findViewById(R.id.rv_preview);
        nts = findViewById(R.id.nts_preview);
          setupRecyclerView();
          setupNts();
          setupIvClickListener();
    }

    private void setupRecyclerView(){
        llManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llManager);
        recyclerView.setAdapter(new PreviewAdapter());
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);

    }

    private void setupNts(){
        nts.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                recyclerView.smoothScrollToPosition(index);
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

}