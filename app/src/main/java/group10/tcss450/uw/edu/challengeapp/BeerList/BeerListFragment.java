/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */
package group10.tcss450.uw.edu.challengeapp.BeerList;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group10.tcss450.uw.edu.challengeapp.Adapter.BeerListRecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.Adapter.ItemTouchHelperSimpleCallback;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about beer unique to each user. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BeerListFragment extends Fragment implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, SearchView.OnFocusChangeListener {
    /** Exception message for too few or too many args*/
    private final String EXCEPTION_MSG = "Three String arguments required.";
    /** Start of the message to notify the user of connection failure.*/
    private final String EXCEPTION_MSG_2 = "Unable to connect, Reason: ";
    private SearchView mSearchView;
    private FloatingActionButton mFab;
    private static final String BEERLIST_PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/beerList";
    private static final String SUGGESTIONS_PARTIAL_URL = "http://api.brewerydb.com/v2/beers/" +
            "?key=b5a1363a472d95fdab32ea49a2c3eb3f&name=";
    private String mUsername;
    private OnFragmentInteractionListener mListener;
    /** Required empty public constructor*/
    public BeerListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beer_list, container, false);
        mSearchView = (SearchView) v.findViewById(R.id.search_view);
        mSearchView.setOnFocusChangeListener(this);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSearchView.setVisibility(View.VISIBLE);
                mFab.setVisibility(View.INVISIBLE);
                mSearchView.requestFocus();
                mSearchView.setIconified(false);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string
                .login_prefs),
                Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString(getString(R.string.usernamePrefs), "");
        AsyncTask<String, Void, String> mGetBeersTask = new GetBeerListTask();
        mGetBeersTask.execute(BEERLIST_PARTIAL_URL);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() == 0) {
            Toast.makeText(getActivity(), "Search field cannot be empty!",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Delete new lines entered by the stupid user.
            if (query.contains("\n")) query = query.substring(0, query.indexOf("\n"));
            query = query.replace(" ", "%20");
            AsyncTask<String, Void, String> task;
            task = new GetSuggestionsTask();
            task.execute(SUGGESTIONS_PARTIAL_URL, "*" + query + "*");
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        mSearchView.setVisibility(View.GONE);
        mFab.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {}

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class GetBeerListTask extends AsyncTask<String, Void, String> {

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
                URL urlObject = new URL(strings[0] + "_get.php" + "?name=" + mUsername);
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
            TextView textView = null;
            if (getActivity() != null) {
                textView = (TextView) getActivity().findViewById(R.id.add_beer_suggestion);
            }
            if (!result.equals("0") && getActivity() != null) {
                if (textView != null) textView.setVisibility(View.GONE);
                RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id
                        .recycler_view_beer);
                recyclerView.setLayoutManager(new LinearLayoutManager(new Activity()));
                BeerListRecViewAdapter mAdapter = new BeerListRecViewAdapter(result, mUsername);
                recyclerView.setAdapter(mAdapter);
                ItemTouchHelper.Callback callback = new ItemTouchHelperSimpleCallback(mAdapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(recyclerView);
                mAdapter.notifyDataSetChanged();
            } else {
                if (textView != null) textView.setVisibility(View.VISIBLE);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    /**
     * Does a partial name search against the BreweryDB list of beers, using wild cards
     * string[0] must be base URL and string [1] must be the keyword being used for search
     *
     */
    private class GetSuggestionsTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException(EXCEPTION_MSG);
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
                if (urlConnection != null) urlConnection.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            mListener.onBeerListAddBeerButtonPressed(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * An interface for the activity to implement to facilitate inter-fragment communication.
     * This is meant to lead into the SuggestedListFragment
     * raw result of the API call is passed to the new fragment for processing
     */
    public interface OnFragmentInteractionListener {
        /**
         * Used to notify the activity that the sign-in was successful.
         */
        void onBeerListAddBeerButtonPressed(String json);
    }
}
