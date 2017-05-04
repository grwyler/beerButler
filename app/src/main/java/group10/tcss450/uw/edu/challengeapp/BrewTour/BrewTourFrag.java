package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import group10.tcss450.uw.edu.challengeapp.Adapter.RecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A fragment that shows data about breweries in the area. The data is displayed in CardViews to
 * make everything consistent across the application.
 */
public class BrewTourFrag extends Fragment implements View.OnClickListener{
    public static final String KEY = "I love beer!";

    /** The recycler view used to cycle through the card views.*/
    private RecyclerView mRecyclerView;
    /** The requisite adapter for recycler view*/
    private RecyclerView.Adapter mAdapter;
    /** The requisite layout manager*/
    private RecyclerView.LayoutManager mLayoutManager;

    /** Required empty public constructor */
    public BrewTourFrag() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_brew);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(new Activity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        BreweryData[] myDataset = new BreweryData[1];
        Bundle b = getArguments();
        if (b != null) {
            try {
                String st = getArguments().getString(KEY);
                System.out.print(st);
                JSONObject jsonO = new JSONObject(st);

                if (jsonO.getInt("error_code") == 0) {
                    JSONObject response = jsonO.getJSONObject("response");
                    JSONObject show = response.getJSONArray("data").getJSONObject(0);
                    BreweryData s = new BreweryData().set(show);
                    myDataset[0] = s;
//                    setData(s);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdapter = new RecViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_brew_tour, container, false);
//        ImageView iv = (ImageView) v.findViewById(R.id.brew_pic);
//        iv.setImageResource(R.drawable.stout);
//        Button b = (Button) getActivity().findViewById(R.id.nav_button);
//        b.setOnClickListener(this);
        return v;
    }

//    private void setData(BreweryData data) {
//        ImageView iv = (ImageView) getActivity().findViewById(R.id.brew_pic);
//        iv.setImageResource(R.drawable.stout);
//        TextView tv = (TextView) getActivity().findViewById(R.id.brewery_name);
//        tv.setText("Brewery: " + data.getName());
//        tv = (TextView) getActivity().findViewById(R.id.dist);
//        tv.setText("Distance: " + data.getDistance());
//        tv = (TextView) getActivity().findViewById(R.id.hours);
//        tv.setText("Rating: " + data.getHours());
//    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {

    }
}
