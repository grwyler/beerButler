package group10.tcss450.uw.edu.challengeapp.Adapter;

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
