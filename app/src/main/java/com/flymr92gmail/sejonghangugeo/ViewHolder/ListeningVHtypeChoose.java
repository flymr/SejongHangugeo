package com.flymr92gmail.sejonghangugeo.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.flymr92gmail.sejonghangugeo.R;


public class ListeningVHtypeChoose extends RecyclerView.ViewHolder {
    public CardView cardView;
    public ImageView ivChoose;
    public ImageView frontImage;
    public ListeningVHtypeChoose(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_front);
        ivChoose = itemView.findViewById(R.id.iv_choose);
        frontImage = itemView.findViewById(R.id.front_iv);
    }
}
