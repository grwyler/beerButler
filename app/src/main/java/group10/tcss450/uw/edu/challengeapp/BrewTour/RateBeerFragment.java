package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import group10.tcss450.uw.edu.challengeapp.LoginFragment;
import group10.tcss450.uw.edu.challengeapp.R;
import group10.tcss450.uw.edu.challengeapp.UserProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateBeerFragment extends Fragment implements View.OnClickListener{

    /** The first part of the URL used for loading the database. */
    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/challenge";

    public OnFragmentInteractionListener mListener;
    public RateBeerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rate_beer, container, false);
        Button b = (Button) v.findViewById(R.id.submit_button);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_button) {
            //TODO This needs to be changed to whatever the beer on the cardview is.
            String beer = "Irish Death";
            AsyncTask<String, Void, String> task;
            View parent = (View) view.getParent();
            EditText rating = (EditText) parent.findViewById(R.id.rateBeerET);
            EditText notes = (EditText) parent.findViewById(R.id.notesET);
            String rate, note;
            rate = rating.getText().toString();
            note = notes.getText().toString();
            task = new RateBeerFragment.RateBeerTask();
            //task.execute(PARTIAL_URL, beer, rate, note, LoginFragment.getmUsername());

        }
    }

    public interface OnFragmentInteractionListener {
        void onRateBeerFragmentInteraction(String string);
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class RateBeerTask extends AsyncTask<String, Void, String> {

        /** The start of a string returned if there was an error connecting to the DB.*/
        private final String START_ERROR = "Unable to";
        /** The error message if the user enters wrong data for logging in*/
        private final String TOAST_ERROR = "Not a recognized account, Please register a new user";
        /** Exception message for too few or too many args*/
        private final String EXCEPTION_MSG = "Three String arguments required.";
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
                URL urlObject = new URL(url + "_rate.php");
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("my_notes", "UTF-8")
                        + "=" + URLEncoder.encode(strings[3], "UTF-8")
                        + "&" + URLEncoder.encode("my_rating", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8")
                        + "&" + URLEncoder.encode("my_beer", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("my_name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[4], "UTF-8");
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
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            } else if(result.startsWith("Successfully")) {
               //mListener.onRateBeerFragmentInteraction(result);

            } else {
                Toast.makeText(getActivity(), TOAST_ERROR, Toast
                        .LENGTH_SHORT).show();
            }
        }
    }
}
