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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThirdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment implements View.OnClickListener {

    private static final String ERROR_MESSAGE = "Please enter a ";
    private static final String VERIFY_ERROR = "Please re-enter the password.";
    private static final String PASSWORD = "password.";
    private static final String USER_NAME = "user name.";
    private static final String MATCH_ERROR = "The passwords do not match.";
    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~grwyler/beerButler/challenge";

    private OnFragmentInteractionListener mListener;



    public ThirdFragment() {
        // Required empty public constructor
    }

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

    private boolean warnUser(EditText userName, EditText password, EditText vPassword) {
        boolean cont = false;
        String usrString = userName.getText().toString();
        String pswrdString = password.getText().toString();
        String vPswrdString = vPassword.getText().toString();
        if(usrString.length() == 0) {
            userName.setHintTextColor(Color.RED);
            userName.setError(ERROR_MESSAGE + USER_NAME);
        } else if(pswrdString.length() == 0 &&
                vPassword.getText().length() == 0) {
            password.setHintTextColor(Color.RED);
            password.setError(ERROR_MESSAGE + PASSWORD);
            vPassword.setHintTextColor(Color.RED);
            vPassword.setError(VERIFY_ERROR);
        } else if(pswrdString.length() == 0) {
            password.setHintTextColor(Color.RED);
            password.setError(ERROR_MESSAGE + PASSWORD);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(String message);
    }

    private class PostWebServiceTask extends AsyncTask<String, Void, String> {

        private final String SERVICE = "_post.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Three String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + SERVICE);
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
                response = "Unable to connect, Reason: " + e.getMessage();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                return;
            } else if(result.startsWith("Successfully")) {
                mListener.onFragmentInteraction(result);
            } else {
                Toast.makeText(getActivity(), "That user name is already being used", Toast
                        .LENGTH_SHORT).show();
            }
        }
    }
}
