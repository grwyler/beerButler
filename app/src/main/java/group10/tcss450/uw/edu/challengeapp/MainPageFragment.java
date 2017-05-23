/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String BEERLIST_PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/beerList";
    private static final String API_BEERS = "http://api.brewerydb.com/v2/beers/" +
            "?key=b5a1363a472d95fdab32ea49a2c3eb3f&";

    /** The start of a string returned if there was an error connecting to the DB.*/
    private final String START_ERROR = "Unable to";
    /** The error message if the user enters wrong data for logging in*/
    private final String TOAST_ERROR = "Not a recognized account";
    /** Exception message for too few or too many args*/
    private final String EXCEPTION_MSG = "One String arguments required.";
    /** Start of the message to notify the user of connection failure.*/
    private final String EXCEPTION_MSG_2 = "Unable to connect, Reason: ";

    private OnFragmentInteractionListener mListener;
    private String mLongitude;
    private String mLatitude;

    private String mUsername;

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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string
                .login_prefs), Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString(getString(R.string.usernamePrefs), "");
        return v;
    }

    @Override
    public void onClick(View v) {
        AsyncTask<String, Void, String> task;
        if (mListener != null) {
            switch (v.getId()) {
                //start async task to hit the API and then open the BrewTourFrag
                case R.id.brew_tour_button:
                    String lat = mLatitude;
                    String lng = mLongitude;
//                    Log.d("MainPageFragemnt", lat + ", " + lng);
                    task = new BrewTourWebServiceTask();
                    task.execute(PARTIAL_URL, "lat=" + lat + "&lng=" + lng);
                    break;
                case R.id.user_profile_button:
                    Toast.makeText(getActivity(),
                            "User profile is not implemented yet!",
                            Toast.LENGTH_LONG).show();
                    break;
                case R.id.beer_list_button:
                    task = new GetBeerListTask();
                    task.execute(API_BEERS);
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
         * Used to notify the activity that the sign-in was successful.
         * @param json The message to send to the activity.
         */
        void onMainPageBrewTourFragmentInteraction(String json);

        void onMainPageBeerListFragmentInteraction(String result);
    }

    /**
     * Asyncrinous task that contacts the breweryDB API to get breweries in the area.
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
                response = EXCEPTION_MSG_2 + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith(START_ERROR)) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            mListener.onMainPageBrewTourFragmentInteraction(result);
        }
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     * */

    public class GetBeerListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 1) {
                throw new IllegalArgumentException(EXCEPTION_MSG);
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(strings[0]);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
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

        @Override
        protected void onPostExecute(String result) {
            mListener.onMainPageBeerListFragmentInteraction(result);
        }

    }

}