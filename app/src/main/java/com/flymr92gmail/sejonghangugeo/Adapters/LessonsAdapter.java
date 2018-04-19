package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.content.DialogInterface;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;



import com.devspark.robototextview.widget.RobotoTextView;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.DataBases.User.UserDataBase;
import com.flymr92gmail.sejonghangugeo.ItemTouchHelperAdapter;

import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.POJO.Lesson;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;
import com.flymr92gmail.sejonghangugeo.Utils.ViewClickListener;
import com.flymr92gmail.sejonghangugeo.ViewHolder.HeaderViewHolder;


import java.util.ArrayList;
import java.util.Collections;

import eu.davidea.flipview.FlipView;


public class LessonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Lesson> mLessonArrayList;
    private UserDataBase dataBase;
    private Context mContext;
    private final int TYPE_HEADER = 0;
    private final int TYPE_LESSON = 1;
    private final int FROM_ACTIVITY= 0;
    private final int FROM_FRAGMENT = 1;
    private  int from;
    private Legend legend;
    private ViewClickListener viewClickListener;

    public LessonsAdapter(ArrayList<Lesson> mLessonArrayList, Context mContext, Legend legend, ViewClickListener viewClickListener) {
        this.mLessonArrayList = mLessonArrayList;
        this.mContext = mContext;
        this.legend = legend;
        this.viewClickListener = viewClickListener;
        dataBase = new UserDataBase(mContext);
        from = FROM_ACTIVITY;
    }

    public LessonsAdapter(ArrayList<Lesson> mLessonArrayList, Context mContext) {
        this.mLessonArrayList = mLessonArrayList;
        this.mContext = mContext;
        dataBase = new UserDataBase(mContext);
        from = FROM_FRAGMENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType==0){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_card_big, parent, false);
            return new HeaderViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_folder_item, parent, false);
        return new LessonViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
                case TYPE_HEADER:
                    HeaderViewHolder viewHolder = (HeaderViewHolder)holder;
                    String category = mContext.getResources().getString(R.string.category, legend.getLegendCategory());
                    viewHolder.legendCategory.setText(category);
                    String header = legend.getNameTranslate() + ". " + legend.getName();
                    viewHolder.legendName.setText(header);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        viewHolder.legendText.setText(Html.fromHtml(legend.getLegendText(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        viewHolder.legendText.setText(Html.fromHtml(legend.getLegendText()));
                    }
                    viewHolder.showAllBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewClickListener.onViewClicked();
                        }
                    });
                    break;
                case TYPE_LESSON:
                    LessonViewHolder lessonHolder = (LessonViewHolder)holder;
                    Lesson lesson = mLessonArrayList.get(position);
                    lesson.setPositionIndex(position);
                    dataBase.editLessonsPositionInArray(lesson);
                    lessonHolder.tvLessonName.setText(lesson.getLessonName());
                    lessonHolder.dateOfCreated.setText(lesson.getDateOfCreated());
                    break;
        }

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
        Lesson lesson = dataBase.getAllLessons().get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.delete_lesson, lesson.getLessonName()));
        builder.setPositiveButton(mContext.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                viewClickListener.deleteItem(position);
            }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

                notifyDataSetChanged();
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
       // mLessonArrayList.remove(position);
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {
        RobotoTextView tvLessonName, dateOfCreated;
         LessonViewHolder(View itemView) {
            super(itemView);
            tvLessonName = itemView.findViewById(R.id.lesson_name_tv);
            dateOfCreated = itemView.findViewById(R.id.date_of_created_lesson);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (from == FROM_ACTIVITY){
            switch (position) {
                case 0:
                    return TYPE_HEADER;
                default:
                    return TYPE_LESSON;
            }
        }else return TYPE_LESSON;
        }

}







