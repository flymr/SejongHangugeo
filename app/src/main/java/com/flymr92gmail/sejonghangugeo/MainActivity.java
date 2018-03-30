package com.flymr92gmail.sejonghangugeo;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.Fragments.MailDialog;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.activities.BookActivity;
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
    @BindView(R.id.iv_loader)
    ImageView ivLoader;
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

    private UserDataBase dataBase;
    private FlowingDrawer mDrawer;
    private PrefManager prefManager;
    private int modeNightNO = AppCompatDelegate.MODE_NIGHT_NO;
    private int modeNightYes = AppCompatDelegate.MODE_NIGHT_YES;
    private int modeNightAuto = AppCompatDelegate.MODE_NIGHT_AUTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        llLastPlaces.setOnClickListener(this);
        llThemeSettings.setOnClickListener(this);
        lastLesson.setOnClickListener(this);
        lastBook.setOnClickListener(this);
        llSendMassage.setOnClickListener(this);
        shareBtn.setOnClickListener(this);

        rbNight.setOnClickListener(this);
        rbDay.setOnClickListener(this);
        rbAuto.setOnClickListener(this);

        dataBase = new UserDataBase(this);

        mDrawer = findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
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
        //mRecyclerView.setAdapter(adapter);

        Toolbar toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");

        }


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

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int materialPagerBg = R.color.listBgColor;
        final int image1Id;
        final int image2Id;
        image1Id = R.drawable.page1_title;
        image2Id = R.drawable.page2_title;
        final Drawable drawable2 = new BitmapDrawable(getResources(),
                decodeSampledBitmapFromResource(getResources(), image2Id, metrics.widthPixels/2, 0));
        final Drawable drawable1 = new BitmapDrawable(getResources(),
                decodeSampledBitmapFromResource(getResources(), image1Id, metrics.widthPixels/2, 0));
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
                                materialPagerBg,
                                drawable1
                        );
                    case 1:
                        logo.setVisibility(View.GONE);
                        return HeaderDesign.fromColorResAndDrawable(
                                materialPagerBg,
                                drawable2
                        );

                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        navigationTabStrip.setViewPager(mViewPager.getViewPager()); // exp
//        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());





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


        if (prefManager.getIsFirstAppActivation()) {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivity(intent);
            prefManager.setIsFirstAppActivation(false);
        }


    }



    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
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

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
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
        ivLoader.setVisibility(View.GONE);
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
                break;
            case R.id.favorites:

                break;
            case R.id.share:
                shareAppLink();
                break;
            case R.id.help:

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
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_AUTO){
            int currentTheme = getCurrentNightMode();
            getDelegate().applyDayNight();
            if (currentTheme != getCurrentNightMode()) recreate();
        }
    }
}
