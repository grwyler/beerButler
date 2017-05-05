/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */
package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.BrewTour.TopBrewery;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * An adapter class to coordinate the recycler view
 */
public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ViewHolder> {

    private ArrayList<TopBrewery> mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mBreweryName;
        public TextView mHours;
        public TextView mDist;
        public Button mButton;

        public ViewHolder(CardView cardView) {
            super(cardView);
            mImageView = (ImageView) cardView.findViewById(R.id.brew_pic);
            mBreweryName = (TextView) cardView.findViewById(R.id.brewery_name);
            mHours = (TextView) cardView.findViewById(R.id.hours);
            mDist = (TextView) cardView.findViewById(R.id.dist);
            mButton = (Button) cardView.findViewById(R.id.nav_button);

        }
    }

    /**
     * Recycler view adapter constructor.
     * @param myDataset the desired dataset for the recycler view.
     */
    public RecViewAdapter (ArrayList<TopBrewery> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.brew_tour_card_view,parent, false);


        ViewHolder vh = new ViewHolder(cv);
        return vh;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        holder.mImageView.setImageResource(R.drawable.stout);
        holder.mBreweryName.setText("Brewery: " + mDataset.get(position).getName());
        holder.mDist.setText("Distance: " + mDataset.get(position).getDistance());
        holder.mHours.setText("Rating: " + mDataset.get(position).getHoursOfOperation());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
