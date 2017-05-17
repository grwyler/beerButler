/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
public class MainPageFragment extends Fragment implements View.OnClickListener {

    private static final String PARTIAL_URL = "http://api.brewerydb.com/v2/search/geo/point" +
            "?key=b5a1363a472d95fdab32ea49a2c3eb3f&";
    private OnFragmentInteractionListener mListener;
    private String mLongitude;
    private String mLatitude;

    public MainPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_page, container, false);
        Button b = (Button) v.findViewById(R.id.brew_tour_button);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.beer_list_button);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        AsyncTask<String, Void, String> task;
        if (mListener != null) {
            switch (v.getId()) {
                //start async task to hit the API and then open the BrewTourFrag
                case R.id.brew_tour_button:
                    task = new BrewTourWebServiceTask();
//                    String lat = "47.248731";
//                    String lng = "-122.444532";
//                    if (MainActivity.getPermission()) {
//                        Log.d("MainPageFrag ", mLatitude + ", " + mLongitude);
//                        lng = mLongitude;
//                        lat = mLatitude;
//                    }
                    Log.d("MainPageFrag ", mLatitude + ", " + mLongitude);
                    task.execute(PARTIAL_URL, "lat=" + mLatitude + "&lng=" + mLongitude);
                    break;
                //open the UserProfileFragment
                case R.id.user_profile_button:
                    Toast.makeText(getActivity(),
                            "User profile is not implemented yet!",
                            Toast.LENGTH_LONG).show();
                    break;
                // Open the BeerListFragment
                case R.id.beer_list_button:
                    mListener.onMainPageBeerListFragmentInteraction("Hello");
                    break;
                default:
                    Toast.makeText(getActivity(),
                            "Error in the button selection process!",
                            Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    /**
     * An interface for the activity to implement to facilitate inter-fragment communication.
     */
    interface OnFragmentInteractionListener {
        /**
         * Used to notify the activity the BrewTourFrag needs to be opened.
         * @param json The message to send to the activity.
         */
        void onMainPageBrewTourFragmentInteraction(String json);
        /**
         * Used to notify the activity that the BeerListFragment needs to be opened.
         * @param json The message to send to the activity.
         */
        void onMainPageBeerListFragmentInteraction(String json);
    }

    /**
     * Asynchronous task that contacts the breweryDB API to get breweries in the area.
     */
    private class BrewTourWebServiceTask extends AsyncTask<String, Void, String> {
        //private final String SERVICE = "_post.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(strings[0] + strings[1]);
                urlConnection = (HttpURLConnection) urlObject.openConnection();

                InputStream content = urlConnection.getInputStream();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            mListener.onMainPageBrewTourFragmentInteraction(result);
        }
    }

}