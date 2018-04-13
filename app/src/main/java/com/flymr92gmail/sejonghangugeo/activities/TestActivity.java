package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Test;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.Helper;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.TouchImageView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private TouchImageView imageViewTest;
    private EditText editTextTest1;
    private EditText editTextTest2;
    private ArrayList<Test> testArrayList;
    private int sizeOfArray;
    private int currentTestCount=0;
    private Test currentTest;
    private Toolbar toolbar;
    private Button submitButton;
    private AppDataBase appDataBase;
    private InputMethodManager imm;
    private ImageButton btnNext, btnPrev;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TESTLOG", "onCreate: ");
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        appDataBase = new AppDataBase(this);
        testArrayList = appDataBase.getTest(intent.getIntExtra("page",-1));
        sizeOfArray = testArrayList.size();
        toolbar = findViewById(R.id.toolbar);
        setupToolbar();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initUi();
        if (currentTestCount==0){
            nextTest();
        }

    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle(R.string.titki);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }


    private void initUi(){
        btnNext = findViewById(R.id.btn_next_test);
        btnNext.setOnClickListener(this);
        btnPrev = findViewById(R.id.btn_prev_test);
        btnPrev.setOnClickListener(this);
        btnNext.setColorFilter(getResources().getColor(R.color.colorAccent));
        btnPrev.setColorFilter(getResources().getColor(R.color.colorAccent));
        imageViewTest = findViewById(R.id.iv_test);
        editTextTest2 = findViewById(R.id.learn_edit_text);
        editTextTest1 = findViewById(R.id.learn_edit_text_2);
       // progressTextView = findViewById(R.id.textProgress);
        submitButton = findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(this);
        editTextTest1.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    editTextTest2.requestFocus();
                    imm.showSoftInput(editTextTest2, InputMethodManager.SHOW_IMPLICIT);
                    return true;
                }
                return false;
            }
        });
        editTextTest2.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    submitButton.performClick();
                    return true;
                }
                return false;
            }
        });

    }
    private void nextTest(){
        updateBtn();
        if (currentTestCount==sizeOfArray){
            finish();
        } else if (currentTestCount<=sizeOfArray) {
            currentTest = null;
            currentTest = testArrayList.get(currentTestCount);
           // imageViewTest.setBackgroundDrawable(Helper.getImageFromAssets(currentTest.getImage(),this));
            imageViewTest.setImageDrawable(Helper.getImageFromAssets(currentTest.getImage(),this));
            editTextTest2.setText("");
            editTextTest1.setText("");
            if (currentTest.getSecondAnswer()!=null) {
                editTextTest1.setVisibility(View.VISIBLE);
                editTextTest1.requestFocus();
                editTextTest1.setHint(currentTest.getFirstHint());
                editTextTest2.setHint(currentTest.getSecondHint());
                imm.showSoftInput(editTextTest2, InputMethodManager.SHOW_IMPLICIT);
                Log.d("inputview", "nextTest: !=null");
            }else {
                editTextTest1.setVisibility(View.GONE);
                editTextTest2.requestFocus();
                editTextTest2.setHint(currentTest.getFirstHint());
                imm.showSoftInput(editTextTest2, InputMethodManager.SHOW_IMPLICIT);
                Log.d("inputview", "nextTest: ==null");
            }
            }
           // if (currentTestCount!=0) updateMenu(currentTestCount);
            currentTestCount++;

    }

    private void prevTest(){
       currentTestCount = currentTestCount -2;
       nextTest();
    }

    private void updateBtn(){
        if (currentTestCount == 0) btnPrev.setVisibility(View.GONE);
        else if (currentTestCount == sizeOfArray-1) btnNext.setVisibility(View.GONE);
        else {
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        }
    }

    private void showSnackbar(boolean wordIsCorrect, String word, View view){
        Snackbar snackbar = Snackbar.make(view, word, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        textView.setTextColor(Color.WHITE);
        if (wordIsCorrect)
            sbView.setBackgroundColor(Color.GREEN);
        else
            sbView.setBackgroundColor(Color.RED);
        snackbar.show();
    }

    private boolean checkTest() {

        Test test = currentTest;
        String tv2text = editTextTest1.getText().toString();
        String tv1text = editTextTest2.getText().toString();
        Log.w("MyLogTest",test.getSecondAnswer()+"....");
        if (test.getSecondAnswer()==null){
            if (test.getFirstAnswer().equals(tv1text)){
                return true;
            }else

                return false;
        }else {
            if ((test.getFirstAnswer().equals(tv2text) && test.getSecondAnswer().equals(tv1text))) {
                // test.getFirstAnswer().equals(tv2text)&&test.getSecondAnothAnswer().equals(tv1text

                return true;
            }else
                return false;
        }
    }

    private void submitAction(View view){

        if (checkTest()){
            showSnackbar(true,"Отлично!",view);
            nextTest();
        }else {
            showSnackbar(false,"Ошибка!",view);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next_test:
                nextTest();
                break;
            case R.id.btn_prev_test:
                prevTest();
                break;
            case R.id.submit_btn:
                submitAction(view);
                break;
        }
    }
}
