/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.content.Context;
import android.graphics.Color;
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


/**
 * This fragment handles new registration. Upon successful registration the fragment
 * interaction listener tells the main activity to load the main page.
 */
public class ThirdFragment extends Fragment implements View.OnClickListener {

    /** The start for all error messages for lack of filled fields.*/
    private static final String ERROR_MESSAGE = "Please enter a ";
    /** End error message if the user didn't enter the verify password.*/
    private static final String VERIFY_ERROR = "Please re-enter the password.";
    /** Used when the two passwords do not match.*/
    private static final String MATCH_ERROR = "The passwords do not match.";
    /** Start of the URL to the data base*/
    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/challenge";
    /** The fragment interaction listener used to communicate with the main activity.*/
    private OnFragmentInteractionListener mListener;

    /** Required empty public constructor. */
    public ThirdFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        Button b = (Button) v.findViewById(R.id.registerButton);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        AsyncTask<String, Void, String> task = null;
        View parent = (View) view.getParent();
        EditText userName = (EditText) parent.findViewById(R.id.rUserNameText);
        EditText password = (EditText) parent.findViewById(R.id.rPasswordText);
        EditText vPassword = (EditText) parent.findViewById(R.id.rPasswordVText);
        String usr, pwd;
        if(parent != null) {
            if(warnUser(userName, password, vPassword)) {
                usr = userName.getText().toString();
                pwd = password.getText().toString();
                task = new PostWebServiceTask();
                task.execute(PARTIAL_URL, usr, pwd);
            }
        }
    }

    /**
     * Helper method that warns the user if incorrect parameters have been entered to the
     * login fields.
     *
     * @param userName the User name edit text.
     * @param password the Password edit text.
     * @param vPassword the Password verifier edit text.
     * @return true if the user entered something into the fields false otherwise.
     */
    private boolean warnUser(EditText userName, EditText password, EditText vPassword) {
        boolean cont = false;
        String usrString = userName.getText().toString();
        String pswrdString = password.getText().toString();
        String vPswrdString = vPassword.getText().toString();
        if(usrString.length() == 0) {
            userName.setHintTextColor(Color.RED);
            userName.setError(ERROR_MESSAGE + "user name.");
        } else if(pswrdString.length() == 0 &&
                vPassword.getText().length() == 0) {
            password.setHintTextColor(Color.RED);
            password.setError(ERROR_MESSAGE + "password.");
            vPassword.setHintTextColor(Color.RED);
            vPassword.setError(VERIFY_ERROR);
        } else if(pswrdString.length() == 0) {
            password.setHintTextColor(Color.RED);
            password.setError(ERROR_MESSAGE + "password.");
        } else if(vPswrdString.length() == 0) {
            vPassword.setHintTextColor(Color.RED);
            vPassword.setError(VERIFY_ERROR);
        } else if(!vPswrdString.equals(pswrdString)) {
            vPassword.setHintTextColor(Color.RED);
            vPassword.setError(MATCH_ERROR);
        } else cont = true;
        return cont;
    }

    /**
     * An interface for the activity to implement to facilitate inter-fragment communication.
     */
    public interface OnFragmentInteractionListener {
        /**
         * Used to notify the activity that the registration was successful.
         * @param message The message to send to the activity.
         */
        void onFragmentInteraction(String message);
    }

    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {

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
            if (strings.length != 3) {
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
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(
                        strings[1], "UTF-8") + "&" + URLEncoder.encode("pwd", "UTF-8") + "=" +
                        URLEncoder.encode(strings[2], "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
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
                return;
            } else if(result.startsWith("Successfully")) {
                mListener.onFragmentInteraction(result);
            } else {
                Toast.makeText(getActivity(), TOAST_ERROR, Toast
                        .LENGTH_SHORT).show();
            }
        }
    }
}
