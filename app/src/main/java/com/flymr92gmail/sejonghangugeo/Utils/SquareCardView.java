package com.flymr92gmail.sejonghangugeo.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;


public class SquareCardView extends CardView {

    int width;
    int height;



    public SquareCardView(Context context) {
        super(context);


    }

    public SquareCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SquareCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public boolean getUseCompatPadding() {
        return super.getUseCompatPadding();
    }

    @Override
    public void setUseCompatPadding(boolean useCompatPadding) {
        super.setUseCompatPadding(useCompatPadding);
    }

    @Override
    public void setContentPadding(int left, int top, int right, int bottom) {
        super.setContentPadding(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            width = getMeasuredWidth();
            height = getMeasuredWidth();

        }else {
             width = getMeasuredWidth();
             height = getMeasuredHeight();

        }
        setMeasuredDimension(width, height);
        Log.w("orientation","width:"+getMeasuredWidth()+"      height:"+ getMeasuredHeight() +" __________onMeasure     ");


    }

    @Override
    public void setMinimumWidth(int minWidth) {
        super.setMinimumWidth(minWidth);
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        super.setMinimumHeight(minHeight);
    }

    @Override
    public void setCardBackgroundColor(int color) {
        super.setCardBackgroundColor(color);
    }

    @Override
    public void setCardBackgroundColor(@Nullable ColorStateList color) {
        super.setCardBackgroundColor(color);
    }

    @Override
    public ColorStateList getCardBackgroundColor() {
        return super.getCardBackgroundColor();
    }

    @Override
    public int getContentPaddingLeft() {
        return super.getContentPaddingLeft();
    }

    @Override
    public int getContentPaddingRight() {
        return super.getContentPaddingRight();
    }

    @Override
    public int getContentPaddingTop() {
        return super.getContentPaddingTop();
    }

    @Override
    public int getContentPaddingBottom() {
        return super.getContentPaddingBottom();
    }

    @Override
    public void setRadius(float radius) {
        super.setRadius(radius);
    }

    @Override
    public float getRadius() {
        return super.getRadius();
    }

    @Override
    public void setCardElevation(float elevation) {
        super.setCardElevation(elevation);
    }

    @Override
    public float getCardElevation() {
        return super.getCardElevation();
    }

    @Override
    public void setMaxCardElevation(float maxElevation) {
        super.setMaxCardElevation(maxElevation);
    }

    @Override
    public float getMaxCardElevation() {
        return super.getMaxCardElevation();
    }

    @Override
    public boolean getPreventCornerOverlap() {
        return super.getPreventCornerOverlap();
    }

    @Override
    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        super.setPreventCornerOverlap(preventCornerOverlap);
    }

}
