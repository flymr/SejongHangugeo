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
import com.flymr92gmail.sejonghangugeo.Utils.Helper;
import com.github.zagum.expandicon.ExpandIconView;

import eu.davidea.flipview.FlipView;

/**
 * Created by DELL on 2/18/2018.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
   public TextView legendName;
    public TextView legendText;
    public TextView legendCategory;
    private ExpandIconView dropBtn;
    private boolean isViewExpanded = false;
    public Button showAllBtn;
    public TextView categoryForGroup;
    private LinearLayout llExpand;
    public HeaderViewHolder(View itemView) {
        super(itemView);
        legendName = itemView.findViewById(R.id.legend_header);
        legendText = itemView.findViewById(R.id.legend_text);
        legendCategory = itemView.findViewById(R.id.legend_category);
        categoryForGroup = itemView.findViewById(R.id.group_category);
        showAllBtn = itemView.findViewById(R.id.show_all_btn);
        dropBtn = itemView.findViewById(R.id.drop_button);
        llExpand = itemView.findViewById(R.id.expand_ll);
        dropBtn.setOnClickListener(this);
        if (!isViewExpanded){
            llExpand.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
                dropBtn.switchState();
                if (isViewExpanded){
                    isViewExpanded = false;
                    dropBtn.setState(ExpandIconView.MORE, true);
                    Helper.collapse(llExpand);
                }else {
                    isViewExpanded = true;
                    dropBtn.setState(ExpandIconView.LESS, true);
                    Helper.expand(llExpand);
                }
    }
}
