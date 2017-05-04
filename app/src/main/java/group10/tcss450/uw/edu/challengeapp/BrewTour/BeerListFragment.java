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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about beer unique to each user. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BeerListFragment extends Fragment {

    /** The recycler view used to cycle through the card views.*/
    private RecyclerView mRecyclerView;
    /** The requisite adapter for recycler view*/
    private RecyclerView.Adapter mAdapter;
    /** The requisite layout manager*/
    private RecyclerView.LayoutManager mLayoutManager;

    /** Required empty public constructor*/
    public BeerListFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_beer);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(new Activity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        String[] myDataset = new String[0];
//        mAdapter = new RecViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beer_list, container, false);
        ImageView iv = (ImageView) v.findViewById(R.id.brew_pic);
        iv.setImageResource(R.drawable.stout);

        return v;
    }

}
