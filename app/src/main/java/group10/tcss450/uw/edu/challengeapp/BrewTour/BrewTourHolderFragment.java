package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrewTourHolderFragment extends Fragment {

    private static final String COLUMN_COUNT = "number_of_columns";
    private int nNumberOfColums;
    private OnListFragmentInteractionListener mListener;

    public BrewTourHolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew_tour_holder, container, false);
        ImageView iv = (ImageView) view.findViewById(R.id.beer_pic);

        iv.setImageResource(R.drawable.stout);
        TextView tv = (TextView) view.findViewById(R.id.beer_name);
        tv.setText("Beer: Quilter's Irish Death");
        tv = (TextView) view.findViewById(R.id.brewery);
        tv.setText("Brewery: Iron Horse Brewery");
        tv = (TextView) view.findViewById(R.id.abv);
        tv.setText("ABV: 10.1     ");
        tv = (TextView) view.findViewById(R.id.ibu);
        tv.setText("IBUs: 45");
//        // Set the adapter
//        if (view instanceof CardView) {
//            Context context = view.getContext();
//            CardView recyclerView = (CardView) view;
//            if (nNumberOfColums <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, nNumberOfColums));
//            }
//            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(BreweryData.DATA, mListener));
//        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
//            String message = getArguments().getString("Message");
//            String password = getArguments().getString("Password Key");
//            updateContent(message);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String item);
    }

}
