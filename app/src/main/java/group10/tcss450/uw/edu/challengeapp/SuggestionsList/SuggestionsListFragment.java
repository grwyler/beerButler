package group10.tcss450.uw.edu.challengeapp.SuggestionsList;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.Adapter.SuggestionsListRecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.BeerList.Beer;
import group10.tcss450.uw.edu.challengeapp.BeerList.BeerListFragment;
import group10.tcss450.uw.edu.challengeapp.BeerList.TopBrew;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionsListFragment extends Fragment {
    public static final String KEY = "I love beer!";
    private ArrayList<Beer> brews = new ArrayList<>();
    private SuggestionsListRecViewAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    public SuggestionsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestions_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BeerListFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_brew);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(new Activity());
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string
                        .login_prefs),
                Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(getString(R.string.usernamePrefs), "");

        Bundle b = getArguments();
        if (b != null) {
            try {
                String st = getArguments().getString(KEY);
                JSONObject jsonO = new JSONObject(st);
                int num;
                num = jsonO.getInt("totalResults");
                if (jsonO.getString("status").equals("success") && num != 0) {
                    JSONArray data = jsonO.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        TopBrew brew = TopBrew.create(data.getJSONObject(i));
                        brews.add(new Beer(brew));
                    }
                } else {
                    Toast.makeText(getActivity(), "No brew data to show", Toast
                            .LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mAdapter == null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(new Activity()));
            }
            mAdapter = new SuggestionsListRecViewAdapter(brews, username, getContext(),
                    mListener);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
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
        void onBeerSuccessfullyAddedToDB();
    }
}
