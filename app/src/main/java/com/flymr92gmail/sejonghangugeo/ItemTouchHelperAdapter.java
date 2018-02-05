package com.flymr92gmail.sejonghangugeo;


public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);


}
