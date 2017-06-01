package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import group10.tcss450.uw.edu.challengeapp.BeerList.Beer;
import group10.tcss450.uw.edu.challengeapp.BeerList.RateBeerFragment;
import group10.tcss450.uw.edu.challengeapp.MainActivity;
import group10.tcss450.uw.edu.challengeapp.R;
/**
 * Created by Garrett on 5/19/2017.
 */
public class BeerListRecViewAdapter extends RecyclerView.Adapter<BeerListRecViewAdapter
        .ViewHolder>  implements ItemTouchHelperAdapter {
    public static final String NOTES_KEY = "You got a friend in me.";
    public static final String RATING_KEY = "You must be this tall to ride the ride.";
    final public static String USERNAME_KEY = "Friends don't let friends drink light beer.";
    final public static String BEERNAME_KEY = "Don't drink and drive kids. You will spill your beer.";
    /** The list of TopBrewery objects that need to be added to the recycler view.*/
    private ArrayList<Beer> mBeerList;
    public String mUsername;
    /** Resources to use string resources */
    private Resources mResources;
    private static final String BEERLIST_PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/beerList";

    public BeerListRecViewAdapter(String beerList, String username) {
        mBeerList = new ArrayList<>();
        mUsername = username;
        populateList(beerList);
    }

    private void populateList(String beerList) {
        int size = Integer.valueOf(beerList.substring(0, beerList.indexOf("name=")));
        String beers = beerList;
        String[] identifiers = makeIdentifiers();
        String[] states = new String[10];
        for (int i = 0; i < size; i++) {
            boolean alreadyAdded = false;
            if (beers.length() > 3) {
                for (int j = 0; j < states.length; j++) {
                    if (j >= states.length - 1) {
                        states[j] = beers.substring(beers.indexOf(identifiers[j]) + identifiers[j]
                                .length(), beers.indexOf("$$$"));
                    } else {
                        states[j] = beers.substring(beers.indexOf(identifiers[j]) + identifiers[j].length(),
                                beers.indexOf(identifiers[j + 1]));
                    }
                }

                for (int j = 0; j < mBeerList.size(); j++) {
                    if(mBeerList.get(j).getmName().equals(states[0])) {
                        alreadyAdded = true;
                        i--;
                    }
                }
                if (!alreadyAdded) {
                    mBeerList.add(new Beer(states[0], states[1], states[2], states[3], states[4],
                            states[5], states[6], states[7], states[8], states[9]));
                }
                beers = beers.substring(beers.indexOf("$$$") + 3);
            }
        }
    }

    private String[] makeIdentifiers() {
        String[] identifiers = new String[10];
        identifiers[0] = "name=";
        identifiers[1] = "style=";
        identifiers[2] = "isOrganic=";
        identifiers[3] = "labelLink=";
        identifiers[4] = "brewery=";
        identifiers[5] = "abv=";
        identifiers[6] = "ibu=";
        identifiers[7] = "description=";
        identifiers[8] = "notes=";
        identifiers[9] = "rating=";
        return identifiers;
    }

    /**
     * This seems to do nothing, the original example allows for views to be swapped
     * But I believe it won't work within our RecyclerView
     *
     * @param fromPosition more info
     * @param toPosition more info
     * @return boolean
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mBeerList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mBeerList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        AsyncTask<String, Void, String> task;
        task = new RemoveBeerFromDBTask();
        task.execute(BEERLIST_PARTIAL_URL, mBeerList.get(position).getmName());
        mBeerList.remove(position);
        notifyItemRemoved(position);
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
//        /** The TextView to display if the beer is organic*/
//        TextView mLabelLink;
        /** The TextView to display the brewery name*/
        TextView mBrewery;
        /** The TextView to display the ABV*/
        TextView mAbv;
        /** The TextView to display the IBUs*/
        TextView mIbu;
        /** The TextView to display the users notes*/
        TextView mNotes;

        ImageView mOrganicPic;

        RatingBar mRating;

        CardView mCardView;
        private String oldNote;
        private String oldRating;
        private String oldDescription;

        /**
         * Constructor class. Initialize all fields.
         * @param cardView the Cardview being passed.
         */
        ViewHolder(CardView cardView) {
            super(cardView);
            mCardView = cardView;
            mImageView = (ImageView) cardView.findViewById(R.id.brew_pic);
            mName = (TextView) cardView.findViewById(R.id.name_view);
            mStyle = (TextView) cardView.findViewById(R.id.style_view);
            mOrganicPic = (ImageView) cardView.findViewById(R.id.organic);
            mBrewery = (TextView) cardView.findViewById(R.id.brewery_view);
            mAbv = (TextView) cardView.findViewById(R.id.abv_view);
            mIbu = (TextView) cardView.findViewById(R.id.ibu_view);
            mNotes = (TextView) cardView.findViewById(R.id.notes_view);
            mRating = (RatingBar) cardView.findViewById(R.id.ratingBar2);
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
    public void onBindViewHolder (final BeerListRecViewAdapter.ViewHolder holder, int position) {
        final Beer beer = mBeerList.get(position);
        ImageView imageView = holder.mImageView;

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateBeerFragment rf = new RateBeerFragment();
                Bundle args = new Bundle();
                args.putSerializable(USERNAME_KEY, mUsername);
                args.putSerializable(BEERNAME_KEY, holder.mName.getText().toString());
                args.putSerializable(NOTES_KEY, holder.oldNote);
                args.putSerializable(RATING_KEY, holder.oldRating);
                args.putString("Description", beer.getDescription());
                rf.setArguments(args);
                FragmentTransaction tran = MainActivity.mFragManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, rf)
                        .addToBackStack("rate");
                tran.commit();
            }
        });

        holder.mImageView.setImageResource(R.drawable.placeholder);
        if (beer != null) {
            if(!beer.getLabelLink().equals("no link")) {
                new DownloadImageTask(imageView).execute(beer.getLabelLink());
            } else imageView.setImageResource(R.drawable.stout);
            holder.mName.setText(beer.getmName());
            holder.mStyle.setText(beer.getStyle());
            String brewery = "Brewery: " + beer.getBrewery();
            holder.mBrewery.setText(brewery);
            holder.mAbv.setText(beer.getAbv().equals("0") ? "" : "ABV: " + beer.getAbv());
            holder.mIbu.setText(beer.getIbu().equals("0") ? "" : "IBUs: " + beer.getIbu());
            String notes = beer.getNotes();
            holder.oldNote = beer.getNotes();
            holder.mNotes.setText(notes);
            String rating = beer.getRating().equals("") ? "0" : beer.getRating();
            holder.oldRating = beer.getRating();
            holder.mRating.setRating(Float.valueOf(rating));
            String boo = beer.getIsOrganic();
            if (boo.equals("Y")) holder.mOrganicPic.setVisibility(View.VISIBLE);
        } else imageView.setImageResource(R.drawable.stout);

//        holder.mImageView.setImageResource(R.drawable.stout);

    }

    @Override
    public int getItemCount() {
        return mBeerList == null? 0 : mBeerList.size();
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class RemoveBeerFromDBTask extends AsyncTask<String, Void, String> {

        /** Exception message for too few or too many args*/
        private final String EXCEPTION_MSG = "Three String arguments required.";
        /** Start of the message to notify the user of connection failure.*/
        private final String EXCEPTION_MSG_2 = "Unable to connect, Reason: ";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException(EXCEPTION_MSG);
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + "_remove.php");
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = eURL("name", "=") + eURL(strings[1], "&") + eURL("username", "=" +
                        eURL(mUsername, ""));
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = EXCEPTION_MSG_2 + e.getMessage();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return response;
        }

        private String eURL(String string, String follow) {
            String result;
            try {
                result = URLEncoder.encode(string, "UTF-8");
            } catch (Exception e) {
                result = e.toString();
            }
            return result + follow;
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
