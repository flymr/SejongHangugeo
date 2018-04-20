package com.flymr92gmail.sejonghangugeo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.Fragments.FavoritesFragment;
import com.flymr92gmail.sejonghangugeo.Fragments.MailDialog;

import com.flymr92gmail.sejonghangugeo.Utils.Helper;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.activities.BookActivity;
import com.flymr92gmail.sejonghangugeo.activities.GramBookActivity;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.zagum.expandicon.ExpandIconView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;


import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    @BindView(R.id.nts_main)
    NavigationTabStrip navigationTabStrip;
    @BindView(R.id.expand_last_places)
    LinearLayout llExpandLP;
    @BindView(R.id.expand_theme_settings)
    LinearLayout expandThemeSettings;
    @BindView(R.id.rb_day)
    AppCompatRadioButton rbDay;
    @BindView(R.id.rb_night)
    AppCompatRadioButton rbNight;
    @BindView(R.id.rb_auto)
    AppCompatRadioButton rbAuto;
    @BindView(R.id.last_lesson)
    LinearLayout lastLesson;
    @BindView(R.id.drawerlayout)
    FlowingDrawer mDrawer;
    @BindView(R.id.drawer_sky_iv)
    ImageView drawerIv;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentTheme(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setClickListenerToViews();
        setupFlowingDrawer();
        setupToolbar();
        setupViewPager();
        setRbChecked();
        thisIsFirstActivation();
        setDrawerIv();
    }


    private void getCurrentTheme(Bundle savedInstanceState){
        prefManager = new PrefManager(this);
        if (savedInstanceState == null){
            int currentThemeIndex = prefManager.getAppTheme();
            switch (currentThemeIndex){
                case AppCompatDelegate.MODE_NIGHT_NO:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case AppCompatDelegate.MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case AppCompatDelegate.MODE_NIGHT_AUTO:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                    break;
            }
        }
    }


    private void thisIsFirstActivation(){
        if (prefManager.getIsFirstAppActivation()) {
            prefManager.setIsFirstAppActivation(false);
            mDrawer.openMenu(true);
        }
    }


    private void setClickListenerToViews(){
        findViewById(R.id.last_places).setOnClickListener(this);
        findViewById(R.id.theme_setting).setOnClickListener(this);
        findViewById(R.id.last_book).setOnClickListener(this);
        findViewById(R.id.last_gramm).setOnClickListener(this);
        lastLesson = findViewById(R.id.last_lesson);
        lastLesson.setOnClickListener(this);
        findViewById(R.id.send_massage).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        rbNight.setOnClickListener(this);
        rbDay.setOnClickListener(this);
        rbAuto.setOnClickListener(this);
        findViewById(R.id.thanks_to).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
    }


    private void setupToolbar(){
        Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            try {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setTitle("");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        mDrawer.openMenu(true);
                    }
                });
                Drawable menu = getResources().getDrawable(R.drawable.ic_menu_24dp);
                getSupportActionBar().setHomeAsUpIndicator(menu);
            }catch (NullPointerException e){

            }


        }
    }


    private void setupFlowingDrawer(){
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState,final int newState) {

            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
            }
        });
    }


    private void setDrawerIv(){
        Drawable drawerDrawable = getResources().getDrawable(R.drawable.sky);
        drawerIv.setImageDrawable(drawerDrawable);
    }


    private void setRbChecked(){
        switch (prefManager.getAppTheme()){
            case AppCompatDelegate.MODE_NIGHT_NO:
                rbDay.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                rbNight.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                rbAuto.setChecked(true);
        }
    }


    private void setupViewPager(){
        mViewPager.setColor(R.color.colorFolder, 100);
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 2) {
                    case 0:
                        return MainPageFragment.newInstance();
                    case 1:
                        return LessonsPageFragment.newInstance();
                    default:
                        return MainPageFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return getResources().getString(R.string.uchebnik_kursa);
                    case 1:
                        return getResources().getString(R.string.uchebnie_moduli);
                }
                return "";
            }


        });
        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();

                }
            });
        }
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.listBgColor,
                                 getResources().getDrawable(R.drawable.page1_title)
                        );
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.listBgColor,
                                  getResources().getDrawable(R.drawable.page2_title)

                        );

                }

                return null;
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        navigationTabStrip.setViewPager(mViewPager.getViewPager()); // exp
    }

    //drawer actions
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.last_places:
                lastPlaces();
                break;
            case R.id.last_lesson:
                lastLesson();
                break;
            case R.id.last_book:
                lastBookPage();
                break;
            case R.id.last_gramm:
                lastGramPage();
                break;
            case R.id.theme_setting:
                themeSetting();
                break;
            case R.id.rb_day:
                setDay();
                break;
            case R.id.rb_night:
                setNight();
                break;
            case R.id.rb_auto:
                setAutoNight();
                break;
            case R.id.send_massage:
                sendMessage();
                break;
            case R.id.thanks_to:
                thanksTo();
                break;
            case R.id.share:
                shareAppLink();
                break;
            case R.id.help:
                goToGitHub();
                break;
        }
    }


    private void goToGitHub(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.home_page)));
        startActivity(intent);
    }

    private int getCurrentNightMode(){
        return getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    }

    private void shareAppLink(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String textToSend = "link my app";
        intent.putExtra(Intent.EXTRA_TEXT, textToSend);
        try
        {
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void thanksTo(){
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.show(getFragmentManager(), "thanks to");
        fragment.setCancelable(true);
    }

    private void sendMessage(){
        MailDialog mailDialog = new MailDialog();
        mailDialog.show(getFragmentManager(), "new message");
        mailDialog.setCancelable(true);
    }

    private void setAutoNight(){
        rbDay.setChecked(false);
        rbNight.setChecked(false);
        prefManager.setAppTheme(AppCompatDelegate.MODE_NIGHT_AUTO);
        int currentTheme = getCurrentNightMode();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        getDelegate().applyDayNight();
        if (currentTheme != getCurrentNightMode()){
            recreate();

        }
    }

    private void setNight(){
        rbDay.setChecked(false);
        rbAuto.setChecked(false);
        prefManager.setAppTheme(AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (getCurrentNightMode() != Configuration.UI_MODE_NIGHT_YES){
            recreate();
        }
    }

    private void setDay(){
        rbAuto.setChecked(false);
        rbNight.setChecked(false);
        prefManager.setAppTheme(AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (getCurrentNightMode() != Configuration.UI_MODE_NIGHT_NO){
            recreate();
        }
    }

    private void themeSetting(){
        if (expandThemeSettings.getVisibility() == View.GONE)
            Helper.expand(expandThemeSettings);
        else
            Helper.collapse(expandThemeSettings);
    }

    private void lastGramPage(){
        startActivity(new Intent(this, GramBookActivity.class));

    }

    private void lastBookPage(){
        startActivity(new Intent(this, BookActivity.class));
    }

    private void lastLesson(){
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra("lessonId", prefManager.getLastLessonID());
        startActivity(intent);

    }


    private void lastPlaces(){
        TextView lastLessonTv = findViewById(R.id.tv_last_lesson);
        TextView lastBookTv = findViewById(R.id.tv_last_book);
        TextView lastGramTv = findViewById(R.id.tv_last_gramm);
       if (prefManager.getLastLessonID()!=0){
           UserDataBase dataBase = new UserDataBase(this);
           lastLesson.setVisibility(View.VISIBLE);
           lastLessonTv.setText(dataBase.getLessonByPrimaryId(prefManager.getLastLessonID()).getLessonName());
           dataBase.close();
       } else {
           lastLesson.setVisibility(View.GONE);
       }
        String sejong = getString(R.string.sejongHangugeo1) + getString(R.string.last_page_of, prefManager.getLastBookPage());
        lastBookTv.setText(sejong);
        String ikhimcheg = getString(R.string.ikhimchek) + getString(R.string.last_page_of, prefManager.getLastGramPage());
        lastGramTv.setText(ikhimcheg);
        if (llExpandLP.getVisibility() == View.GONE)
            Helper.expand(llExpandLP);
        else
            Helper.collapse(llExpandLP);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.unbindDrawables(drawerIv);
        Helper.unbindDrawables(mDrawer);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
