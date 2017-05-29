package group10.tcss450.uw.edu.challengeapp.BrewTour;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import group10.tcss450.uw.edu.challengeapp.Adapter.BrewTourRecViewAdapter;
import group10.tcss450.uw.edu.challengeapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    MapView mMap;
    private GoogleMap mGoogleMap;
    private double mLongitude;
    private double mLatitude;
    private String mName;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMap = (MapView) v.findViewById(R.id.map_fragment);
        mMap.onCreate(savedInstanceState);
        mMap.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                Log.d("TEST", "Map is ready!");
                if (getArguments() != null) {
                    mLongitude = getArguments().getDouble(BrewTourRecViewAdapter.LNG_KEY);
                    mLatitude = getArguments().getDouble(BrewTourRecViewAdapter.LAT_KEY);
                    mName = getArguments().getString(BrewTourRecViewAdapter.NAME_KEY);
                }
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                // create marker
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(mLatitude, mLongitude)).title(mName);

                // Changing marker icon
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                // adding marker
                mGoogleMap.addMarker(marker);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(mLatitude, mLongitude)).zoom(12).build();
                mGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

}
