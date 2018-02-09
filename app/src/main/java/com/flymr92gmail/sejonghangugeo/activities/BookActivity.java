package com.flymr92gmail.sejonghangugeo.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wnafee.vector.MorphButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class BookActivity extends AppCompatActivity implements NewWordsRecyclerAdapter.OnRecyclerViewItemClickListener{
    private PDFView pdfView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private PrefManager prefManager;
    private FabSpeedDial bookMenu;
    private AppDataBase dataBase;
    private RecyclerView recyclerView;
    private int differencePages = 2;
    private Handler handlerPdf;
    private MediaPlayer mp;
    private Handler handlerAudio;
    private SeekBar audioSeekBar;
    private TextView tvAudioStart, tvAudioEnd;
    private Runnable audioRun;
    private MorphButton playButton;
    private LinearLayout controller_ll;
    private ImageButton closeAudioBtn;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        initialization();
        setupPdf();
        setupBookMenu();
        setupAudioPlayer();
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
        mp = new MediaPlayer();
        audioSeekBar = findViewById(R.id.book_seek_bar);
        tvAudioStart = findViewById(R.id.tv_audio_start);
        tvAudioEnd = findViewById(R.id.tv_audio_end);
        playButton = findViewById(R.id.book_play_audio);
        controller_ll = findViewById(R.id.book_audio_control_ll);
        closeAudioBtn = findViewById(R.id.close_audio_btn);
        addAll = findViewById(R.id.sliding_add_all_btn);
        addSelected = findViewById(R.id.sliding_add_selected_btn);
        wordsSearcher = findViewById(R.id.search_words_sv);
        selectedCountTv = findViewById(R.id.selected_count_tv);
        pageWords = new ArrayList<>();
        selectedWords = new ArrayList<>();
    }

    private void setupRecyclerView(){
        String s = "выбранные(0)";
        selectedCountTv.setText(s);
        selectedWords.clear();
        pageWords.clear();
        pageWords = dataBase.getPageWords(pdfView.getCurrentPage()+differencePages);
        NewWordsRecyclerAdapter adapter = new NewWordsRecyclerAdapter(pageWords, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
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
    }

    @Override
    public void onBackPressed() {
        if (!closeSearcher()&&!clearNavBook()&&!stopAudio()) {
            super.onBackPressed();
        }
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
            public void onItemClick(View view, int position) {
                pdfView.jumpTo(position, true);
            }
            @Override
            public void onLongItemClick(View view, final int position) {

            }
        });
        navBookRv.addOnItemTouchListener(navListener);
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

    private void setupSearcher(){
        wordsSearcher.setVisibility(View.VISIBLE);
        SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        final SearchView.OnQueryTextListener queryTextListener;
        final RecyclerView searchRv = findViewById(R.id.search_words_rv);
        searchRv.setLayoutManager(new LinearLayoutManager(this));
        try{
            wordsSearcher.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }catch (NullPointerException n){

        }
        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(final String newText) {
                ArrayList<Word> wordArrayList;
                Constants.Language language = Constants.Language.Russian;
                wordArrayList = dataBase.getSearchResult(newText, language);
                Collections.sort(wordArrayList, new Comparator<Word>() {
                    @Override
                    public int compare(Word o1, Word o2) {
                        if (o1.getRussianWord().indexOf(newText)<0 || o2.getRussianWord().indexOf(newText)<0)
                            return o1.getRussianWord().toLowerCase().indexOf(newText.toLowerCase())-o2.getRussianWord().toLowerCase().indexOf(newText.toLowerCase());
                            return o1.getRussianWord().indexOf(newText)-o2.getRussianWord().indexOf(newText);
                    }
                });
                if (wordArrayList.size()==0){
                    language = Constants.Language.Korean;
                    wordArrayList = dataBase.getSearchResult(newText, language);
                    Collections.sort(wordArrayList, new Comparator<Word>() {
                        @Override
                        public int compare(Word o1, Word o2) {
                            return o1.getKoreanWord().indexOf(newText)-o2.getKoreanWord().indexOf(newText);
                        }
                    });
                }
                if (newText.equals(""))
                    wordArrayList = new ArrayList<>();

                searchRv.setAdapter(new SearchWordsAdapter(wordArrayList, getApplicationContext(), newText, language));
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }
        };
        wordsSearcher.setOnQueryTextListener(queryTextListener);
        wordsSearcher.setIconified(false);
        wordsSearcher.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (wordsSearcher.getQuery().equals("")) closeSearcher();
                return false;
            }
        });
    }

    private boolean closeSearcher(){
        if (wordsSearcher.getVisibility() == View.VISIBLE){
            wordsSearcher.setQuery("", false);
            wordsSearcher.setVisibility(View.GONE);
            return true;
        }else return false;
    }

    private boolean orientationIsHorizontal(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

    }

    private void setupPdf(){
        handlerPdf.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdfView.fromAsset("Book.pdf")
                                .defaultPage(prefManager.getLastBookPage())
                                .enableSwipe(true)
                                .swipeHorizontal(orientationIsHorizontal())
                                //.enableAnnotationRendering(true)
                                .enableAntialiasing(true)
                                .spacing(20)
                                .onLoad(new OnLoadCompleteListener() {
                                    @Override
                                    public void loadComplete(int nbPages) {

                                    }
                                })
                                .onPageScroll(new OnPageScrollListener() {
                                    @Override
                                    public void onPageScrolled(int page, float positionOffset) {

                                    }

                                })
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {
                                        stopAudio();
                                        prefManager.saveLastBookPage(page);
                                       try {
                                           clearNavBook();
                                        }catch (NullPointerException e){

                                       }
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
                      break;
                  case R.id.action_play_audio:
                      playAudio();
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


    @SuppressLint("RestrictedApi")
    private void updateMenu(NavigationMenu menu){
        int page = pdfView.getCurrentPage()+differencePages;
        if (dataBase.getPageWords(page).size() > 0){
            MenuItem item = menu.findItem(R.id.action_new_words);
            item.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.action_new_words);
            item.setVisible(false);
        }
        if (dataBase.getPageAudio(page) != null){
            MenuItem item = menu.findItem(R.id.action_play_audio);
            item.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.action_play_audio);
            item.setVisible(false);
        }
        if (dataBase.getTest(page).size()>0){
            MenuItem item = menu.findItem(R.id.action_test);
            item.setVisible(true);
        }else {
            MenuItem item = menu.findItem(R.id.action_test);
            item.setVisible(false);
        }
    }

    private void setupAudioPlayer(){
      audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
              if(mp!=null && b){
                  mp.seekTo(i*10);
              }
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {

          }
      });
      playButton.setOnStateChangedListener(new MorphButton.OnStateChangedListener() {
          @Override
          public void onStateChanged(MorphButton.MorphState changedTo, boolean isAnimating) {
              if (isAnimating) {
                  switch (changedTo) {
                      case START:
                          if (mp != null) mp.start();
                          break;
                      case END:
                          if (mp != null) mp.pause();
                          break;

                  }
              }
          }
      });
      closeAudioBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              stopAudio();
          }
      });
    }

    private void playAudio(){
        bookMenu.hide();
        try {
            controller_ll.setVisibility(View.VISIBLE);
            mp = new MediaPlayer();
            AssetFileDescriptor descriptor = getAssets().openFd("Audio/"+dataBase.getPageAudio(pdfView.getCurrentPage()+2).getmTrackId()+".wma");
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            descriptor.close();
            mp.prepare();
            mp.setVolume(1f, 1f);
            mp.setLooping(true);
            mp.start();
            getAudioStats();
            initializeSeekBar();
        } catch (Exception e) {

        }
    }

    protected boolean stopAudio(){
        if (controller_ll.getVisibility() == View.VISIBLE){
            if(mp!=null){
                mp.stop();
                mp.release();
                mp = null;
                if(handlerAudio!=null){
                    handlerAudio.removeCallbacks(audioRun);
                }
            }
            audioSeekBar.setProgress(0);
            controller_ll.setVisibility(View.GONE);
            if (playButton.getState() == MorphButton.MorphState.END) playButton.setState(MorphButton.MorphState.START);
            bookMenu.show();
            return true;
        }else return false;

    }



    private void getAudioStats(){
        int duration  = mp.getDuration()/1000; // In milliseconds
        int due = (mp.getDuration() - mp.getCurrentPosition())/1000;
        int pass = duration - due;

        tvAudioStart.setText(getDuration(pass));
        tvAudioEnd.setText(getDuration(due));
    }

    private String getDuration(int duration){
        int seconds;
        int minutes = 0;
        if (60 < duration){
            for (int i =60; i < duration; i = i +60){
                minutes++;
            }
            seconds = duration - (minutes*60);
        }else {
           seconds = duration;
        }
        if (seconds < 10) return minutes + ":0" + seconds;
        return minutes + ":" + seconds;
    }

    private void initializeSeekBar(){
        audioSeekBar.setMax(mp.getDuration()/10);

        audioRun = new Runnable() {
            @Override
            public void run() {
                if(mp!=null){
                    int mCurrentPosition = mp.getCurrentPosition()/10; // In milliseconds
                    audioSeekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                handlerAudio.postDelayed(audioRun,10);
            }
        };
        handlerAudio.postDelayed(audioRun,10);
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
        if (1 == selected){
                colorAnimator(addSelected, "backgroundColor", R.color.white, R.color.colorAccent, 500, true);
                colorAnimator(selectedCountTv, "textColor", R.color.lowBlue, R.color.white, 500, true);
        }else if (0 == selected){
            colorAnimator(addSelected, "backgroundColor", R.color.white, R.color.colorAccent, 500, false);
            colorAnimator(selectedCountTv, "textColor", R.color.lowBlue, R.color.white, 500, false);
        }

        String s = "выбранные(" + selected + ")";
        selectedCountTv.setText(s);

    }

    private boolean wordIsAlreadyDone(Word checkingWord, ArrayList<Word> words){
        String rusWord = checkingWord.getRussianWord();
        String korWord = checkingWord.getKoreanWord();
        for (Word word:words){
            if (rusWord.equals(word.getRussianWord())
                    &&korWord.equals(word.getKoreanWord())) return true;
        }
        return false;
    }

    private void colorAnimator(View view, String propertyName, int firstColor, int secondColor, int duration, boolean isStart){
       if(isStart) ObjectAnimator.ofObject(view, propertyName, new ArgbEvaluator(), getResources().getColor(firstColor),
                getResources().getColor(secondColor)).setDuration(duration).start();
       else ObjectAnimator.ofObject(view, propertyName, new ArgbEvaluator(), getResources().getColor(secondColor),
               getResources().getColor(firstColor)).setDuration(duration).start();
    }

}
