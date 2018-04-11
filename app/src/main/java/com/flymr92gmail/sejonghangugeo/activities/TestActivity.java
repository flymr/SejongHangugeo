package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
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

public class TestActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TouchImageView imageViewTest;
    private EditText editTextTest1;
    private EditText editTextTest2;
    private ImageView ivAudio;
    private ArrayList<Test> testArrayList;
    private int sizeOfArray;
    private int currentTestCount=0;
    private Test currentTest;
   // private TextView progressTextView;
    private Toolbar toolbar;
    private Button submitButton;
    private AppDataBase appDataBase;
    private InputMethodManager imm;
    private MediaPlayer mp;
    private int lastTrack;
    private Menu menu;
    private LinearLayout llTest;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TESTLOG", "onCreate: ");
        setContentView(R.layout.activity_test);
        llTest = findViewById(R.id.ll_test);
        Intent intent = getIntent();
        mp = new MediaPlayer();

        prefManager = new PrefManager(this);



        appDataBase = new AppDataBase(this);

        testArrayList = appDataBase.getTest(intent.getIntExtra("page",-1));
        if(testArrayList.get(1).getTrackId()!=0) llTest.setBackgroundColor(Color.WHITE);
        sizeOfArray = testArrayList.size();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Page "+testArrayList.get(1).getPage());
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUi();
        if (currentTestCount==0){
            nextTest();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test,menu);
        this.menu = menu;
        Log.d("TESTLOG", "onCreateOptionsMenu: ");
        updateMenu(0);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionPlayTrack:
                if (mp.isPlaying()) {
                    stopAudio();
                }else {
                    playAudio();
                }
                break;
            case R.id.actionNextTest:
                nextTest();
                break;
            case R.id.actionBackTest:
                prevTest();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp.isPlaying()) mp.stop();
    }
    private void initUi(){
        progressBar = findViewById(R.id.learn_progress);
        imageViewTest = findViewById(R.id.iv_test);
        editTextTest2 = findViewById(R.id.learn_edit_text);
        editTextTest1 = findViewById(R.id.learn_edit_text_2);
       // progressTextView = findViewById(R.id.textProgress);
        submitButton = findViewById(R.id.submit_btn);
        ivAudio = findViewById(R.id.iv_audio);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              submitAction(v);
            }
        });
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
        stopAudio();
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
            if (currentTestCount!=0)
            updateMenu(currentTestCount);
            currentTestCount++;

    }

    private void prevTest(){
        stopAudio();
        currentTest = null;
        currentTest = testArrayList.get(currentTestCount-1);
        imageViewTest.setBackgroundDrawable(Helper.getImageFromAssets(currentTest.getImage(),this));
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
        if (currentTestCount!=0)
            updateMenu(currentTestCount);
        currentTestCount--;

    }
    private void showSnackbar(boolean wordIsCorrect, String word, View view){
        Snackbar snackbar = Snackbar.make(view, word, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        ImageView imageView = new ImageView(this);
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
                //TODO: verify anothAnswer
                return true;
            }else
                return false;
        }
    }
    private void submitAction(View view){

        if (checkTest()){
            // TODO showProgress method
            showSnackbar(true,"Отлично!",view);
            nextTest();
        }else {
            //TODO showProgress method

            showSnackbar(false,"Ошибка!",view);
        }
    }
    public void nextTestOnclick(View view) {
        nextTest();
    }

    private void playAudio(){
        try {
                mp = new MediaPlayer();
                AssetFileDescriptor descriptor = getAssets().openFd("Audio/"+currentTest.getTrackId()+".wma");
                mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mp.prepare();
                mp.setVolume(1f, 1f);
                mp.setLooping(false);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        updateMenu(currentTestCount);
                    }
                });
                updateMenu(currentTestCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopAudio(){
        if (mp.isPlaying()){
            mp.stop();

            updateMenu(currentTestCount);
        }
    }
    private void updateMenu(int currentTestCount) {
        if (currentTestCount!=0)
            currentTestCount=this.currentTestCount;

        MenuItem prevMenuItem = menu.findItem(R.id.actionBackTest);
        MenuItem nextMenuItem = menu.findItem(R.id.actionNextTest);
        MenuItem playMenuItem = menu.findItem(R.id.actionPlayTrack);
        if (currentTest.getTrackId()==0)
            playMenuItem.setVisible(false);
        else
            playMenuItem.setVisible(true);

        Log.w("UPDATEMENU","currentTestCount: "+currentTestCount);
        Log.w("UPDATEMENU","sizeOfArray: "+sizeOfArray);
       // if(currentTestCount>0&&currentTestCount<=sizeOfArray){
       //     prevMenuItem.setVisible(true);
       // }else
       //     prevMenuItem.setVisible(false);

        if (currentTestCount<sizeOfArray-1){
            nextMenuItem.setVisible(true);
        }else if (currentTestCount==sizeOfArray-1){
            nextMenuItem.setVisible(true);
            nextMenuItem.setIcon(R.drawable.ic_close);
        }

        if (mp.isPlaying()){
           playMenuItem.setIcon(R.drawable.ic_volume_24dp);
        }else playMenuItem.setIcon(R.drawable.ic_play_vector);
    }


}
