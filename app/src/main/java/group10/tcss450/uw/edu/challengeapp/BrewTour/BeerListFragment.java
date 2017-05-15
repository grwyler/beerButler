/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */
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
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.Adapter.ItemTouchHelperSimpleCallback;
import group10.tcss450.uw.edu.challengeapp.Adapter.RecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about beer unique to each user. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BeerListFragment extends Fragment {
    public static final String KEY = "I love beer!";
    private ArrayList<TopBrewery> breweries = new ArrayList<>();

    /** Required empty public constructor*/
    public BeerListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beer_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_brew);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(new Activity());
        recyclerView.setLayoutManager(layoutManager);



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
//                     * ToDo need a code branch to handle zero result responses
//                     */
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RecyclerView.Adapter adapter = new RecViewAdapter(breweries);
//            recyclerView.setAdapter(adapter);
//
//            ItemTouchHelper.Callback callback =
//                    new ItemTouchHelperSimpleCallback((RecViewAdapter)adapter);
//            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//            touchHelper.attachToRecyclerView(recyclerView);
//        }
    }

}
