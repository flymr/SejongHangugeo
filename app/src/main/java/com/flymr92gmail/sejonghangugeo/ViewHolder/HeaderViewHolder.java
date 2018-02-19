package com.flymr92gmail.sejonghangugeo.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.R;

import eu.davidea.flipview.FlipView;

/**
 * Created by DELL on 2/18/2018.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder{
   public TextView legendName;
    public TextView legendText;
    private ImageView iv_add;
    private FlipView flipView;
    private boolean isViewExpanded = false;
    public HeaderViewHolder(View itemView) {
        super(itemView);
        legendName = itemView.findViewById(R.id.legend_header);
        legendText = itemView.findViewById(R.id.legend_text);
        iv_add = itemView.findViewById(R.id.iv_add_legend);
        flipView = itemView.findViewById(R.id.drop_down_up);

        flipView.setOnFlippingListener(new FlipView.OnFlippingListener() {
            @Override
            public void onFlipped(FlipView flipView, boolean checked) {
                if (checked){
                    isViewExpanded = true;

                    expand(legendText);
                }else {
                    isViewExpanded = false;

                    collapse(legendText);
                }
            }
        });
        if (!isViewExpanded){
            legendText.setVisibility(View.GONE);
            //  legendText.setEnabled(false);
        }
    }

    private static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
