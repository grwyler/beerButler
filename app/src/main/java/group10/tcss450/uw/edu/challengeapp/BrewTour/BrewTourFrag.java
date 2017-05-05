package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.FourthFragment;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about breweries in the area. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BrewTourFrag extends Fragment implements View.OnClickListener{
    public static final String KEY = "I love beer!";
    private ArrayList<TopBrewery> breweries = new ArrayList<TopBrewery>();

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
//        Button b = (Button) getActivity().findViewById(R.id.nav_button);
//        b.setOnClickListener(this);
        return v;
    }

    private void setData(BreweryData data) {
        ImageView iv = (ImageView) getActivity().findViewById(R.id.brew_pic);
        iv.setImageResource(R.drawable.stout);
        TextView tv = (TextView) getActivity().findViewById(R.id.brewery_name);
        tv.setText("Brewery: " + data.getName());
        tv = (TextView) getActivity().findViewById(R.id.dist);
        tv.setText("Distance: " + data.getDistance());
        tv = (TextView) getActivity().findViewById(R.id.hours);
        tv.setText("Rating: " + data.getHours());
    }

    /**
     *
     * Parsing the JSON response to put into an array of Breweries
     */
    @Override
    public void onStart() {
        super.onStart();
        Bundle b = getArguments();
        if (b != null) {

            try {
                String st = getArguments().getString(KEY);
                System.out.print(st);
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
        }
    }

    @Override
    public void onClick(View v) {

    }
}
