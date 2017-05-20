/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import group10.tcss450.uw.edu.challengeapp.BeerList.BeerListFragment;
import group10.tcss450.uw.edu.challengeapp.BrewTour.BrewTourFrag;


/**
 * The main activity class. Login, registration, and cardview are loaded and managed here.
 */
public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        MainPageFragment.OnFragmentInteractionListener {

    private SharedPreferences mLoginPreferences;
    private SharedPreferences.Editor mLoginPrefsEditor;
    private boolean mSaveLogin;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment;

        mLoginPreferences = getSharedPreferences(getString(R.string.login_prefs),
                Context.MODE_PRIVATE);
        mLoginPrefsEditor = mLoginPreferences.edit();
        mSaveLogin = mLoginPreferences.getBoolean(getString(R.string.save_login),
                false);
        if (mSaveLogin == true) {
            fragment = new MainPageFragment();
        } else {
            fragment = new LoginSelectionFragment();
        }
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, fragment).commit();
            }
        }
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
        MainPageFragment fourthFragment = new MainPageFragment();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.message), json);
        fourthFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fourthFragment)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    /**
     * opens the MainPageFragment
     * @param json The json string to send to the activity.
     */
    @Override
    public void onRegisterFragmentInteraction(String json) {
        Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
        MainPageFragment fourthFragment = new MainPageFragment();
        mUsername = json.substring(json.lastIndexOf(" ") + 1);
        mLoginPrefsEditor.putBoolean(getString(R.string.save_login), true);
        mLoginPrefsEditor.putString(getString(R.string.usernamePrefs), mUsername);
        mLoginPrefsEditor.commit();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.message), json);
        fourthFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fourthFragment)
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

    @Override
    public void onMainPageBeerListFragmentInteraction(String json) {
        BeerListFragment bl = new BeerListFragment();
        Bundle args = new Bundle();
        args.putSerializable(BeerListFragment.KEY, json);
        bl.setArguments(args);
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

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, frag).addToBackStack(null);
        transaction.commit();
    }
}
