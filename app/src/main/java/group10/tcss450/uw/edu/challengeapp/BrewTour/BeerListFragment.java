/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */
package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    // Required empty public constructor
    public BeerListFragment() {
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
