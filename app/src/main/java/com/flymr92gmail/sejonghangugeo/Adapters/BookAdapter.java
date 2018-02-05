package com.flymr92gmail.sejonghangugeo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flymr92gmail.sejonghangugeo.R;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    static final int TYPE_HEADER = 0;
    static final int TYPE_BOOK = 1;

    public BookAdapter() {

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
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_BOOK: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_item, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_BOOK:
                break;
        }
    }


    public class BookViewHolder extends RecyclerView.ViewHolder{

        public BookViewHolder(View itemView) {
            super(itemView);

        }
    }

    public class HeaderViewHolderBook extends RecyclerView.ViewHolder{

        public HeaderViewHolderBook(View itemView) {
            super(itemView);

        }
    }
}