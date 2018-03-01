package com.flymr92gmail.sejonghangugeo;

import android.animation.Animator;
import android.content.Intent;
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
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
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

    private UserDataBase dataBase;
    private FlowingDrawer mDrawer;
    private PrefManager prefManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefManager = new PrefManager(this);
        if (prefManager.getIsFirstAppActivation()) {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        llLastPlaces.setOnClickListener(this);
        llThemeSettings.setOnClickListener(this);
        lastLesson.setOnClickListener(this);
        lastBook.setOnClickListener(this);
        llSendMassage.setOnClickListener(this);

        rbNight.setOnCheckedChangeListener(this);
        rbDay.setOnCheckedChangeListener(this);
        rbAuto.setOnCheckedChangeListener(this);

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
        final Drawable drawable2 = new BitmapDrawable(getResources(),
                decodeSampledBitmapFromResource(getResources(), R.drawable.white_imtitle, metrics.widthPixels, 0));
        final Drawable drawable1 = new BitmapDrawable(getResources(),
                decodeSampledBitmapFromResource(getResources(), R.drawable.title2, metrics.widthPixels, 0));


        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.navigationBarColor,
                                drawable1
                                 );
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.black,
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


        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    String success = "Ваше письмо доставленно";
                    String error = "Ошибка. Проверьте подключение к интернету";
                    String sending = "Письмо отправляется...";
                    BackgroundMail.newBuilder(MainActivity.this)
                            .withUsername("appmailsejong@gmail.com")
                            .withPassword("kanoxa94")
                            .withMailto("flymr92@gmail.com")
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withSubject("this is the subject")
                            .withBody("this is the body")
                            .withProcessVisibility(true)
                            .withSendingMessageSuccess(success)
                            .withSendingMessageError(error)
                            .withSendingMessage(sending)
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {

                                }
                            })
                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                @Override
                                public void onFail() {
                                    //do some magic

                                }
                            })
                            .send();
                }
            });
        }


        switch (prefManager.getAppTheme()){
            case 1:
                rbDay.setChecked(true);
                break;
            case 2:
                rbNight.setChecked(true);
                break;
            case 3:
                rbAuto.setChecked(true);
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

    public void startPreloadAnim(int position, int cx, int cy){
        ivLoader.setVisibility(View.VISIBLE);
        // get the final radius for the clipping circle
        int dx = Math.max(cx, ivLoader.getWidth() - cx);
        int dy = Math.max(cy, ivLoader.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(ivLoader, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(MainActivity.this, BookActivity.class);
                startActivity(intent);
                try{
                   overridePendingTransition(0,0);
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private static void expand(final View v) {
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

    private static void collapse(final View v) {
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
                lastBookTv.setText("SejongHangugeo 1 (с" + prefManager.getLastBookPage() + ")");
                lastGramTv.setText("Ikhimcheg (с" + prefManager.getLastGramPage() + ")");
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
               // startPreloadAnim(lastBook.getWidth()/2, lastBook.getHeight()/2);
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
            case R.id.send_massage:
                MailDialog mailDialog = new MailDialog();
                mailDialog.show(getFragmentManager(), "new massage");
                break;
            case R.id.favorites:

                break;
            case R.id.share:

                break;
            case R.id.help:

                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       if (isChecked){
           if (buttonView.getId() == R.id.rb_auto){
               rbDay.setChecked(false);
               rbNight.setChecked(false);
           }else if (buttonView.getId() == R.id.rb_night){
               rbDay.setChecked(false);
               rbAuto.setChecked(false);
           }else if (buttonView.getId() == R.id.rb_day){
               rbAuto.setChecked(false);
               rbNight.setChecked(false);
           }
       }
    }
}
