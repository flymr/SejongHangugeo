package com.flymr92gmail.sejonghangugeo;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.Fragments.FavoritesFragment;
import com.flymr92gmail.sejonghangugeo.Fragments.MailDialog;
import com.flymr92gmail.sejonghangugeo.Utils.Helper;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.activities.BookActivity;
import com.flymr92gmail.sejonghangugeo.activities.GramBookActivity;
import com.flymr92gmail.sejonghangugeo.activities.PreviewActivity;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.zagum.expandicon.ExpandIconView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.datatype.Duration;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    @BindView(R.id.nts_main)
    NavigationTabStrip navigationTabStrip;
    @BindView(R.id.last_places)
    LinearLayout llLastPlaces;
    @BindView(R.id.send_massage)
    LinearLayout llSendMassage;
    @BindView(R.id.theme_setting)
    LinearLayout llThemeSettings;
    @BindView(R.id.favorites)
    LinearLayout llFavorites;
    @BindView(R.id.help)
    LinearLayout llHelp;
    @BindView(R.id.expand_last_places)
    LinearLayout llExpandLP;
    @BindView(R.id.last_lesson)
    LinearLayout lastLesson;
    @BindView(R.id.last_book)
    LinearLayout lastBook;
    @BindView(R.id.last_gramm)
    LinearLayout lastGram;
    @BindView(R.id.expand_theme_settings)
    LinearLayout expandThemeSettings;
    @BindView(R.id.rb_day)
    AppCompatRadioButton rbDay;
    @BindView(R.id.rb_night)
    AppCompatRadioButton rbNight;
    @BindView(R.id.rb_auto)
    AppCompatRadioButton rbAuto;
    @BindView(R.id.share)
    LinearLayout shareBtn;
    @BindView(R.id.iv_menu_main)
    ImageView drawerIv;
    @BindView(R.id.drawerlayout)
    FlowingDrawer mDrawer;

    private UserDataBase dataBase;
    private PrefManager prefManager;
    private Drawable drawable1;
    private Drawable drawable2;
    private Drawable drawerDrawable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentTheme(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setClickListenerToViews();
        initObj();
        //setupDrawable();
        setupFlowindDrawer();
        setupToolbar();
        setupViewPsger();
        setRbChecked();
       // thisIsFirstActivation();

    }


    private void getCurrentTheme(Bundle savedInstanceState){
        prefManager = new PrefManager(this);
        if (savedInstanceState == null){
            Log.d("savedInstanceState", "   ==null");
            int currentThemeIndex = prefManager.getAppTheme();
            switch (currentThemeIndex){
                case AppCompatDelegate.MODE_NIGHT_NO:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    //recreate();
                    break;
                case AppCompatDelegate.MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //recreate();
                    break;
                case AppCompatDelegate.MODE_NIGHT_AUTO:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                    // recreate();
                    break;
            }
        }
    }


    private void thisIsFirstActivation(){
        if (prefManager.getIsFirstAppActivation()) {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivity(intent);
            prefManager.setIsFirstAppActivation(false);
        }
    }


    private void initObj(){
        dataBase = new UserDataBase(this);

    }


    private void setupDrawable(){
       // DisplayMetrics metrics = new DisplayMetrics();
       // getWindowManager().getDefaultDisplay().getMetrics(metrics);
        drawerDrawable = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.drawer_title_image, (200 * (int)getResources().getDisplayMetrics().density)/2, 0));
        //drawable2 = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(),  R.drawable.page2_title, metrics.widthPixels/2, 0));
       // drawable1 = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.page1_title, metrics.widthPixels/2, 0));
    }


    private void setClickListenerToViews(){
        llLastPlaces.setOnClickListener(this);
        llThemeSettings.setOnClickListener(this);
        lastLesson.setOnClickListener(this);
        lastBook.setOnClickListener(this);
        llSendMassage.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        rbNight.setOnClickListener(this);
        rbDay.setOnClickListener(this);
        rbAuto.setOnClickListener(this);
        findViewById(R.id.preview).setOnClickListener(this);
        findViewById(R.id.favorites).setOnClickListener(this);
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


    private void setupFlowindDrawer(){
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        drawerIv.setImageDrawable(getResources().getDrawable(R.drawable.drawer_title_image));
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


    private void setupViewPsger(){
        //final DisplayMetrics metrics = new DisplayMetrics();
      //  getWindowManager().getDefaultDisplay().getMetrics(metrics);
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
                               // new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.page1_title, metrics.widthPixels/2, 0))
                        );
                    case 1:
                        logo.setVisibility(View.GONE);
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.listBgColor,
                               // new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.page1_title, metrics.widthPixels/2, 0))
                                  getResources().getDrawable(R.drawable.page2_title)


                        );

                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        navigationTabStrip.setViewPager(mViewPager.getViewPager()); // exp
//        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
    protected void onStart() {
        super.onStart();
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
                mDrawer.closeMenu(false);
                Intent intent = new Intent(this, LessonActivity.class);
                intent.putExtra("lessonId", prefManager.getLastLessonID());
                startActivity(intent);
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
                fragment.show(getFragmentManager(), "favorites");
                fragment.setCancelable(true);
                break;
            case R.id.share:
                shareAppLink();
                break;
            case R.id.help:

                break;
            case R.id.preview:
                startActivity(new Intent(this, PreviewActivity.class));
                break;
        }
    }


    private int getCurrentNightMode(){
        return getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    }


    private void shareAppLink(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String textToSend="link for my app";
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


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(drawerDrawable!=null){
            drawerDrawable.setCallback(null);
            drawerDrawable=null;
        }
        if (drawable1 != null){
            drawable1.setCallback(null);
            drawable1 = null;
        }
        if (drawable2 != null) {
            drawable2.setCallback(null);
            drawable2 = null;
        }
        if (mViewPager !=null) {
            mViewPager.getHeaderBackgroundContainer().removeAllViewsInLayout();
            mViewPager.removeCallbacks(null);
            mViewPager = null;
        }
    }


}
