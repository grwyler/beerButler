/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * The initially loaded screen for new users. The related XML file contains the Login and
 * Register buttons.
 */
public class LoginSelectionFragment extends Fragment {

    /** The main image loaded on start*/
    private ImageView mCurrentImage;
    /** The motto image loaded on start*/
    private ImageView mMottoImage;

    /**
     * Required empty public constructor.
     */
    public LoginSelectionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_selection, container, false);
        mCurrentImage = (ImageView) v.findViewById(R.id.imageView);
        mMottoImage = (ImageView) v.findViewById(R.id.imageView2);
        if (this.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE) {
            mCurrentImage.setImageResource(R.drawable.beer_landscape);
            mMottoImage.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mCurrentImage.setImageResource(R.drawable.beer_landscape);
            mMottoImage.setVisibility(View.GONE);
        } else {
            mMottoImage.setVisibility(View.VISIBLE);
            mCurrentImage.setImageResource(R.drawable.beer);
        }
    }


}
