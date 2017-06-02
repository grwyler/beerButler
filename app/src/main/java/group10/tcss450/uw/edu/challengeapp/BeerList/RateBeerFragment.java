package group10.tcss450.uw.edu.challengeapp.BeerList;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import group10.tcss450.uw.edu.challengeapp.Adapter.BeerListRecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateBeerFragment extends Fragment implements View.OnClickListener{

    /** The first part of the URL used for loading the database. */
    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/";
    public OnFragmentInteractionListener mListener;

    private String mNotes;

    private RatingBar mRatingBar;

    public RateBeerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rate_beer, container, false);
        TextView tv = (TextView) v.findViewById(R.id.beer_name_TV);
        Button b = (Button) v.findViewById(R.id.submit_rating_button);
        String mBeerName = getArguments().getString(BeerListRecViewAdapter.BEERNAME_KEY);
        String mDescription = getArguments().getString("Description");
        String argRating = getArguments().getString(BeerListRecViewAdapter.RATING_KEY);
        EditText noteET = (EditText) v.findViewById(R.id.notesET);
        mNotes = noteET.getText().toString();
        if (mNotes.equals("")) mNotes = getArguments().getString(BeerListRecViewAdapter.NOTES_KEY);
        if (mDescription != null) if (mDescription.equals("0")) mDescription = "";
        mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        noteET.setText(mNotes);
        float mRating = mRatingBar.getRating();
        if (argRating != null) {
            if (mRating == 0 && argRating.length() > 0) mRating = Float.valueOf(argRating);
        }
        mRatingBar.setRating(mRating);
        tv.setText(mBeerName);
        tv = (TextView) v.findViewById(R.id.description);
        tv.setText(mDescription);
        b.setOnClickListener(this);
        return v;
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

    /**
     * On submit button click start a task that posts a rating and notes to the database.
     * The name of the current user and the beer are added programmatically.
     * @param view the submit button.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_rating_button) {
            AsyncTask<String, Void, String> task;
            View parent = (View) view.getParent();
            EditText notes = (EditText) parent.findViewById(R.id.notesET);
            String beer, username, oldRating;
            mNotes = notes.getText().toString();
            task = new RateBeerFragment.RateBeerTask();
            if (getArguments() != null) {
                beer = getArguments().getString(BeerListRecViewAdapter.BEERNAME_KEY);
                username = getArguments().getString(BeerListRecViewAdapter.USERNAME_KEY);
                task.execute(PARTIAL_URL, beer, mRatingBar.getRating() + "", mNotes, username);
            } else {
                Toast.makeText(getActivity(), "You messed it up you drunk bastard",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onRateBeerFragmentInteraction();
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class RateBeerTask extends AsyncTask<String, Void, String> {

        /** The start of a string returned if there was an error connecting to the DB.*/
        private final String START_ERROR = "Unable to";
        /** Exception message for too few or too many args*/
        private final String EXCEPTION_MSG = "Five String arguments required.";
        /** Start of the message to notify the user of connection failure.*/
        private final String EXCEPTION_MSG_2 = "Unable to connect, Reason: ";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 5) {
                throw new IllegalArgumentException(EXCEPTION_MSG);
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];

            try {
                String data = URLEncoder.encode("my_notes", "UTF-8")
                        + "=" + URLEncoder.encode(strings[3], "UTF-8")
                        + "&" + URLEncoder.encode("my_rating", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8")
                        + "&" + URLEncoder.encode("my_beer", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("my_name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[4], "UTF-8");
                URL urlObject = new URL(url + "rate.php?" + data);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

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
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith(START_ERROR)) {
//                Log.d("TEST!!!! Line 166", result);
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            } else if(result.startsWith("UPDATE")) {
//                Log.d("RateBeerFragment", "Success!!!!!!!!!!");
                mListener.onRateBeerFragmentInteraction();
            } else {
//                Log.d("TEST!!!! Line 172", result);
                Toast.makeText(getActivity(), result, Toast
                        .LENGTH_LONG).show();

            }
        }
    }
}
