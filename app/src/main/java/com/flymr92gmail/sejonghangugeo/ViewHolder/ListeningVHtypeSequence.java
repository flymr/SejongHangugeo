package com.flymr92gmail.sejonghangugeo.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flymr92gmail.sejonghangugeo.R;

public class ListeningVHtypeSequence extends RecyclerView.ViewHolder{
    public CardView cardView;
    public ImageView frontImage;
    public ImageView ivVar1, ivVar2, ivVar3;
    public LinearLayout llVariants;
    public ListeningVHtypeSequence(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_front);
        ivVar1 = itemView.findViewById(R.id.var1);
        ivVar2 = itemView.findViewById(R.id.var2);
        ivVar3 = itemView.findViewById(R.id.var3);
        llVariants = itemView.findViewById(R.id.ll_variants);
        frontImage = itemView.findViewById(R.id.front_iv);


    }



}
