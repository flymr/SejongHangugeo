package com.flymr92gmail.sejonghangugeo.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.flymr92gmail.sejonghangugeo.R;

public class ListeningViewHolder extends RecyclerView.ViewHolder {
    public ImageView frontImage, rearCorrect, rearIncorrect;
    public ListeningViewHolder(View itemView) {
        super(itemView);
        frontImage = itemView.findViewById(R.id.front_iv);
        rearCorrect = itemView.findViewById(R.id.rear_correct_iv);
        rearIncorrect = itemView.findViewById(R.id.rear_incorrect_iv);
    }
}
