package com.flymr92gmail.sejonghangugeo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.Fragments.FavoritesFragment;
import com.flymr92gmail.sejonghangugeo.Fragments.MailDialog;

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

    @BindView(R.id.drawerlayout)
    FlowingDrawer mDrawer;

    private UserDataBase dataBase;
    private PrefManager prefManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentTheme(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setClickListenerToViews();
        initObj();
        setupFlowingDrawer();
        setupToolbar();
        setupViewPager();
        setRbChecked();
        thisIsFirstActivation();
    }




    private void getCurrentTheme(Bundle savedInstanceState){
        prefManager = new PrefManager(this);
        if (savedInstanceState == null){
            Log.d("savedInstanceState", "   ==null");
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


    private void initObj(){
        dataBase = new UserDataBase(this);

    }



    private void setClickListenerToViews(){
        findViewById(R.id.last_places).setOnClickListener(this);
        findViewById(R.id.theme_setting).setOnClickListener(this);
        findViewById(R.id.last_book).setOnClickListener(this);
        findViewById(R.id.last_gramm).setOnClickListener(this);
        findViewById(R.id.last_lesson).setOnClickListener(this);
        findViewById(R.id.send_massage).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        rbNight.setOnClickListener(this);
        rbDay.setOnClickListener(this);
        rbAuto.setOnClickListener(this);
        findViewById(R.id.favorites).setOnClickListener(this);
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
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

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
                        logo.setVisibility(View.VISIBLE);
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.listBgColor,
                                 getResources().getDrawable(R.drawable.page1_title)
                        );
                    case 1:
                        logo.setVisibility(View.GONE);
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.listBgColor,
                                  getResources().getDrawable(R.drawable.page4_title)

                        );

                }

                return null;
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        navigationTabStrip.setViewPager(mViewPager.getViewPager()); // exp
    }


    private void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();


        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight*2 / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight*2 / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.last_places:
                TextView lastLessonTv = findViewById(R.id.tv_last_lesson);
                TextView lastBookTv = findViewById(R.id.tv_last_book);
                TextView lastGramTv = findViewById(R.id.tv_last_gramm);
                lastLessonTv.setText(dataBase.getLessonByPrimaryId(prefManager.getLastLessonID()).getLessonName());
                String sejong = getResources().getString(R.string.sejongHangugeo1) + " (стр. " + prefManager.getLastBookPage() + ")";
                lastBookTv.setText(sejong);
                String ikhimcheg = getResources().getString(R.string.ikhimchek) + " (стр. " + prefManager.getLastGramPage() + ")";
                lastGramTv.setText(ikhimcheg);
                ExpandIconView expandBtn = findViewById(R.id.expand_btn);
                if (llExpandLP.getVisibility() == View.GONE){
                    expandBtn.setState(ExpandIconView.LESS, true);
                    expand(llExpandLP);
                }else {
                    expandBtn.setState(ExpandIconView.MORE, true);
                    collapse(llExpandLP);
                }
                break;
            case R.id.last_lesson:
                UserDataBase dataBase = new UserDataBase(this);
                if (1 < dataBase.getAllLessons().size()){
                    mDrawer.closeMenu(false);
                    Intent intent = new Intent(this, LessonActivity.class);
                    intent.putExtra("lessonId", prefManager.getLastLessonID());
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "Вы не создали ни одной папки", Toast.LENGTH_SHORT).show();
                }
                dataBase.close();
                break;
            case R.id.last_book:
                startActivity(new Intent(this, BookActivity.class));
                mDrawer.closeMenu(false);
                break;
            case R.id.last_gramm:
                startActivity(new Intent(this, GramBookActivity.class));
                break;
            case R.id.theme_setting:
                ExpandIconView expandBtnTheme = findViewById(R.id.expand_themes);
                if (expandThemeSettings.getVisibility() == View.GONE){
                    expandBtnTheme.setState(ExpandIconView.LESS, true);
                    expand(expandThemeSettings);
                }else {
                    expandBtnTheme.setState(ExpandIconView.MORE, true);
                    collapse(expandThemeSettings);
                }
                break;
            case R.id.rb_day:
                rbAuto.setChecked(false);
                rbNight.setChecked(false);
                prefManager.setAppTheme(AppCompatDelegate.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                if (getCurrentNightMode() != Configuration.UI_MODE_NIGHT_NO){
                    recreate();
                }
                break;
            case R.id.rb_night:
                rbDay.setChecked(false);
                rbAuto.setChecked(false);
                prefManager.setAppTheme(AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                if (getCurrentNightMode() != Configuration.UI_MODE_NIGHT_YES){
                    recreate();
                }
                break;
            case R.id.rb_auto:
                rbDay.setChecked(false);
                rbNight.setChecked(false);
                prefManager.setAppTheme(AppCompatDelegate.MODE_NIGHT_AUTO);
                int currentTheme = getCurrentNightMode();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                getDelegate().applyDayNight();
                if (currentTheme != getCurrentNightMode())
                    recreate();
                break;
            case R.id.send_massage:
                MailDialog mailDialog = new MailDialog();
                mailDialog.show(getFragmentManager(), "new massage");
                mailDialog.setCancelable(true);
                break;
            case R.id.favorites:
                FavoritesFragment fragment = new FavoritesFragment();
                fragment.show(getFragmentManager(), "thanks to");
                fragment.setCancelable(true);
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
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/flymr/SejongHangugeo"));
        startActivity(intent);

    }


    private int getCurrentNightMode(){
        return getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    }


    private void shareAppLink(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String textToSend = "link for my app";
        intent.putExtra(Intent.EXTRA_TEXT, textToSend);
        try
        {
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }


}
