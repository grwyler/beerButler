package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

    public void populateList(String beerList) {
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
                    boolean isOrganic = states[2].equals("1") ? true : false;
                    mBeerList.add(new Beer(states[0], states[1], isOrganic, states[3], states[4],
                            Double.valueOf(states[5]), Double.valueOf(states[6]), states[7], states[8],
                            states[9]));
                }
                beers = beers.substring(beers.indexOf("$$$") + 3);
            }
        }
    }

    public String getmUsername() {
        return mUsername;
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
        ViewHolder(CardView cardView, final String username) {
            super(cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RateBeerFragment rf = new RateBeerFragment();
                    Bundle args = new Bundle();
                    args.putSerializable(USERNAME_KEY, username);
                    args.putSerializable(BEERNAME_KEY, mName.getText().toString());
                    rf.setArguments(args);
                    FragmentTransaction tran = MainActivity.mFragManager
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, rf)
                            .addToBackStack(null);
                    tran.commit();
                }
            });
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
        return new BeerListRecViewAdapter.ViewHolder(cv, mUsername);
    }

    @Override
    public void onBindViewHolder (BeerListRecViewAdapter.ViewHolder holder, int position) {
        Beer beer = mBeerList.get(position);
        holder.mImageView.setImageResource(R.drawable.stout);
        holder.mName.setText(beer.getmName());
        String style = "Style: " + beer.getStyle();
        holder.mStyle.setText(style);
        String label = "Label: " + beer.getLabelLink();
        holder.mLabelLink.setText(label);
        String brewery = "Brewery: " + beer.getBrewery();
        holder.mBrewery.setText(brewery);
        String ABV = "ABV: " + beer.getAbv();
        holder.mAbv.setText(ABV);
        String IBU = "IBUs: " + beer.getIbu();
        holder.mIbu.setText(IBU);
        String desc = "Description: " + beer.getDescription();
        holder.mDescription.setText(desc);
        String notes = "Notes: " + beer.getNotes();
        holder.mNotes.setText(notes);
        String rating = "Rating: " + beer.getRating();
        holder.mRating.setText(rating);
        String boo;
        if (beer.getIsOrganic()) {
            boo = "yes";
        } else { boo = "no"; }
        String org = "Organic: " + boo;
        holder.mIsOrganic.setText(org);
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
        /** The start of a string returned if there was an error connecting to the DB.*/
        private final String START_ERROR = "Unable to";
        /** The error message if the user enters wrong data for logging in*/
        private final String TOAST_ERROR = "That user name is already being used";


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

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
//            if (result.startsWith(START_ERROR)) {
//                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//            } else if(result.startsWith("Successfully")) {
//                mListener.onRegisterFragmentInteraction(result);
//            } else {
//                Toast.makeText(getActivity(), TOAST_ERROR, Toast
//                        .LENGTH_SHORT).show();
//            }
        }
    }

}
