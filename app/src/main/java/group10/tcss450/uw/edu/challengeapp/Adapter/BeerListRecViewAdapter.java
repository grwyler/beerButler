package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.BeerList.Beer;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * Created by Garrett on 5/19/2017.
 */

public class BeerListRecViewAdapter extends RecyclerView.Adapter<BeerListRecViewAdapter
        .ViewHolder> {

    /** The list of TopBrewery objects that need to be added to the recycler view.*/
    private ArrayList<Beer> mDataset;
    /** Resources to use string resources */
    private Resources mResources;

    public BeerListRecViewAdapter(ArrayList<Beer> beerList) {
        mDataset = beerList;
    }

    public void addBeer(Beer beer) {
        mDataset.add(beer);
    }

    /**
     * A View Holder used to change the elements inside the recycler view.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        /**The ImageView for the mBrewery logo to go into.*/
        ImageView mImageView;
        /** The TextView to display the name of the beer.*/
        TextView mName;
        /** The TextView to display the style.*/
        TextView mStyle;
        /** The TextView to display the distance from your location*/
        TextView mIsOrganic;
        /** The TextView to display the distance from your location*/
        TextView mLabelLink;
        /** The TextView to display the distance from your location*/
        TextView mBrewery;
        /** The TextView to display the distance from your location*/
        TextView mAbv;
        /** The TextView to display the distance from your location*/
        TextView mIbu;
        /** The TextView to display the distance from your location*/
        TextView mDescription;
        /** The TextView to display the distance from your location*/
        TextView mNotes;
        /** The TextView to display the distance from your location*/
        TextView mRating;


        /**
         * Constructor class. Initialize all fields.
         * @param cardView the Cardview being passed.
         */
        ViewHolder(CardView cardView) {
            super(cardView);
            mImageView = (ImageView) cardView.findViewById(R.id.brew_pic);
            mName = (TextView) cardView.findViewById(R.id.name_view);
            mStyle = (TextView) cardView.findViewById(R.id.style_view);
            mIsOrganic = (TextView) cardView.findViewById(R.id.is_organic_view);
            mLabelLink = (TextView) cardView.findViewById(R.id.label_link_view);
            mBrewery = (TextView) cardView.findViewById(R.id.brewery_view);
            mAbv = (TextView) cardView.findViewById(R.id.abv_view);
            mIbu = (TextView) cardView.findViewById(R.id.ibu_view);
            mDescription = (TextView) cardView.findViewById(R.id.description_view);
            mNotes = (TextView) cardView.findViewById(R.id.notes_view);
            mRating = (TextView) cardView.findViewById(R.id.rating_view);
        }
    }

    @Override
    public BeerListRecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.beer_list_card_view, parent, false);
        mResources = parent.getContext().getResources();
        return new BeerListRecViewAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder (BeerListRecViewAdapter.ViewHolder holder, int position) {
        Beer beer = mDataset.get(position);
        holder.mImageView.setImageResource(R.drawable.stout);
        holder.mName.setText(beer.getmName());
        holder.mStyle.setText(beer.getStyle());
        holder.mLabelLink.setText(beer.getLabelLink());
        holder.mBrewery.setText(beer.getBrewery());
        holder.mAbv.setText(beer.getAbv() + "");
        holder.mIbu.setText(beer.getIbu() + "");
        holder.mDescription.setText(beer.getDescription());
        holder.mNotes.setText(beer.getNotes());
        holder.mRating.setText(beer.getRating() + "");
        holder.mIsOrganic.setText(beer.getIsOrganic() + "");
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mDataset != null) size = mDataset.size();
        return size;
    }

}
