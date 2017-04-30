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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment implements View.OnClickListener{

    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/challenge";
    private OnFragmentInteractionListener mListener;


    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        Button b = (Button) v.findViewById(R.id.signInButton);
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
        AsyncTask<String, Void, String> task;
        View parent = (View) view.getParent();
        EditText userName = (EditText) parent.findViewById(R.id.userNameText);
        EditText password = (EditText) parent.findViewById(R.id.passwordText);
        String usr, pwd;
        if(parent != null) {
            if(warnUser(userName, password)) {
                usr = userName.getText().toString();
                pwd = password.getText().toString();
                task = new GetWebServiceTask();
                task.execute(PARTIAL_URL, usr, pwd);
            }
        }

    }

    private boolean warnUser(EditText userName, EditText password) {
        boolean cont = true;
        if(userName.getText().toString().length() <= 2) {
            userName.setHintTextColor(Color.RED);
            userName.setError("User name must be more than 2 characters");
            cont = false;
        }

        if(password.getText().toString().length() < 5) {
            password.setHintTextColor(Color.RED);
            password.setError("Password must be more than 5 characters");
            cont = false;
        }

        String string = password.getText().toString();
        //enforces alpha numeric entry for passwords using regex.
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        if (!digit.matcher(string).find()){
            password.setHintTextColor(Color.RED);
            password.setError("Password must contain at least one number");
            cont = false;
        }

        if (!letter.matcher(string).find()){
            password.setHintTextColor(Color.RED);
            password.setError("Password must contain at least one letter");
            cont = false;
        }

        return cont;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String message);
    }

    private class GetWebServiceTask extends AsyncTask<String, Void, String> {

        private final String SERVICE = "_get.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String args = "?name=" + strings[1] + "&pswd=" + strings[2];
            System.out.println(url + SERVICE + args);
            try {
                URL urlObject = new URL(url + SERVICE + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: " + e.getMessage();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL
            String success = result;
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                return;
            } else if(result.startsWith("Successfully")) {
                mListener.onFragmentInteraction(success);
            } else {
                Toast.makeText(getActivity(), "Not a recognized account", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
