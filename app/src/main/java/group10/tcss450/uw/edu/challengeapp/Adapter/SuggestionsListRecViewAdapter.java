package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.BeerList.TopBrew;
import group10.tcss450.uw.edu.challengeapp.R;
import group10.tcss450.uw.edu.challengeapp.SuggestionsList.SuggestionsListFragment;

/**
 * Created by Chris on 5/25/2017.
 */

public class SuggestionsListRecViewAdapter  extends RecyclerView.Adapter<SuggestionsListRecViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private final Context mContext;
    private final SuggestionsListFragment.OnFragmentInteractionListener mListener;
    /** The list of TopBrew objects that need to be added to the recycler view.*/
    private ArrayList<TopBrew> mDataset;
    private static final String BEERLIST_PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/beerList";
    private String mUsername;

    /**
     * Recycler view adapter constructor.
     * @param myDataset an array of complete Brew objects
     */
    public SuggestionsListRecViewAdapter(ArrayList<TopBrew> myDataset, String username,
                                         Context context,
                                         SuggestionsListFragment
                                                 .OnFragmentInteractionListener listener) {
        mDataset = myDataset;
        mUsername = username;
        mContext = context;
        mListener = listener;
    }

    @Override
    public SuggestionsListRecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.brew_tour_card_view, parent, false);
        return new ViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(SuggestionsListRecViewAdapter.ViewHolder viewHolder, int position) {
        final TopBrew topBrew = mDataset.get(position);
        String name = "";
        ImageView imageView = viewHolder.mImageView;
        CardView cardView = viewHolder.mCardView;
        viewHolder.mImageView.setImageResource(R.drawable.placeholder);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, String> task;
                task = new AddBeerToDBTask();
                task.execute(BEERLIST_PARTIAL_URL, topBrew.getName(),
                        topBrew.getStyle() == null ? "" : topBrew.getStyle().getName(),
                        topBrew.getIsOrganic(),
                        topBrew.getLabels() == null ? "no link" : topBrew.getLabels().getLarge(),
                        topBrew.getNameDisplay(), topBrew.getAbv(), topBrew.getIbu(),
                        topBrew.getDescription(), "", "");
            }
        });
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
//        name = mResources.getString(R.string.text_view_brewery) + name;
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

        CardView mCardView;


        /**
         * Constructor class. Initialize all fields.
         * @param cardView the Cardview being passed.
         */
        ViewHolder(CardView cardView) {
            super(cardView);
            mCardView = cardView;
            mImageView = (ImageView) cardView.findViewById(R.id.brew_pic);
            mBrewName = (TextView) cardView.findViewById(R.id.brewery_name);
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

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class AddBeerToDBTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 11) {
                /* Exception message for too few or too many args*/
                String EXCEPTION_MSG = "Three String arguments required.";
                throw new IllegalArgumentException(EXCEPTION_MSG);
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + "_post.php");
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                for (int i = 0; i < strings.length; i++) {
                    if (strings[i] == null) {
                        strings[i] = "0";
                    }
                }
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = eURL("name", "=") + eURL(strings[1], "&")
                        + eURL("style", "=") + eURL(strings[2], "&")
                        + eURL("isOrganic", "=") + eURL(strings[3], "&")
                        + eURL("labelLink", "=") + eURL(strings[4], "&")
                        + eURL("brewery", "=") + eURL(strings[5], "&")
                        + eURL("abv", "=") + eURL(strings[6], "&")
                        + eURL("ibu", "=") + eURL(strings[7], "&")
                        + eURL("description", "=") + eURL(strings[8], "&")
                        + eURL("notes", "=") + eURL(strings[9], "&")
                        + eURL("rating", "=")  + eURL(strings[10], "&")
                        + eURL("username", "=" + eURL(mUsername, ""));
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                /*
      Start of the message to notify the user of connection failure.
     */
                String EXCEPTION_MSG_2 = "Unable to connect, Reason: ";
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

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) dialog.dismiss();
            mListener.onBeerSuccessfullyAddedToDB();
        }
    }
}


