/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.gms.location.LocationServices;

import group10.tcss450.uw.edu.challengeapp.BeerList.BeerListFragment;
import group10.tcss450.uw.edu.challengeapp.BrewTour.BrewTourFrag;
import group10.tcss450.uw.edu.challengeapp.BrewTour.RateBeerFragment;


/**
 * The main activity class. Login, registration, and cardview are loaded and managed here.
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        MainPageFragment.OnFragmentInteractionListener,
        RateBeerFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private SharedPreferences mLoginPreferences;
    private SharedPreferences.Editor mLoginPrefsEditor;
    private boolean mSaveLogin;
    private String mUsername;
    public static FragmentManager mFragManager;
    public MainPageFragment mMainPage;
    private GoogleApiClient mGoogleApiClient;

    private static final int MY_PERMISSIONS_LOCATIONS = 814;
    protected LocationManager locationManager;
    //static Boolean mPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPage = new MainPageFragment();
        setContentView(R.layout.activity_main);
        Fragment fragment;
        mFragManager = getSupportFragmentManager();
        mLoginPreferences = getSharedPreferences(getString(R.string.login_prefs),
                Context.MODE_PRIVATE);
        mLoginPrefsEditor = mLoginPreferences.edit();
        mSaveLogin = mLoginPreferences.getBoolean(getString(R.string.save_login),
                false);
        if (mSaveLogin == true) {
            fragment = mMainPage;
        } else {
            fragment = new LoginSelectionFragment();
        }
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, fragment).commit();
            }
        }
        mFragManager = getSupportFragmentManager();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


       // mPermission = true;
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
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
                .addToBackStack(null);
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
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    /**
     * Loads the user profile fragment when the user pushes the 'user profile' button.
     * @param view the Button the user pressed.
     */
    public void goToUserProfile(View view) {
        UserProfileFragment userProfile = new UserProfileFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, userProfile)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

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

        Toast.makeText(this, json, Toast.LENGTH_SHORT).show();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.message), json);
        mMainPage.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, mMainPage)
                .addToBackStack(null);
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

        mUsername = json.substring(json.lastIndexOf(" ") + 1);
        mLoginPrefsEditor.putBoolean(getString(R.string.save_login), true);
        mLoginPrefsEditor.putString(getString(R.string.usernamePrefs), mUsername);
        mLoginPrefsEditor.commit();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.message), json);
        mMainPage.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, mMainPage)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    /**
     * opens the BrewTourFragment
     * @param json The json string to send to the activity.
     */
    @Override
    public void onMainPageBrewTourFragmentInteraction(String json) {
        BrewTourFrag bf = new BrewTourFrag();
        UserProfileFragment us = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(BrewTourFrag.KEY, json);
        bf.setArguments(args);
        us.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, bf)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
    /**
     * opens the BeerListFragment
     */
    @Override
    public void onMainPageBeerListFragmentInteraction() {
        BeerListFragment bl = new BeerListFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(BeerListFragment.KEY, json);
//        bl.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, bl)
                .addToBackStack(null);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mLoginPrefsEditor.clear();
            mLoginPrefsEditor.commit();
            loadFragment(new LoginSelectionFragment());
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
                    //mPermission = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Locations need to be working for this portion, " +
                                    "please provide permission"
                            , Toast.LENGTH_SHORT)
                            .show();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, frag).addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLocationChanged(Location location) {
        mMainPage.setmLatitude(String.valueOf(location.getLatitude()));
        mMainPage.setmLongitude(String.valueOf(location.getLongitude()));
        //Log.d("MainActivity ", "Location changed! " + String.valueOf(location.getLatitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRateBeerFragmentInteraction(String string) {

    }
}
