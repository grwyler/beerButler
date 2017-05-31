/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.app.ProgressDialog;
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
    /** The start of a string returned if there was an error connecting to the DB.*/
    private final String START_ERROR = "Unable to";
    /** Exception message for too few or too many args*/
    private final String EXCEPTION_MSG = "One String arguments required.";
    /** Start of the message to notify the user of connection failure.*/
    private final String EXCEPTION_MSG_2 = "Unable to connect, Reason: ";

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
                    String lat = mLatitude;
                    String lng = mLongitude;
                    if (lat == null || lng == null) {
                        lat = "47.2529";
                        lng = "-122.4443";
                        Toast.makeText(getActivity(),
                                "Cannot find your location! " +
                                        "\n Please make sure your phones " +
                                        "location settings are turned on.",
                                Toast.LENGTH_LONG).show();
                    }
                    Log.d("MainPageFragemnt", lat + ", " + lng);
                    task = new BrewTourWebServiceTask();
                    task.execute(PARTIAL_URL, "lat=" + lat + "&lng=" + lng);
                    break;
                case R.id.beer_list_button:
                    mListener.onMainPageBeerListButtonPressed();
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
        void onMainPageBrewTourButtonPressed(String json);

        /**
         *  Used to notify the activity that the beer list button was pressed.
         */
        void onMainPageBeerListButtonPressed();
    }

    /**
     * Asyncrinous task that contacts the breweryDB API to get breweries in the area.
     */
    private class BrewTourWebServiceTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            mListener.onMainPageBrewTourButtonPressed(result);
        }
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     * */

    public class GetBeerListTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            mListener.onMainPageBeerListButtonPressed();
        }

    }

}