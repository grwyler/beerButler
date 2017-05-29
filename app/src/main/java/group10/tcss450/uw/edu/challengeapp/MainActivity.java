/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import group10.tcss450.uw.edu.challengeapp.BeerList.BeerListFragment;
import group10.tcss450.uw.edu.challengeapp.BrewTour.BrewTourFrag;
import group10.tcss450.uw.edu.challengeapp.BeerList.RateBeerFragment;
import group10.tcss450.uw.edu.challengeapp.SuggestionsList.SuggestionsListFragment;


/**
 * The main activity class. Login, registration, and cardview are loaded and managed here.
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        MainPageFragment.OnFragmentInteractionListener,
        RateBeerFragment.OnFragmentInteractionListener,
        BeerListFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    /**
     * The desired interval for location updates. Inexact. Updates may be
     more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will
     never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private SharedPreferences mLoginPreferences;
    private SharedPreferences.Editor mLoginPrefsEditor;
    private boolean mSaveLogin;
    private boolean mMenuItemEnabled;
    private String mUsername;
    public static FragmentManager mFragManager;
    private MainPageFragment mMainPage;
    private GoogleApiClient mGoogleApiClient;

    private static final int MY_PERMISSIONS_LOCATIONS = 814;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPage = new MainPageFragment();
        mMainPage.setArguments(new Bundle());
        setContentView(R.layout.activity_main);
        Fragment fragment;
        mFragManager = getSupportFragmentManager();

    // mPermission = true;
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster
        // interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLoginPreferences = getSharedPreferences(getString(R.string.login_prefs),
                Context.MODE_PRIVATE);
        mLoginPrefsEditor = mLoginPreferences.edit();
        mSaveLogin = mLoginPreferences.getBoolean(getString(R.string.save_login),
                false);
        if (mSaveLogin == true) {
            fragment = mMainPage;
            mMenuItemEnabled = true;
        } else {
            fragment = new LoginSelectionFragment();
            mMenuItemEnabled = false;
        }
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, fragment).commit();
            }
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        }
    }
    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TEST", "True!!!!");
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            Log.d("TEST", "False!!!!");
        }
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activityis in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        //(http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
    protected void onStart() {
        Log.d("TEST", "google client started!");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }
//    protected void onStop() {
//        Log.d("TEST", "google client stopped!");
//
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.disconnect();
//        }
//        super.onStop();
//    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Loads the login fragment when the user pushes the 'login' button.
     * @param view the Button the user pressed.
     */
    public void goToSignIn(View view) {
        LoginFragment secondFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, secondFragment)
                .addToBackStack("goto_sign_in");
        // Commit the transaction
        transaction.commit();
    }

    /**
     * Loads the registration fragment when the user pushes the 'register' button.
     * @param view the Button the user pressed.
     */
    public void goToRegister(View view) {
        RegisterFragment thirdFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, thirdFragment)
                .addToBackStack("goto_register");
        // Commit the transaction
        transaction.commit();
    }

