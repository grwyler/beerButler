package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


public interface ItemTouchHelperAdapter {

    /**
     *
     *
     * @param fromPosition
     * @param toPosition
     * @return
     */
    boolean onItemMove(int fromPosition, int toPosition);


    /**
     *
     * @param position
     */
    void onItemDismiss(int position);
}
