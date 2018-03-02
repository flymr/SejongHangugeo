package com.flymr92gmail.sejonghangugeo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.ViewHolder.HeaderViewHolder;

import java.util.ArrayList;

/**
 * Created by hp on 01.03.2018.
 */

public class LegendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Legend> legends;
    private final int TITLE_ITEM = 1;
    private final int NORMAL_ITEM = 0;

    public LegendsAdapter(ArrayList<Legend> legends) {
        this.legends = legends;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           HeaderViewHolder viewHolder = (HeaderViewHolder)holder;
           Legend legend = legends.get(position);
           viewHolder.legendCategory.setVisibility(View.GONE);
           viewHolder.showAllBtn.setVisibility(View.GONE);
           if (getItemViewType(position)==TITLE_ITEM){
               viewHolder.categoryForGroup.setVisibility(View.VISIBLE);
               viewHolder.categoryForGroup.setText(legend.getLegendCategory());
           }
           String header = legend.getNameTranslate() + ". " + legend.getName();
           viewHolder.legendName.setText(header);
           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
               viewHolder.legendText.setText(Html.fromHtml(legend.getLegendText(), Html.FROM_HTML_MODE_LEGACY));
           } else {
               viewHolder.legendText.setText(Html.fromHtml(legend.getLegendText()));
           }


    }

    @Override
    public int getItemCount() {
        return legends.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TITLE_ITEM;
        if (!legends.get(position).getLegendCategory()
                .equals(legends.get(position-1).getLegendCategory())) return TITLE_ITEM;
        else return NORMAL_ITEM;
    }
}
