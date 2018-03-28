package com.flymr92gmail.sejonghangugeo.Adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.ViewHolder.ListeningVHtypeChoose;
import com.flymr92gmail.sejonghangugeo.ViewHolder.ListeningVHtypeSequence;
import com.flymr92gmail.sejonghangugeo.ViewHolder.ListeningViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 24.03.2018.
 */

public class ListeningAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int correctAnswer;
    private ArrayList<Drawable> images;
    private boolean[] selects;
    private int[] sequence;
    private int TEST_FORMAT = 0;
    private final int TEST_STANDARD = 0;
    private final int TEST_CHOOSE = 1;
    private final int TEST_SEQUENCE = 2;
    private final int TEST_TEXT = 3;

    public boolean[] getSelects() {
        return selects;
    }

    public int[] getSequence() {
        return sequence;
    }

    public ListeningAdapter(int correctAnswer, ArrayList<Drawable> images, int TEST_FORMAT) {
        this.correctAnswer = correctAnswer;
        this.images = images;
        this.TEST_FORMAT = TEST_FORMAT;
        if (TEST_FORMAT == TEST_CHOOSE)selects = new boolean[images.size()];
        if (TEST_FORMAT == TEST_SEQUENCE)sequence = new int[3];
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (TEST_FORMAT){
            case TEST_STANDARD:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listenning, parent, false);
                return new ListeningViewHolder(view);
            case TEST_CHOOSE:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listening_sequence, parent, false);
                return new ListeningVHtypeChoose(view1);
            case TEST_SEQUENCE:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listening_sequence, parent, false);
                return new ListeningVHtypeSequence(view2);
            case TEST_TEXT:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listening_sequence, parent, false);
                return new ListeningVHtypeChoose(view3);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listenning, parent, false);
        return new ListeningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (TEST_FORMAT){
            case TEST_STANDARD:
                ListeningViewHolder holderStandard = (ListeningViewHolder)holder;
                holderStandard.frontImage.setImageDrawable(images.get(position));
                if (correctAnswer == position+1)
                    holderStandard.rearCorrect.setVisibility(View.VISIBLE);
                else holderStandard.rearIncorrect.setVisibility(View.VISIBLE);
                break;
            case TEST_CHOOSE:
                final ListeningVHtypeChoose holderChoose = (ListeningVHtypeChoose)holder;
                selects[position] = false;
                holderChoose.frontImage.setImageDrawable(images.get(position));
                holderChoose.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selects[position]){
                            selects[position] = false;
                            holderChoose.ivChoose.setVisibility(View.GONE);
                        } else {
                            selects[position] = true;
                            holderChoose.ivChoose.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            case TEST_SEQUENCE:
                final ListeningVHtypeSequence holserSequence = (ListeningVHtypeSequence)holder;
                holserSequence.frontImage.setImageDrawable(images.get(position));
                holserSequence.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holserSequence.llVariants.getVisibility() == View.VISIBLE){
                            holserSequence.llVariants.setVisibility(View.GONE);
                            sequence[position] = 0;
                        }else {
                            holserSequence.llVariants.setVisibility(View.VISIBLE);
                            holserSequence.ivVar1.setVisibility(View.VISIBLE);
                            holserSequence.ivVar2.setVisibility(View.VISIBLE);
                            holserSequence.ivVar3.setVisibility(View.VISIBLE);
                        }
                    }
                });
                holserSequence.ivVar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holserSequence.ivVar2.getVisibility() ==View.GONE){
                            sequence[position] = 0;
                            holserSequence.ivVar2.setVisibility(View.VISIBLE);
                            holserSequence.ivVar3.setVisibility(View.VISIBLE);
                        }else {
                            sequence[position] = 1;
                            holserSequence.ivVar2.setVisibility(View.GONE);
                            holserSequence.ivVar3.setVisibility(View.GONE);
                        }

                    }
                });
                holserSequence.ivVar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holserSequence.ivVar1.getVisibility() == View.GONE){
                            sequence[position] = 0;
                            holserSequence.ivVar1.setVisibility(View.VISIBLE);
                            holserSequence.ivVar3.setVisibility(View.VISIBLE);
                        }else {
                            sequence[position] = 2;
                            holserSequence.ivVar1.setVisibility(View.GONE);
                            holserSequence.ivVar3.setVisibility(View.GONE);
                        }

                    }
                });
                holserSequence.ivVar3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holserSequence.ivVar2.getVisibility() ==View.GONE){
                            sequence[position] = 0;
                            holserSequence.ivVar2.setVisibility(View.VISIBLE);
                            holserSequence.ivVar1.setVisibility(View.VISIBLE);
                        }else {
                            sequence[position] = 3;
                            holserSequence.ivVar1.setVisibility(View.GONE);
                            holserSequence.ivVar2.setVisibility(View.GONE);
                        }

                    }
                });
                break;
            case TEST_TEXT:
                ListeningVHtypeChoose holderChooseText = (ListeningVHtypeChoose)holder;
                holderChooseText.frontImage.setImageDrawable(images.get(position));
                holderChooseText.cardView.setClickable(false);
                break;
        }

    }




    @Override
    public int getItemCount() {return images.size();}


}
