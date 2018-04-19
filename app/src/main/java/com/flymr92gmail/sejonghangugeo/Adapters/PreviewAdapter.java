package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.ViewHolder.PreviewViewHolder;


public class PreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;

    public PreviewAdapter(Context context) {
     this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_preview, parent, false);

        return new PreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PreviewViewHolder viewHolder = (PreviewViewHolder)holder;
        String[] stringsArray = new String[2];
        switch (position){
            case 0:
                stringsArray = context.getResources()
                        .getStringArray(R.array.preview_page0);
                break;
            case 1:
                stringsArray = context.getResources()
                        .getStringArray(R.array.preview_page1);
                break;
            case 2:
                stringsArray = context.getResources()
                        .getStringArray(R.array.preview_page2);
                break;
            case 3:
                stringsArray = context.getResources()
                        .getStringArray(R.array.preview_page3);
                break;
            case 4:
                stringsArray = context.getResources()
                        .getStringArray(R.array.preview_page4);
                break;
        }
        viewHolder.title.setText(stringsArray[0]);
        viewHolder.subTitle.setText(stringsArray[1]);

    }

    @Override
    public int getItemCount() {
        return 5;
    }


}
