package com.flymr92gmail.sejonghangugeo.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.R;

/**
 * Created by hp on 3/27/2018.
 */

public class ListeningVHtypeSequence extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CardView cardView;
    public ImageView frontImage;
    public ImageView ivVar1, ivVar2, ivVar3;
    public boolean hasChoose = false;
    public LinearLayout llVariants;
    public int var;
    public ListeningVHtypeSequence(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_front);
        ivVar1 = itemView.findViewById(R.id.var1);
        ivVar2 = itemView.findViewById(R.id.var2);
        ivVar3 = itemView.findViewById(R.id.var3);
        llVariants = itemView.findViewById(R.id.ll_variants);
        frontImage = itemView.findViewById(R.id.front_iv);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_front:
                if (hasChoose){
                    hasChoose = false;
                    showVar();
                    llVariants.setVisibility(View.GONE);
                }else {
                    llVariants.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.var1:
                ivVar2.setVisibility(View.GONE);
                ivVar3.setVisibility(View.GONE);
                hasChoose = true;
                break;
            case R.id.var2:
                ivVar1.setVisibility(View.GONE);
                ivVar3.setVisibility(View.GONE);
                hasChoose = true;
                break;
            case R.id.var3:
                ivVar1.setVisibility(View.GONE);
                ivVar2.setVisibility(View.GONE);
                hasChoose = true;
                break;
        }
    }

    private void showVar(){
        ivVar1.setVisibility(View.VISIBLE);
        ivVar2.setVisibility(View.VISIBLE);
        ivVar3.setVisibility(View.VISIBLE);
    }


}
