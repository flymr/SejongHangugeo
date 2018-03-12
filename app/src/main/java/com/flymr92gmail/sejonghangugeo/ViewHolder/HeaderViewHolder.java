package com.flymr92gmail.sejonghangugeo.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.Adapters.NewWordsRecyclerAdapter;
import com.flymr92gmail.sejonghangugeo.DataBases.External.AppDataBase;
import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.R;
import com.github.zagum.expandicon.ExpandIconView;

import eu.davidea.flipview.FlipView;

/**
 * Created by DELL on 2/18/2018.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
   public TextView legendName;
    public TextView legendText;
    public TextView legendCategory;
    private ImageView iv_add;
    private ExpandIconView dropBtn;
    private boolean isViewExpanded = false;
    public Button showAllBtn;
    public TextView categoryForGroup;
    private LinearLayout llExpand;
    public CardView cardView;
    public HeaderViewHolder(View itemView) {
        super(itemView);
        legendName = itemView.findViewById(R.id.legend_header);
        legendText = itemView.findViewById(R.id.legend_text);
        legendCategory = itemView.findViewById(R.id.legend_category);
        categoryForGroup = itemView.findViewById(R.id.group_category);
        iv_add = itemView.findViewById(R.id.iv_add_legend);
        showAllBtn = itemView.findViewById(R.id.show_all_btn);
        dropBtn = itemView.findViewById(R.id.drop_button);
        llExpand = itemView.findViewById(R.id.expand_ll);
        cardView = itemView.findViewById(R.id.card_view_legend);
        dropBtn.setOnClickListener(this);
        if (!isViewExpanded){
            llExpand.setVisibility(View.GONE);
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
        a.setDuration((int)(targetHeight*2 / v.getContext().getResources().getDisplayMetrics().density));
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

    @Override
    public void onClick(View view) {
                dropBtn.switchState();
                if (isViewExpanded){
                    isViewExpanded = false;
                    dropBtn.setState(ExpandIconView.MORE, true);
                    collapse(llExpand);
                }else {
                    isViewExpanded = true;
                    dropBtn.setState(ExpandIconView.LESS, true);
                    expand(llExpand);
                }
    }
}