//    /**
//     * Loads the user profile fragment when the user pushes the 'user profile' button.
//     * @param view the Button the user pressed.
//     */
//    public void goToUserProfile(View view) {
//        UserProfileFragment userProfile = new UserProfileFragment();
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragmentContainer, userProfile)
//                .addToBackStack(null);
//        // Commit the transaction
//        transaction.commit();
//    }

    /**
     * opens the MainPageFragment
     * @param json The json string to send to the activity.
     */
    @Override
    public void onLoginFragmentInteraction(String json) {
        mUsername = json.substring(json.lastIndexOf(" ") + 1);
        mLoginPrefsEditor.putBoolean(getString(R.string.save_login), true);
        mLoginPrefsEditor.putString(getString(R.string.usernamePrefs), mUsername);
        mLoginPrefsEditor.commit();
        mMenuItemEnabled = true;
//        mMainPage = new MainPageFragment();
        Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
//        Bundle arg = mMainPage.getArguments();
        if (mMainPage.getArguments() != null) {
            mMainPage.getArguments().putString(getString(R.string.message), json);
        } else {
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.message), json);
            mMainPage.setArguments(args);
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, mMainPage)
                .addToBackStack("login");
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onLoginRegisterButtonInteraction() {
        goToRegister(null);
    }

    /**
     * opens the MainPageFragment
     * @param json The json string to send to the activity.
     */
    @Override
    public void onRegisterFragmentInteraction(String json) {
        Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
        mMenuItemEnabled = true;
        mUsername = json.substring(json.lastIndexOf(" ") + 1);
        mLoginPrefsEditor.putBoolean(getString(R.string.save_login), true);
        mLoginPrefsEditor.putString(getString(R.string.usernamePrefs), mUsername);
        mLoginPrefsEditor.commit();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.message), json);
        mMainPage = new MainPageFragment();
        mMainPage.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, mMainPage)
                .addToBackStack("register");
        // Commit the transaction
        transaction.commit();
    }

    /**
     * opens the BrewTourFragment
     * @param json The json string to send to the activity.
     */
    @Override
    public void onMainPageBrewTourButtonPressed(String json) {
        BrewTourFrag bf = new BrewTourFrag();
        UserProfileFragment us = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(BrewTourFrag.KEY, json);
        bf.setArguments(args);
        us.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, bf)
                .addToBackStack("brew_tour");
        // Commit the transaction
        transaction.commit();
    }
    /**
     * opens the BeerListFragment
     */
    @Override
    public void onMainPageBeerListButtonPressed() {
        BeerListFragment bl = new BeerListFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, bl)
                .addToBackStack("beer_list");
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_settings);
//        MenuItem menuItem = (MenuItem) findViewById(R.id.action_settings);
        menuItem.setEnabled(mMenuItemEnabled);
        return true;
    }

    @Override
    public void onRateBeerFragmentInteraction(String string) {
        //onMainPageBeerListButtonPressed();
        BeerListFragment bl = new BeerListFragment();
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 1; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        fm.beginTransaction().replace(R.id.fragmentContainer, bl).commit();
    }

    /**
     * Initiated by the user action of inputting a keyword or partial name to search
     * Receives JSON from partial name search at BreweryDB and initiates Suggestions recyclerView
     */
    @Override
    public void onBeerListAddBeerButtonPressed(String json){
        SuggestionsListFragment sl = new SuggestionsListFragment();
        Bundle args = new Bundle();
        args.putSerializable(SuggestionsListFragment.KEY, json);
        sl.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, sl)
                .addToBackStack("add_beer");
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setEnabled(false);
        if (id == R.id.action_settings) {
            mMenuItemEnabled = false;
            Fragment frag = new LoginSelectionFragment();
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            fm.beginTransaction().add(R.id.fragmentContainer, frag).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Ask the user for permission to use their phones location.
     * @param requestCode the request code;
     * @param permissions an array of permissions requested.
     * @param grantResults The grant results for the permissions requested.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Locations need to be working for this portion, please " +
                                    "provide permission"
                            , Toast.LENGTH_SHORT)
                            .show();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mMainPage.setmLatitude(String.valueOf(location.getLatitude()));
        mMainPage.setmLongitude(String.valueOf(location.getLongitude()));

//        Log.d("MainActivity ", "Location changed! " + location.toString());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previouslyrequested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requestslocation updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, itmeans that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.

        while (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                        mCurrentLocation = LocationServices.FusedLocationApi
                                .getLastLocation(mGoogleApiClient);
                        if (mCurrentLocation != null) {
                            mMainPage.setmLatitude(String.valueOf(mCurrentLocation.getLatitude()));
                            mMainPage.setmLongitude(String.valueOf(mCurrentLocation.getLongitude()));
//                            Log.d("MainActivity ", "Location changed! " + String.valueOf(mCurrentLocation.getLatitude()));
                        }
                        startLocationUpdates();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Main Location Test", "Connection failed: ConnectionResult.getErrorCode() = " +
                connectionResult.getErrorCode());
    }
}
