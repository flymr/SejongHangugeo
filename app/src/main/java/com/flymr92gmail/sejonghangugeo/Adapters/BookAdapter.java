package com.flymr92gmail.sejonghangugeo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.flymr92gmail.sejonghangugeo.POJO.Legend;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.ViewClickListener;
import com.flymr92gmail.sejonghangugeo.ViewHolder.HeaderViewHolder;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Legend legend;
    private ViewClickListener viewClickListener;
    private final int TYPE_HEADER = 0;
    private final int TYPE_BOOK = 1;


    public BookAdapter(Legend legend, ViewClickListener viewClickListener) {
        this.viewClickListener = viewClickListener;
        this.legend = legend;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_BOOK;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                return new HeaderViewHolder(view) {
                };
            }
            case TYPE_BOOK: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_item, parent, false);
                return new BookViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                HeaderViewHolder viewHolder = (HeaderViewHolder)holder;
                String category = "Категория: " + legend.getLegendCategory();
                viewHolder.legendCategory.setText(category);
                String header = legend.getNameTranslate() + ". " + legend.getName();
                viewHolder.legendName.setText(header);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    viewHolder.legendText.setText(Html.fromHtml(legend.getLegendText(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    viewHolder.legendText.setText(Html.fromHtml(legend.getLegendText()));
                }
                viewHolder.showAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewClickListener.onViewClicked();
                    }
                });
                break;
            case TYPE_BOOK:
                BookViewHolder bookViewHolder = (BookViewHolder)holder;
                break;
        }
    }


    private class BookViewHolder extends RecyclerView.ViewHolder{

        private BookViewHolder(View itemView) {
            super(itemView);
        }


    }

}