package group10.tcss450.uw.edu.challengeapp.SuggestionsList;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;

import group10.tcss450.uw.edu.challengeapp.Adapter.SuggestionsListRecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.BeerList.TopBrew;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionsListFragment extends Fragment {
    public static final String KEY = "I love beer!";
    private ArrayList<TopBrew> brews = new ArrayList<>();
    private SuggestionsListRecViewAdapter mAdapter;

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
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_brew);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(new Activity());
        recyclerView.setLayoutManager(layoutManager);

        Bundle b = getArguments();
            if (b != null) {
                try {
                String st = getArguments().getString(KEY);
                JSONObject jsonO = new JSONObject(st);
                int num;
                num  = jsonO.getInt("totalResults");
                    if (jsonO.getString("status").equals("success") && num != 0) {
                        JSONArray data = jsonO.getJSONArray("data");
                        for(int i=0; i<data.length(); i++){
                            TopBrew brew = TopBrew.create(data.getJSONObject(i));
                            brews.add(brew);
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
                mAdapter = new SuggestionsListRecViewAdapter(brews);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter = new SuggestionsListRecViewAdapter(brews);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
