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
public class FourthFragment extends Fragment implements View.OnClickListener {

    private static final String PARTIAL_URL = "http://api.brewerydb.com/v2/search/geo/point" +
            "?key=b5a1363a472d95fdab32ea49a2c3eb3f&";
    private SecondFragment.OnFragmentInteractionListener mListener;

    public FourthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fourth, container, false);
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
                case R.id.brew_tour_button:
                    String lat = "47.255053";
                    String lng = "122.445805";
                    task = new BrewTourWebServiceTask();
                    task.execute(PARTIAL_URL, "lat=" + lat + "&lng=" + lng);
                    break;
                default:
                    Toast.makeText(getActivity(),
                            "something went horibly wrong!",
                            Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SecondFragment.OnFragmentInteractionListener) {
            mListener = (SecondFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Get this class.
     *
     * @return this , This class
     */
    public Fragment getThisClass() {
        return this;
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
            mListener.onFragmentInteraction(getThisClass(), result);
        }
    }
}