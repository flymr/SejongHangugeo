package com.flymr92gmail.sejonghangugeo.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.flymr92gmail.sejonghangugeo.Adapters.NavBookAdapter;
import com.flymr92gmail.sejonghangugeo.Adapters.NewWordsRecyclerAdapter;
import com.flymr92gmail.sejonghangugeo.Adapters.SearchWordsAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.Fragments.LessonsDialogAddFragment;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.RecyclerItemClickListener;
import com.flymr92gmail.sejonghangugeo.Utils.Constants;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.SpeechActionListener;
import com.flymr92gmail.sejonghangugeo.Utils.WordsSpeech;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.john.waveview.WaveView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wnafee.vector.MorphButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class GramBookActivity extends AppCompatActivity implements NewWordsRecyclerAdapter.OnRecyclerViewItemClickListener, SpeechActionListener {
    private PDFView pdfView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private PrefManager prefManager;
    private FabSpeedDial bookMenu;
    private AppDataBase dataBase;
    private RecyclerView recyclerView;
    private int differencePages = 3;
    private Handler handlerPdf;
    private Handler handlerAudio;
    private RecyclerView navBookRv;
    private LinearLayoutManager llmanagerNav;
    private boolean navIsShow = false;
    private RecyclerItemClickListener navListener;
    private LinearLayout addAll;
    private LinearLayout addSelected;
    private RobotoTextView selectedCountTv;
    private ArrayList<Word> pageWords;
    private ArrayList<Word> selectedWords;
    private SearchView wordsSearcher;
    private boolean firstWordIsSelected = true;
    private RecyclerView searchRv;
    private ArrayList<Word> searchedArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gram_book);
        initialization();
        setupPdf();
        setupBookMenu();
        setupSlidingPanelButtonListener();
    }

    private void initialization(){
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelHeight(0);
        pdfView = findViewById(R.id.pdfViewPager);
        bookMenu = findViewById(R.id.book_menu);
        prefManager = new PrefManager(this);
        dataBase = new AppDataBase(this);
        recyclerView = findViewById(R.id.new_words_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        handlerPdf = new Handler();
        handlerAudio = new Handler();
        addAll = findViewById(R.id.sliding_add_all_btn);
        addSelected = findViewById(R.id.sliding_add_selected_btn);
        wordsSearcher = findViewById(R.id.search_words_sv);
        selectedCountTv = findViewById(R.id.selected_count_tv);
        pageWords = new ArrayList<>();
        selectedWords = new ArrayList<>();

    }

    private void setupPdf(){
        handlerPdf.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdfView.fromAsset("gramatika.pdf")
                                .defaultPage(prefManager.getLastGramPage())
                                .enableSwipe(true)
                                .swipeHorizontal(orientationIsHorizontal())
                                //.enableAnnotationRendering(true)
                                .enableAntialiasing(true)
                                .spacing(40)
                                .onPageScroll(new OnPageScrollListener() {
                                    @Override
                                    public void onPageScrolled(int page, float positionOffset) {
                                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                    }
                                })
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {
                                        prefManager.saveLastGramPage(page);
                                        try {
                                            clearNavBook();
                                        }catch (NullPointerException e){

                                        }
                                    }
                                })
                                .onLoad(new OnLoadCompleteListener() {
                                    @Override
                                    public void loadComplete(int nbPages) {
                                        // ObjectAnimator.ofFloat(llLoader, "alpha", 1f, 0).setDuration(500).start();
                                    }
                                })
                                .load();
                        pdfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clearNavBook();
                                closeSearcher();
                            }
                        });
                    }
                });
            }
        }, 0);

    }

    private boolean orientationIsHorizontal(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

    }

    private void setupBookMenu(){
        bookMenu.setMenuListener(new SimpleMenuListenerAdapter(){

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                updateMenu(navigationMenu);
                setupRecyclerView();

                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_new_words:
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        bookMenu.hide();
                        break;
                    case R.id.action_navigation:
                        navIsShow = true;
                        setupNavBook();
                        break;
                    case R.id.action_search:
                        setupSearcher();
                        break;
                }
                return false;
            }
        });

    }

    private void setupSlidingPanelButtonListener(){
        addAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LessonsDialogAddFragment lessonsDialogAddFragment = new LessonsDialogAddFragment();
                lessonsDialogAddFragment.setWords(pageWords);
                lessonsDialogAddFragment.show(getSupportFragmentManager(),"Choise Lesson");

            }
        });
        addSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LessonsDialogAddFragment lessonsDialogAddFragment = new LessonsDialogAddFragment();
                lessonsDialogAddFragment.setWords(selectedWords);
                lessonsDialogAddFragment.show(getSupportFragmentManager(),"Choise Lesson");
            }
        });
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) bookMenu.show();
            }
        });
    }

    private void setupRecyclerView(){
        String s = "выбранные(0)";
        selectedCountTv.setText(s);
        selectedWords.clear();
        pageWords.clear();
        pageWords = dataBase.getGramPageWords(pdfView.getCurrentPage()+differencePages);
        NewWordsRecyclerAdapter adapter = new NewWordsRecyclerAdapter(pageWords, getApplicationContext(), this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @SuppressLint("RestrictedApi")
    private void updateMenu(NavigationMenu menu){
        int page = pdfView.getCurrentPage()+differencePages;
        if (dataBase.getGramPageWords(page).size() > 0){
            MenuItem item = menu.findItem(R.id.action_new_words);
            item.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.action_new_words);
            item.setVisible(false);
        }

    }

    private void setupSearcher(){
        bookMenu.hide();
        wordsSearcher.setVisibility(View.VISIBLE);
        SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        final SearchView.OnQueryTextListener queryTextListener;
        searchRv = findViewById(R.id.search_words_rv);
        searchRv.setLayoutManager(new LinearLayoutManager(this));
        try{
            wordsSearcher.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }catch (NullPointerException n){

        }
        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(final String newText) {

                Constants.Language language = Constants.Language.Russian;
                searchedArray = dataBase.getSearchResult(newText, language);
                Collections.sort(searchedArray, new Comparator<Word>() {
                    @Override
                    public int compare(Word o1, Word o2) {
                        if (o1.getRussianWord().indexOf(newText)<0 || o2.getRussianWord().indexOf(newText)<0)
                            return o1.getRussianWord().toLowerCase().indexOf(newText.toLowerCase())-o2.getRussianWord().toLowerCase().indexOf(newText.toLowerCase());
                        return o1.getRussianWord().indexOf(newText)-o2.getRussianWord().indexOf(newText);
                    }
                });
                if (searchedArray.size()==0){
                    language = Constants.Language.Korean;
                    searchedArray = dataBase.getSearchResult(newText, language);
                    Collections.sort(searchedArray, new Comparator<Word>() {
                        @Override
                        public int compare(Word o1, Word o2) {
                            return o1.getKoreanWord().indexOf(newText)-o2.getKoreanWord().indexOf(newText);
                        }
                    });
                }
                if (newText.equals(""))
                    searchedArray = new ArrayList<>();

                searchRv.setAdapter(new SearchWordsAdapter(searchedArray, getApplicationContext(), newText, language, getSupportFragmentManager(), GramBookActivity.this));
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }
        };
        wordsSearcher.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "Поиск в словаре" + "</font>"));
        wordsSearcher.setOnQueryTextListener(queryTextListener);
        wordsSearcher.setIconified(false);
        wordsSearcher.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                closeSearcher();
                return false;
            }
        });
    }

    private boolean closeSearcher(){
        if (wordsSearcher.getVisibility() == View.VISIBLE){
            wordsSearcher.setQuery("", false);
            wordsSearcher.setVisibility(View.GONE);
            bookMenu.show();
            return true;
        }else return false;
    }

    @Override
    public void onAddClick(boolean[] selects, int position) {
        selectedWords.clear();
        int selected = 0;
        for (boolean b : selects){
            if (b){
                selected++;
                selectedWords.add(pageWords.get(position));
            }
        }
        if (1 == selected&&firstWordIsSelected){
            firstWordIsSelected = false;
            colorAnimator(addSelected, "backgroundColor", R.color.white, R.color.colorAccent, 500, true);
            colorAnimator(selectedCountTv, "textColor", R.color.lowBlue, R.color.white, 500, true);
        }else if (0 == selected){
            firstWordIsSelected = true;
            colorAnimator(addSelected, "backgroundColor", R.color.white, R.color.colorAccent, 500, false);
            colorAnimator(selectedCountTv, "textColor", R.color.lowBlue, R.color.white, 500, false);
        }

        String s = "выбранные(" + selected + ")";
        selectedCountTv.setText(s);
    }

    private void colorAnimator(View view, String propertyName, int firstColor, int secondColor, int duration, boolean isStart){
        if(isStart) ObjectAnimator.ofObject(view, propertyName, new ArgbEvaluator(), getResources().getColor(firstColor),
                getResources().getColor(secondColor)).setDuration(duration).start();
        else ObjectAnimator.ofObject(view, propertyName, new ArgbEvaluator(), getResources().getColor(secondColor),
                getResources().getColor(firstColor)).setDuration(duration).start();
    }

    @Override
    public void onBackPressed() {
        if (!closeSearcher()&&!clearNavBook()) {
            super.onBackPressed();
        }
    }

    private boolean clearNavBook(){
        if (navIsShow) {
            navIsShow = false;
            navBookRv.removeOnItemTouchListener(navListener);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.left_to_right_nav);
            navBookRv.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    bookMenu.show();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    navBookRv.getRecycledViewPool().clear();
                    navBookRv.setAdapter(null);
                    navBookRv.setLayoutManager(null);
                    navBookRv = null;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            return true;
        }else return false;
    }

    private void setupNavBook(){
        bookMenu.hide();
        navBookRv = findViewById(R.id.nav_book_rv);
        NavBookAdapter adapter = new NavBookAdapter(differencePages, pdfView.getCurrentPage(), pdfView.getPageCount(), this);
        llmanagerNav = new LinearLayoutManager(this);
        navBookRv.setLayoutManager(llmanagerNav);
        navBookRv.setAdapter(adapter);
        llmanagerNav.scrollToPositionWithOffset(pdfView.getCurrentPage()-5,0);
        navBookRv.setHasFixedSize(true);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.right_to_left_nav);
        navBookRv.startAnimation(anim);
        navListener = new RecyclerItemClickListener(this, navBookRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {
                pdfView.jumpTo(position, true);
            }
            @Override
            public void onLongItemClick(View view, final int position) {

            }
        });
        navBookRv.addOnItemTouchListener(navListener);
    }

    @Override
    public void onSpeechClick(int position, View view) {
        final WordsSpeech wordsSpeech = new WordsSpeech(this);
        String kor;
        if (wordsSearcher.getVisibility() == View.GONE)kor = pageWords.get(position).getKoreanWord();
        else kor = searchedArray.get(position).getKoreanWord();
        final ImageView imageView = view.findViewById(R.id.speech_iv);
        ObjectAnimator.ofObject(imageView, "colorFilter", new ArgbEvaluator(), getResources().getColor(R.color.black),
                getResources().getColor(R.color.yellow)).setDuration(100).start();
        wordsSpeech.speechWord(kor);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        while (pageWords.size() != 0){
                            if (!wordsSpeech.wordIsSpeech()){
                                ObjectAnimator.ofObject(imageView, "colorFilter", new ArgbEvaluator(), getResources().getColor(R.color.yellow),
                                        getResources().getColor(R.color.black)).setDuration(300).start();
                                return;
                            }
                        }
                    }
                });
                // mBottomNavigationTabStrip.setTabIndex(tabIndex);

            }
        }, 100);
    }
}
