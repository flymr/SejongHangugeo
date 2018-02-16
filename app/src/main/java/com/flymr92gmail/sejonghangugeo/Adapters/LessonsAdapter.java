package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.devspark.robototextview.widget.RobotoTextView;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.ItemTouchHelperAdapter;
import com.flymr92gmail.sejonghangugeo.LessonsCreateFolder;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;


public class LessonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Lesson> mLessonArrayList;
    private UserDataBase dataBase;
    private Context mContext;
    private final int TYPE_HEADER = 0;
    private final int TYPE_CELL = 1;

    public LessonsAdapter(ArrayList<Lesson> mLessonArrayList, Context mContext) {
        this.mLessonArrayList = mLessonArrayList;
        this.mContext = mContext;
        dataBase = new UserDataBase(mContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_folder_item,parent,false);
       // return new ViewHolder(view);
        View view = null;
        if (viewType==0){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_card_big, parent, false);
            HeaderViewHolder vh = new HeaderViewHolder(view);
            return vh;
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_folder_item, parent, false);
        LessonViewHolder vh = new LessonViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
                case TYPE_HEADER:
                    HeaderViewHolder viewHolder = (HeaderViewHolder)holder;
                    String header = viewHolder.legend.getNameTranslate() + ". " + viewHolder.legend.getName();
                    viewHolder.legendName.setText(header);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        viewHolder.legendText.setText(Html.fromHtml(viewHolder.legend.getLegendText(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        viewHolder.legendText.setText(Html.fromHtml(viewHolder.legend.getLegendText()));
                    }
                    //viewHolder.legendText.setText(setIndentToText(viewHolder.legend.getLegendText()));
                    break;
                case TYPE_CELL:
                    LessonViewHolder lessonHolder = (LessonViewHolder)holder;
                    Lesson lesson = mLessonArrayList.get(position);
                    lesson.setPositionIndex(position);
                    dataBase.editLessonsPositionInArray(lesson);
                    lessonHolder.tvLessonName.setText(lesson.getLessonName());
                    lessonHolder.dateOfCreated.setText(lesson.getDateOfCreated());
                    break;
        }

    }

    private String setIndentToText(String text){
        String s = text.replaceAll("p", "    ");
        return s.replaceAll("/p", "\n\n");
    }

    @Override
    public int getItemCount() {
        return mLessonArrayList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition+1; i <= toPosition; i++){
                Lesson lesson = mLessonArrayList.get(i);
                lesson.setPositionIndex(i-1);
                dataBase.editLessonsPositionInArray(lesson);
                Lesson lessonMove = mLessonArrayList.get(fromPosition);
                lessonMove.setPositionIndex(fromPosition+1);
                dataBase.editLessonsPositionInArray(lessonMove);
            }
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mLessonArrayList, i, i + 1);
            }

        } else {
            for (int i = fromPosition-1; i >= toPosition; i--){
                Lesson lesson = mLessonArrayList.get(i);
                lesson.setPositionIndex(i+1);
                dataBase.editLessonsPositionInArray(lesson);
                Lesson lessonMove = mLessonArrayList.get(fromPosition);
                lessonMove.setPositionIndex(fromPosition-1);
                dataBase.editLessonsPositionInArray(lessonMove);
            }
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mLessonArrayList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }



    @Override
    public void onItemDismiss(final int position) {

        final Lesson lesson = dataBase.getAllLessons().get(position);
        String buttonOk = "Удалить";
        String buttonCancel ="Отмена";
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Удалить " + mLessonArrayList.get(position).getLessonName()+"?");
        builder.setPositiveButton(buttonOk, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dataBase.deleteLesson(lesson);
                notifyItemRemoved(position);
            }
        });
        builder.setNegativeButton(buttonCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

                mLessonArrayList.add(position, dataBase.getAllLessons().get(position));
                mLessonArrayList = dataBase.getAllLessons();
                notifyDataSetChanged();
            }
        });
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
        mLessonArrayList.remove(position);
    }




    public class LessonViewHolder extends RecyclerView.ViewHolder {
        RobotoTextView tvLessonName, dateOfCreated;

        public LessonViewHolder(View itemView) {
            super(itemView);
            tvLessonName = itemView.findViewById(R.id.lesson_name_tv);
            dateOfCreated = itemView.findViewById(R.id.date_of_created_lesson);
        }

        }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView legendName;
        TextView legendText;
        ImageView iv_add;
        AppDataBase appDataBase;
        Legend legend;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            legendName = itemView.findViewById(R.id.legend_header);
            legendText = itemView.findViewById(R.id.legend_text);
            iv_add = itemView.findViewById(R.id.iv_add_legend);
            appDataBase = new AppDataBase(mContext);
            legend = appDataBase.getLegends().get(2);
        }

    }



    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    }







