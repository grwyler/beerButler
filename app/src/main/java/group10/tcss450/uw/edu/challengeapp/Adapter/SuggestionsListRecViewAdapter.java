package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.BeerList.TopBrew;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * Created by Chris on 5/25/2017.
 */

public class SuggestionsListRecViewAdapter  extends RecyclerView.Adapter<SuggestionsListRecViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    /** The list of TopBrew objects that need to be added to the recycler view.*/
    private ArrayList<TopBrew> mDataset;
    /** Resources to use string resources */
    private Resources mResources;

    /**
     * Recycler view adapter constructor.
     * @param myDataset an array of complete Brew objects
     */
    public SuggestionsListRecViewAdapter(ArrayList<TopBrew> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public SuggestionsListRecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.brew_tour_card_view,parent, false);
        mResources = parent.getContext().getResources();
        return new ViewHolder(cv);
    }


    @Override
    public void onBindViewHolder(SuggestionsListRecViewAdapter.ViewHolder viewHolder, int position) {
        TopBrew topBrew = mDataset.get(position);
        String name = "";
        ImageView imageView = viewHolder.mImageView;
        viewHolder.mImageView.setImageResource(R.drawable.placeholder);
        if (topBrew != null) {
            TopBrew.labels labels = topBrew.getLabels();
            name = topBrew.getName();

            if(labels != null) {
                new DownloadImageTask(imageView).execute(labels.getLarge());
            } else viewHolder.mImageView.setImageResource(R.drawable.stout);
        } else viewHolder.mImageView.setImageResource(R.drawable.stout);
        if (name == null || name.equals("")) {
            name = "Brew";
        }
        name = mResources.getString(R.string.text_view_brewery) + name;
        viewHolder.mBrewName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * A View Holder used to change the elements inside the recycler view.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        /**The ImageView for the mBrewery logo to go into.*/
        ImageView mImageView;
        /** The TextView to display the name of the mBrewery.*/
        TextView mBrewName;


        /**
         * Constructor class. Initialize all fields.
         * @param cardView the Cardview being passed.
         */
        ViewHolder(CardView cardView) {
            super(cardView);
            mImageView = (ImageView) cardView.findViewById(R.id.brew_pic);
            mBrewName = (TextView) cardView.findViewById(R.id.brewery_name);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: tie this into the beer rating function
                    //Activity a = new Activity();
                }
            });
        }
    }

    /**
     * Helper class used to download and return an image.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView bmImage;

        /**
         * Constructor
         * @param bmImage The ImageView to be updated.
         */
        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /**
         * Makes a connection to the URL, downloads the image, and saves it as bitmap
         * @param urls the URLs to check for an image.
         * @return a Bitmap image from the url.
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
