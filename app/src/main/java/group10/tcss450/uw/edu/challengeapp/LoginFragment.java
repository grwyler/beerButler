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
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This fragment handles login input. If the user successfully logs in the fragment
 * interaction listener calls back to main activity and loads the main page.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    /** The first part of the URL used for loading the database. */
    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/challenge";
    /** The fragment interaction listener used to communicate with the main activity.*/
    private OnFragmentInteractionListener mListener;
    /**
     * The username is  set so it can be used later to add beer information to the database
     * by user.
     */
    private static String mUsername;

    /**
     * Required empty public constructor
     */
    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button b = (Button) v.findViewById(R.id.signInButton);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.registerFromLoginButton);
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
        if (view.getId() == R.id.signInButton) {
            AsyncTask<String, Void, String> task;
            View parent = (View) view.getParent();
            EditText userName = (EditText) parent.findViewById(R.id.userNameText);
            EditText password = (EditText) parent.findViewById(R.id.passwordText);
            String usr, pwd;
            usr = userName.getText().toString();
            pwd = password.getText().toString();
            task = new GetWebServiceTask();
            mUsername = usr;
            task.execute(PARTIAL_URL, usr, pwd);
        } else if (view.getId() == R.id.registerFromLoginButton) {
            mListener.onLoginRegisterButtonInteraction();
        }
    }

    public static String getmUsername() {
        return mUsername;
    }

    /**
     * An interface for the activity to implement to facilitate inter-fragment communication.
     */
    interface OnFragmentInteractionListener {
        /**
         * Used to notify the activity that the sign-in was successful.
         * @param json The message to send to the activity.
         */
        void onLoginFragmentInteraction(String json);
        void onLoginRegisterButtonInteraction();
    }

//    /**
//     * Helper method that warns the user if incorrect parameters have been entered to the
//     * login fields.
//     *
//     * @param userName the User name edit text.
//     * @param password the Password edit text.
//     * @return true if the user entered something into the fields false otherwise.
//     */
//    private boolean warnUser(EditText userName, EditText password) {
//        boolean cont = true;
//        if(userName.getText().toString().length() <= 2) {
//            userName.setHintTextColor(Color.RED);
//            userName.setError("User name must be more than 2 characters");
//            cont = false;
//        }
//
//        if(password.getText().toString().length() < 5) {
//            password.setHintTextColor(Color.RED);
//            password.setError("Password must be more than 5 characters");
//            cont = false;
//        }
//        return cont;
//    }


    /**
     * A local AsyncTask class used to access the database and communicate back to the
     * activity.
     */
    private class GetWebServiceTask extends AsyncTask<String, Void, String> {

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
            if (strings.length != 3) {
                throw new IllegalArgumentException(EXCEPTION_MSG);
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String args = "?name=" + strings[1] + "&pswd=" + strings[2];
            System.out.println(url + "_get.php" + args);
            try {
                URL urlObject = new URL(url + "_get.php" + args);
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
            // Something wrong with the network or the URL
            if (result.startsWith(START_ERROR)) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            } else if(result.startsWith("Successfully")) {
                mListener.onLoginFragmentInteraction(result);
            } else {
                Toast.makeText(getActivity(), TOAST_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
