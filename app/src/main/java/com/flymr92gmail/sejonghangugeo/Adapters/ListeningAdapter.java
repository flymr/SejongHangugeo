package com.flymr92gmail.sejonghangugeo.Adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flymr92gmail.sejonghangugeo.POJO.AudioTest;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.ViewHolder.ListeningViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 24.03.2018.
 */

public class ListeningAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int correctAnswer;
    private ArrayList<Drawable> images;

    public ListeningAdapter(int correctAnswer, ArrayList<Drawable> images) {
        this.correctAnswer = correctAnswer;
        this.images = images;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listenning, parent, false);
        return new ListeningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListeningViewHolder viewHolder = (ListeningViewHolder)holder;
        viewHolder.frontImage.setImageDrawable(images.get(position));
        if (correctAnswer == position+1)
            viewHolder.rearCorrect.setVisibility(View.VISIBLE);
        else viewHolder.rearIncorrect.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {return images.size();}


}
