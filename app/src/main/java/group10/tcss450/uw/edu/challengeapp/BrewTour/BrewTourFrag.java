package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.Adapter.BrewTourRecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.Adapter.ItemTouchHelperAdapter;
import group10.tcss450.uw.edu.challengeapp.Adapter.ItemTouchHelperSimpleCallback;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about breweries in the area. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BrewTourFrag extends Fragment {
    public static final String KEY = "I love beer!";
    private ArrayList<TopBrewery> breweries = new ArrayList<>();
    /** The recycler view used to cycle through the card views.*/
    private RecyclerView mRecyclerView;
    /** The requisite adapter for recycler view*/
    private RecyclerView.Adapter mAdapter;
    /** The requisite layout manager*/
    private RecyclerView.LayoutManager mLayoutManager;
    /** The adapter for the ItemTouchHelper*/
    private ItemTouchHelperAdapter mITHAdapter;

    // Required empty public constructor
    public BrewTourFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brew_tour, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_brew);
        mLayoutManager = new LinearLayoutManager(new Activity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Bundle b = getArguments();
        if (b != null) {

            try {
                String st = getArguments().getString(KEY);
                JSONObject jsonO = new JSONObject(st);
                int num = 0;
                num  = jsonO.getInt("totalResults");
                if (jsonO.getString("status").toString().equals("success") && num != 0) {
                    JSONArray data = jsonO.getJSONArray("data");

                    for(int i=0; i<data.length(); i++){
                        TopBrewery brewery = TopBrewery.create(data.getJSONObject(i));
                        breweries.add(brewery);
                    }
                }
                else {
                    /**
                     * ToDo need a code branch to handle zero result responses
                     */
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new BrewTourRecViewAdapter(breweries);
            mRecyclerView.setAdapter(mAdapter);

            ItemTouchHelper.Callback callback =
                    new ItemTouchHelperSimpleCallback((BrewTourRecViewAdapter)mAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);
        }
    }
}
