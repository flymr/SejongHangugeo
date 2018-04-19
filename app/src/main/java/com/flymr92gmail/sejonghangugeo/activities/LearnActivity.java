package com.flymr92gmail.sejonghangugeo.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.POJO.Word;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.Helper;

import java.util.ArrayList;
import java.util.Collections;


public class LearnActivity extends AppCompatActivity {
    private ArrayList<Word> words;
    private AppCompatTextView textViewWord;
    private TextView textViewProgress;
    private TextInputEditText editTextWord;
    private Word currentWord;
    private RecyclerView recyclerView;
    private LearningWordAdapter adapter;
    private  ArrayList<ComponentsOfWord> wordComponents;
    private TextInputLayout inputLayout;
    private ImageView firstCircle;
    private ImageView secondCircle;
    private int delayMillis = 0;
    private Toolbar toolbar;
    private Lesson lesson;
    private UserDataBase dataBase;
    private ProgressBar progressBar;
    private int currentWordCount =0;
    private  ArrayList<Word> learningWords;
    private int portionOfWords = 5;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        initialization();
        setupToolbar();
        getLearningWordsArray();
        if (currentWord.getCorrectCount()==1){
            firstCircle.setColorFilter(getResources().getColor(R.color.green));
        }
    }

    private void initialization(){
        dataBase = new UserDataBase(this);
        Intent intent = getIntent();
        learningWords = new ArrayList<>();
        lesson = (Lesson)intent.getSerializableExtra("lesson");
        words = Helper.randomizeArray(dataBase.getLearningWord(lesson));
        progressBar = findViewById(R.id.learn_progress);
        inputLayout = findViewById(R.id.learn_textInputLayout);
        inputLayout.setErrorTextAppearance(R.style.ErrorTextNormal);
        inputLayout.setError("Введите ответ");
        firstCircle = findViewById(R.id.firstCircle);
        secondCircle = findViewById(R.id.secondCircle);
        wordComponents = new ArrayList<>();
        textViewWord = findViewById(R.id.learn_tv);
        textViewProgress = findViewById(R.id.learn_progress_tv);
        recyclerView = findViewById(R.id.learn_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        toolbar = findViewById(R.id.toolbar_learn);
        editTextWord = findViewById(R.id.learn_et);
        editTextWord.requestFocus();
        editTextWord.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){

                checkWord(getCorrectAnswer(), editTextWord.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void setupToolbar(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle(lesson.getLessonName());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    onBackPressed();
                }
            });
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    private void publishProgress(int numb, int progress){
        String s = (numb+1)+"/"+portionOfWords;
        textViewProgress.setText(s);
        progressBar.setProgress(progress);
    }

    private float getProgress(int i){

        return 100f*(((float)currentWordCount+i)/(float)portionOfWords);
    }

    private void checkWord(String correctAnswer, String answer){
        if (correctAnswer.equals(answer)){
            UIcolorChanger(1);
            publishProgress(currentWordCount, (int) getProgress(1));
            currentWord.setCorrectCount(currentWord.getCorrectCount()+1);
            dataBase.editWordCorrectCount(lesson, currentWord);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nextWord();
                        }
                    });

                }
            }, 500);
        }else {
            if ((-1) < currentWord.getCorrectCount()){
                currentWord.setMissCount(currentWord.getMissCount()+1);
                currentWord.setCorrectCount(-1);
                dataBase.editWordMissCount(lesson,currentWord);
                dataBase.editWordCorrectCount(lesson, currentWord);
            }
            wordComponents.clear();
            for(int i = 0; i < answer.length(); i++){
                ComponentsOfWord componentsOfWord = new ComponentsOfWord();
                componentsOfWord.setComponent(answer.substring(i, i+1));
                wordComponents.add(componentsOfWord);
            }
            UIcolorChanger(2);
            getWordsFragment(answer, correctAnswer);
            editTextWord.setText("");
            adapter = new LearningWordAdapter(wordComponents);
            recyclerView.setAdapter(adapter);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            answerEditor(0);
                        }
                    });

                }
            }, 200);
        }
    }

    private void getLearningWordsArray(){
        learningWords.clear();
        portionOfWords = 5;
        for (Word word : words){
            if (word.getCorrectCount()< 2) learningWords.add(word);
        }
        if (learningWords.size() < portionOfWords) {
            portionOfWords = learningWords.size();
        }
        if (portionOfWords == 0){
            for (Word word : words){
                word.setCorrectCount(0);
                word.setMissCount(0);
                dataBase.editWordCorrectCount(lesson, word);
                dataBase.editWordMissCount(lesson, word);
            }
            learningWords.addAll(words);
            portionOfWords = 5;
            if (learningWords.size() < portionOfWords){
                portionOfWords = learningWords.size();
            }
        }
        if (portionOfWords < learningWords.size()){
            while (portionOfWords < learningWords.size())
                learningWords.remove(portionOfWords);
        }


        currentWord = learningWords.get(currentWordCount);
        textViewWord.setText("");
        textViewWord.setText(getQuestionText());
        editTextWord.setText("");
        publishProgress(currentWordCount, (int)getProgress(0));
        String s = (currentWordCount+1)+"/"+portionOfWords;
        textViewProgress.setText(s);
    }


    private String getCorrectAnswer(){
        if(lesson.getCurrentLanguage() !=0) return currentWord.getRussianWord();
        else return currentWord.getKoreanWord();

    }

    private String getQuestionText(){
        if (lesson.getCurrentLanguage() !=0) return currentWord.getKoreanWord();
        else return currentWord.getRussianWord();
    }

    private void nextWord(){
        currentWordCount++;
        if (currentWordCount < portionOfWords&& currentWordCount < learningWords.size()){
            currentWord = null;
            currentWord = learningWords.get(currentWordCount);
            UIcolorChanger(0);
            textViewWord.setText("");
            editTextWord.setText("");
            textViewWord.setText(getQuestionText());
            publishProgress(currentWordCount, (int)getProgress(0));

        }else {
            if (!allWordIsLearned()){
             Intent intent = new Intent(this, LearnResult.class);
             intent.putExtra("learningWords", learningWords);
             intent.putExtra("lesson", lesson);
             startActivity(intent);
             finish();
            }else {
             Intent intent = new Intent(this, LearnFinalResult.class);
             intent.putExtra("lesson", lesson);
             startActivity(intent);
             finish();
            }
        }
    }

    private boolean allWordIsLearned() {
        boolean allIsLearned = true;
        for (Word word : words){
            if (word.getCorrectCount()<2) {
                allIsLearned = false;
                break;
            }
        }
        return allIsLearned;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_learn,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionChangeLanguage:
                if (lesson.getCurrentLanguage()==0){
                    lesson.setCurrentLanguage(1);
                    dataBase.editCurrentLanguage(lesson);
                }
                else {
                    lesson.setCurrentLanguage(0);
                    dataBase.editCurrentLanguage(lesson);
                }
                restartLearn();
                break;
            case R.id.actionRestart:
                restartLearn();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartLearn(){
        if (currentWord.getCorrectCount()==1){
            colorAnimator(firstCircle, "colorFilter", R.color.green, R.color.grayM, 500);
        }
        for (Word word : words){
            word.setMissCount(0);
            word.setCorrectCount(0);
            dataBase.editWordMissCount(lesson, word);
            dataBase.editWordCorrectCount(lesson, word);
        }
        Collections.shuffle(words);
        currentWordCount = 0;
        getLearningWordsArray();
    }


    void UIcolorChanger(int var){
        switch (var){
            case 0:
                inputLayout.setErrorTextAppearance(R.style.ErrorTextNormal);
                inputLayout.setError("Введите ответ");
                editTextWord.requestFocus();
                if (1 == currentWord.getCorrectCount()&&2 == learningWords.get(currentWordCount-1).getCorrectCount()){
                    colorAnimator(secondCircle, "colorFilter", R.color.green, R.color.grayM, 500);
                }else if (2 == learningWords.get(currentWordCount-1).getCorrectCount()){
                    colorAnimator(firstCircle, "colorFilter", R.color.green, R.color.grayM, 500);
                    colorAnimator(secondCircle, "colorFilter", R.color.green, R.color.grayM, 500);
                }else if (1 == currentWord.getCorrectCount() && 2 != currentWord.getCorrectCount()-1){
                    //colorAnimator(firstCircle, "colorFilter", R.color.green, R.color.grayM, 500);
                }else if (0 == currentWord.getCorrectCount() && 2 != currentWord.getCorrectCount()-1){
                    colorAnimator(firstCircle, "colorFilter", R.color.green, R.color.grayM, 500);

                }
                colorAnimator(toolbar, "backgroundColor", R.color.green, R.color.colorAccent, 500);

                break;
            case 1:
                inputLayout.setErrorTextAppearance(R.style.ErrorTextCorrect);
                inputLayout.setError("Верно");
                textViewWord.requestFocus();
                colorAnimator(toolbar, "backgroundColor", R.color.colorAccent, R.color.green, 500);
                if (0 < currentWord.getCorrectCount()){
                    colorAnimator(secondCircle, "colorFilter", R.color.grayM, R.color.green, 500);
                }else {
                    colorAnimator(firstCircle, "colorFilter", R.color.grayM, R.color.green, 500);
                }
                break;
            case 2:
                textViewWord.requestFocus();
                inputLayout.setErrorTextAppearance(R.style.ErrorTextIncorrect);
                inputLayout.setError("Неверно");
                colorAnimator(toolbar, "backgroundColor", R.color.colorAccent, R.color.redM, 500);
                if (0 < currentWord.getCorrectCount()){
                    colorAnimator(firstCircle, "colorFilter", R.color.green, R.color.redM, 500);
                }else {
                    colorAnimator(firstCircle, "colorFilter", R.color.grayM, R.color.redM, 500);
                }

                break;
            case 3:
                inputLayout.setErrorTextAppearance(R.style.ErrorTextNormal);
                inputLayout.setError("Попробуйте еще раз");
                int position = wordComponents.size()-1;
                while (0<=position){
                    adapter.notifyItemRemoved(position);
                    position--;
                }
                wordComponents.clear();
                editTextWord.requestFocus();
                colorAnimator(toolbar, "backgroundColor", R.color.redM, R.color.colorAccent, 500);
                colorAnimator(firstCircle, "colorFilter", R.color.redM, R.color.grayM, 500);
                break;
        }
    }

    private void colorAnimator(View view, String propertyName, int firstColor, int secondColor, int duration){
        ObjectAnimator.ofObject(view, propertyName, new ArgbEvaluator(), getResources().getColor(firstColor),
                getResources().getColor(secondColor)).setDuration(duration).start();
    }


    //мозгов хватило только на такое вот безобразие :(
    private void getWordsFragment(String userAnswer, String correctAnswer){
        int corAnsSize = correctAnswer.length();
        for (int i = 0; i < corAnsSize; i++){
            for (int b = 0; b <= i; b++){
                int cutFromEnd = corAnsSize - (i - b);
                String string = correctAnswer.substring(b, cutFromEnd);
                if (userAnswer.contains(string)){
                    int fromIndex = userAnswer.indexOf(string);
                    int toIndex = fromIndex + string.length();
                    int fromIndexCor = correctAnswer.indexOf(string);
                    int toIndexCor = fromIndexCor + string.length();
                    int a = fromIndex;
                    while (a < toIndex){
                        wordComponents.get(a).setType(0);
                        a++;
                    }
                    String leftComp = userAnswer.substring(0, fromIndex);
                    String corLeftComp = correctAnswer.substring(0, fromIndexCor);
                    String rightComp = userAnswer.substring(toIndex, userAnswer.length());
                    String corRightComp = correctAnswer.substring(toIndexCor, correctAnswer.length());
                    if (leftComp.length()!=0&&corLeftComp.length()!=0) {
                        getLeftComp(leftComp, corLeftComp, 0);

                    }
                    if (rightComp.length()!=0&&corRightComp.length()!=0)getLeftComp(rightComp, corRightComp, toIndex);

                    return;
                }
            }

        }

    }

    public void  getRightComp(String userAnswer , String correctAnswer, int index){

        int corAnsSize = correctAnswer.length();
        int userAnsSize = userAnswer.length();
        int indexOf;
        for (int i = 0; i < corAnsSize; i++){
            indexOf = 0;
            for (int b = 0; b <= i; b++){
                int cutFromEnd = corAnsSize - (i - b);
                String string = correctAnswer.substring(b, cutFromEnd);
                String stringUse = userAnswer.substring(indexOf, userAnsSize);
                if (stringUse.contains(string)){
                    int fromIndex = userAnswer.indexOf(string);
                    int toIndex = fromIndex + string.length();
                    int fromIndexCor = correctAnswer.indexOf(string);
                    int toIndexCor = fromIndexCor + string.length();
                    indexOf = toIndex;
                    int a = fromIndex;
                    while (a < toIndex){
                        wordComponents.get(a+index).setType(0);
                        a++;
                    }
                    String leftComp = userAnswer.substring(0, fromIndex);
                    String corLeftComp = correctAnswer.substring(0, fromIndexCor);
                    String rightComp = userAnswer.substring(toIndex, userAnswer.length());
                    String corRightComp = correctAnswer.substring(toIndexCor, correctAnswer.length());
                    if (leftComp.length()!=0&&corLeftComp.length()!=0)getLeftComp(leftComp, corLeftComp, index);
                    if (rightComp.length()!=0&&corRightComp.length()!=0) getRightComp(rightComp, corRightComp,
                            toIndex + index);
                    else return;
                }
            }

        }

    }

    void getLeftComp(String userAnswer, String correctAnswer, int index){
        int corAnsSize = correctAnswer.length();
        int userAnsSize = userAnswer.length();
        int indexOf;
        for (int i = 0; i < corAnsSize; i++){
            indexOf = 0;
            for (int b = 0; b <= i; b++){
                int cutFromEnd = corAnsSize - (i - b);
                String string = correctAnswer.substring(b, cutFromEnd);
                String stringUse = userAnswer.substring(indexOf, userAnsSize);
                if (stringUse.contains(string)){
                    int fromIndex = userAnswer.indexOf(string);
                    int toIndex = fromIndex + string.length();
                    int fromIndexCor = correctAnswer.indexOf(string);
                    int toIndexCor = fromIndexCor + string.length();
                    indexOf = toIndex;
                    int a = fromIndex;
                    while (a < toIndex){
                        wordComponents.get(a+index).setType(0);
                        a++;
                    }
                    String leftComp = userAnswer.substring(0, fromIndex);
                    String corLeftComp = correctAnswer.substring(0, fromIndexCor);
                    String rightComp = userAnswer.substring(toIndex, userAnswer.length());
                    String corRightComp = correctAnswer.substring(toIndexCor, correctAnswer.length());

                    if (rightComp.length()!=0&&corRightComp.length()!=0) getRightComp(rightComp, corRightComp,
                            toIndex+index);

                    if (leftComp.length()!=0&&corLeftComp.length()!=0)getLeftComp(leftComp, corLeftComp, index);
                    else return;
                }
            }

        }
    }


    void answerEditor(final int step){
        if (getCorrectAnswer().length() <= step) {
            while (step < wordComponents.size()){
                wordComponents.remove(step);
                adapter.notifyItemRemoved(step);

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIcolorChanger(3);
                        }
                    });

                }
            }, 1000);
            return;
        }
        delayMillis = 0;
        try{
            if (wordComponents.get(step).getType()==2){
                delayMillis = 500;
                for (int a = 0; (step+a) < wordComponents.size(); a++){
                    ComponentsOfWord componentsOfWord1 = wordComponents.get(step+a);
                    if (componentsOfWord1.getType()==2){
                        wordComponents.remove(step+a);
                        adapter.notifyItemRemoved(step+a);
                        a--;
                    }else break;
                }
            }else delayMillis = 0;
        }catch (IndexOutOfBoundsException e){
            delayMillis = 0;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ComponentsOfWord componentsOfWord = new ComponentsOfWord();
                        try {
                            if (!wordComponents.get(step).getComponent().equals(getCorrectAnswer()
                                    .substring(step, step+1))){
                                componentsOfWord.setComponent(getCorrectAnswer().substring(step,step+1));
                                componentsOfWord.setType(1);
                                wordComponents.add(step, componentsOfWord);
                                adapter.notifyItemInserted(step);
                                adapter.notifyItemChanged(step);
                            }
                        }catch (IndexOutOfBoundsException e){
                            componentsOfWord.setComponent(getCorrectAnswer().substring(step,step+1));
                            componentsOfWord.setType(1);
                            wordComponents.add(step, componentsOfWord);
                            adapter.notifyItemInserted(step);
                            adapter.notifyItemChanged(step);
                        }


                    }
                });
                recyclerView.getItemAnimator().isRunning(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                    @Override
                    public void onAnimationsFinished() {
                        answerEditor(step+1);
                    }

                });


            }
        }, delayMillis);
    }


    class LearningWordAdapter extends RecyclerView.Adapter<LearningWordAdapter.ViewHolder>{
        private ArrayList<ComponentsOfWord> componentArray;


        LearningWordAdapter(ArrayList<ComponentsOfWord> componentArray) {
            this.componentArray = componentArray;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learn_answer_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            ComponentsOfWord componentsOfWord = componentArray.get(position);
            holder.textView.setText(componentsOfWord.getComponent());
            if (componentsOfWord.getType()==2) holder.ll.setBackgroundColor(getResources().getColor(R.color.redM));
            if (componentsOfWord.getType()==1) holder.ll.setBackgroundColor(getResources().getColor(R.color.green));
        }

        @Override
        public int getItemCount() {
            return componentArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            LinearLayout ll;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.learn_answer_tv);
                ll = itemView.findViewById(R.id.learn_ll);

            }
        }


    }

    public class ComponentsOfWord{
        String component = "abc";
        int type = 2;
        String getComponent(){
            return component;
        }
        void setComponent(String component){
            this.component = component;
        }
        Integer getType(){
            return type;
        }
        void setType(int type){
            this.type = type;
        }
    }

}
