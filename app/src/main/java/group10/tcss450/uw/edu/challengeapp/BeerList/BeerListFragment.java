/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */
package group10.tcss450.uw.edu.challengeapp.BeerList;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.Adapter.BeerListRecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.Adapter.ItemTouchHelperSimpleCallback;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about beer unique to each user. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BeerListFragment extends Fragment implements View.OnClickListener {
    public static final String KEY = "I love beer!";
    private AutoCompleteTextView mAutoCompleteTextView;
    private OnFragmentInteractionListener mListener;
    private static final String BEERLIST_PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/beerList";
    private String mUsername;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BeerListRecViewAdapter mAdapter;
    private ArrayList<Beer> mBeerList;

    /** Required empty public constructor*/
    public BeerListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beer_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_beer);
        mLayoutManager = new LinearLayoutManager(new Activity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Button b = (Button) getActivity().findViewById(R.id.add_beer);
        b.setOnClickListener(this);

        mAutoCompleteTextView = (AutoCompleteTextView) getActivity().findViewById(R.id.
                auto_complete_beers_text);

        //TODO: populate this list with all beer names from the API
        String[] beerNames = new String[10];
        for (int i = 0; i < beerNames.length; i++) beerNames[i] = "beer" + (i + 1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_dropdown_item_1line, beerNames);
        mAutoCompleteTextView.setAdapter(adapter);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string
                .login_prefs), Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString(getString(R.string.usernamePrefs), "");
        mAdapter = new BeerListRecViewAdapter(getArguments().get(BeerListFragment.KEY).toString(),
                mUsername);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperSimpleCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

//        Bundle b = getArguments();
//        if (b != null) {
//
//            try {
//                String st = getArguments().getString(KEY);
//                JSONObject jsonO = new JSONObject(st);
//                int num = 0;
//                num  = jsonO.getInt("totalResults");
//                if (jsonO.getString("status").toString().equals("success") && num != 0) {
//                    JSONArray data = jsonO.getJSONArray("data");
//
//                    for(int i=0; i<data.length(); i++){
//                        TopBrewery brewery = TopBrewery.create(data.getJSONObject(i));
//                        breweries.add(brewery);
//                    }
//                }
//                else {
//                    /**
//                     * ToDo need a code branch to handle zero result responses
//                     */
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RecyclerView.Adapter adapter = new BrewTourRecViewAdapter(breweries);
//            recyclerView.setAdapter(adapter);
//
//            ItemTouchHelper.Callback callback =
//                    new ItemTouchHelperSimpleCallback((BrewTourRecViewAdapter)adapter);
//            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//            touchHelper.attachToRecyclerView(recyclerView);
//        }
    }

    /**
     * An interface for the activity to implement to facilitate inter-fragment communication.
     */
    public interface OnFragmentInteractionListener {
        void onRegisterFragmentInteraction(String message);
    }

    @Override
    public void onClick(View v) {
        String beerName = mAutoCompleteTextView.getText().toString();
        if (beerName.length() == 0) {
            mAutoCompleteTextView.setError("Search field cannot be empty!");
            // TODO: Call the api and search for beerName
        } else {
            if (true/*If beerName is in the API*/) {
                // TODO: Add the new beer to the BeerListRecViewAdapter's list using a setter method
                AsyncTask<String, Void, String> task;
                Beer beer = new Beer(beerName, "s", false, "labelLink", "brewery", 1.0, 1.0,
                        "description", "notes", 1);
                task = new AddBeerToDBTask();
                task.execute(BEERLIST_PARTIAL_URL, beerName, "s", "0", "labelLink", "brewery",
                        "1.0", "1.0", "description", "notes", "1");
                mAdapter.addBeer(beer);
            } else {
                mAutoCompleteTextView.setError(beerName + " isn't a recognized beer.");
            }
        }
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class AddBeerToDBTask extends AsyncTask<String, Void, String> {

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
            if (strings.length != 11) {
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
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = eURL("name", "=") + eURL(strings[1], "&") + eURL("style", "=") +
                        eURL(strings[2], "&") + eURL("isOrganic", "=") + eURL(strings[3], "&") +
                        eURL("labelLink", "=") + eURL(strings[4], "&") + eURL("brewery", "=")
                        + eURL(strings[5], "&") + eURL("abv", "=") + eURL(strings[6], "&") +
                        eURL("ibu", "=") + eURL(strings[7], "&") + eURL("description", "=")
                        + eURL(strings[8], "&") + eURL("notes", "=") + eURL(strings[9], "&") +
                        eURL("rating", "=")  + eURL(strings[10], "&") + eURL("username", "=" +
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
//            System.out.println(result);
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
