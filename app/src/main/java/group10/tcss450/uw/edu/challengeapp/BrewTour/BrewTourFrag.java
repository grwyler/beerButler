package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about breweries in the area. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BrewTourFrag extends Fragment {

    // Required empty public constructor
    public BrewTourFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_brew_tour, container, false);
        ImageView iv = (ImageView) v.findViewById(R.id.brew_pic);
        iv.setImageResource(R.drawable.stout);

        return v;
    }

}
