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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ArrayList<TopBrew> brews = new ArrayList<>();
    private ArrayList<String> beerNames = new ArrayList<>();
    /** Exception message for too few or too many args*/
    private final String EXCEPTION_MSG = "Three String arguments required.";
    /** Start of the message to notify the user of connection failure.*/
    private final String EXCEPTION_MSG_2 = "Unable to connect, Reason: ";
    private AutoCompleteTextView mAutoCompleteTextView;
    private static final String BEERLIST_PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/beerList";
    private static final String SUGGESTIONS_PARTIAL_URL = "http://api.brewerydb.com/v2/beers/" +
            "?key=b5a1363a472d95fdab32ea49a2c3eb3f&name=";
    private String mUsername;
    private BeerListRecViewAdapter mAdapter;
    private AsyncTask<String, Void, String> mGetBeersTask;
    private OnFragmentInteractionListener mListener;
    /** Required empty public constructor*/
    public BeerListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beer_list, container, false);
        Button b = (Button) v.findViewById(R.id.add_beer);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        Bundle bundle = getArguments();
        if (bundle != null) {

            try {
                String st = getArguments().getString(KEY);
                JSONObject jsonO = new JSONObject(st);
                int num = 0;
                num = jsonO.getInt("totalResults");
                if (jsonO.getString("status").toString().equals("success") && num != 0) {
                    JSONArray data = jsonO.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        //TopBrew brew = TopBrew.create(data.getJSONObject(i));
                        //brews.add(brew);
                        beerNames.add((String)(data.getJSONObject(i)).get("name"));
                    }
                } else {
                    Toast.makeText(getActivity(), "No beer data to show", Toast
                            .LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        Button b = (Button) getActivity().findViewById(R.id.add_beer);
        b.setOnClickListener(this);
        mAutoCompleteTextView = (AutoCompleteTextView) getActivity().findViewById(R.id.
                auto_complete_beers_text);

        //this won't be used
        //String[] beerNames = new String[10];
/*        for (int i = 0; i < beerNames.length; i++) {
            beerNames[i] = "beer" + (i + 1);
        }*/
/*        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout
                .simple_dropdown_item_1line, beerNames);
        mAutoCompleteTextView.setAdapter(adapter);*/


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string
                .login_prefs),
                Context.MODE_PRIVATE);
        mUsername = sharedPreferences.getString(getString(R.string.usernamePrefs), "");
        mGetBeersTask = new GetBeerListTask();
        mGetBeersTask.execute(BEERLIST_PARTIAL_URL);
//        mAdapter = new BeerListRecViewAdapter(getArguments().get(BeerListFragment.KEY).toString(),
//                mUsername);


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



//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener.onBackPressedAction();
//    }
    @Override
    public void onClick(View v) {
        String beerName = mAutoCompleteTextView.getText().toString();
        if (beerName.length() == 0) {
            mAutoCompleteTextView.setError("Search field cannot be empty!");
            // TODO: Call the api and search for beerName
        } else {
            // Delete new lines entered by the stupid user.
            if (beerName.contains("\n")) beerName = beerName.substring(0, beerName.indexOf("\n"));
            //set below block aside for later

/*            if (true*//*If beerName is in the API*//*) {
                AsyncTask<String, Void, String> task;
                task = new AddBeerToDBTask();
                task.execute(BEERLIST_PARTIAL_URL, beerName, "S", "0", "labelLink", "brewery",
                        "1.0", "1.0", "description", "notes", "1");
                mGetBeersTask = new GetBeerListTask();
                mGetBeersTask.execute(BEERLIST_PARTIAL_URL);
            } else {
                mAutoCompleteTextView.setError(beerName + " isn't a recognized beer.");
            }*/

            AsyncTask<String, Void, String> task;
            task = new GetSuggestionsTask();
            task.execute(SUGGESTIONS_PARTIAL_URL, "*" + beerName + "*");

        }
    }


    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class AddBeerToDBTask extends AsyncTask<String, Void, String> {


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

        }
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class GetBeerListTask extends AsyncTask<String, Void, String> {

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
            TextView textView = (TextView) getActivity().findViewById(R.id.add_beer_suggestion);
            if (!result.equals("false")) {
                textView.setVisibility(View.GONE);
                if (mAdapter == null) {
                    RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id
                            .recycler_view_beer);
                    recyclerView.setLayoutManager(new LinearLayoutManager(new Activity()));
                    mAdapter = new BeerListRecViewAdapter(result, mUsername);
                    recyclerView.setAdapter(mAdapter);
                    ItemTouchHelper.Callback callback = new ItemTouchHelperSimpleCallback(mAdapter);
                    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                    touchHelper.attachToRecyclerView(recyclerView);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.populateList(result);
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * Does a partial name search against the BreweryDB list of beers, using wild cards
     * string[0] must be base URL and string [1] must be the keyword being used for search
     *
     */
    private class GetSuggestionsTask extends AsyncTask<String, Void, String> {
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
        //void onBackPressedAction();
    }
}
