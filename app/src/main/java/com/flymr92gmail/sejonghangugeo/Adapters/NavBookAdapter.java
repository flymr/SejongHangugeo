package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hp on 05.02.2018.
 */

public class NavBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int differencePages;
    private int currentPage;
    private  int pageCount;
    private Context context;

    public NavBookAdapter(int differencePages,int currentPage, int pageCount, Context context) {
    this.differencePages = differencePages;
    this.currentPage = currentPage;
    this.pageCount = pageCount;
    this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PageViewHolder viewHolder = (PageViewHolder)holder;
        String s = ""+(position+differencePages);
        viewHolder.pageNum.setText(s);
        if (currentPage == position)
            viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        else viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        try {
            Drawable d;
            // get input stream
            InputStream ims = context.getAssets().open("pages/page-"+(position+1)+"-1.png");
            // load image as Drawable

            d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            viewHolder.imageView.setImageDrawable(d);
        }
        catch(IOException ex) {

        }

    }

    class PageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView pageNum;
        CardView cardView;
        PageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_page_nav);
            pageNum = itemView.findViewById(R.id.tv_page_nav);
            cardView = itemView.findViewById(R.id.cv_nav_page);

        }
    }


}