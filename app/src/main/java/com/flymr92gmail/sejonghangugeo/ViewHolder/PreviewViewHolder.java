package com.flymr92gmail.sejonghangugeo.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.R;

/**
 * Created by hp on 21.02.2018.
 */

public class PreviewViewHolder extends RecyclerView.ViewHolder{
    public TextView title, subTitle;
    public ImageView iv_page;
    public PreviewViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_page);
        subTitle = itemView.findViewById(R.id.subtitle_page);
        iv_page = itemView.findViewById(R.id.iv_preview);
    }
}
