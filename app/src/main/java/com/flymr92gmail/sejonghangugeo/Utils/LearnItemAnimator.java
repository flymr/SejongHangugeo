package com.flymr92gmail.sejonghangugeo.Utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.animation.BounceInterpolator;

import com.flymr92gmail.sejonghangugeo.R;

/**
 * Created by hp on 06.01.2018.
 */

public class LearnItemAnimator extends SimpleItemAnimator{

    Context context;

    public LearnItemAnimator(Context context) {

        this.context = context;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        Animator set = AnimatorInflater.loadAnimator(context,
                R.animator.scale_learnitem);
        set.setInterpolator(new BounceInterpolator());
        set.setTarget(holder.itemView);
        set.start();
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {

        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return true;
    }

    @Override
    public void runPendingAnimations() {

    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
