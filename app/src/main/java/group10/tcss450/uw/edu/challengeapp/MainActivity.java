/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */

package group10.tcss450.uw.edu.challengeapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import group10.tcss450.uw.edu.challengeapp.BrewTour.BrewTourFrag;

/**
 * The main activity class. Login, registration, and cardview are loaded and managed here.
 */
public class MainActivity extends AppCompatActivity implements SecondFragment
        .OnFragmentInteractionListener, ThirdFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new FirstFragment()).commit();
            }
        }
    }

    /**
     * Loads the login fragment when the user pushes the 'login' button.
     * @param view the Button the user pressed.
     */
    public void goToSignIn(View view) {
        SecondFragment secondFragment = new SecondFragment();
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
        ThirdFragment thirdFragment = new ThirdFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, thirdFragment)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Fragment frag, String message) {
        if (frag instanceof SecondFragment || frag instanceof ThirdFragment) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            FourthFragment fourthFragment = new FourthFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.message), message);
            fourthFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fourthFragment)
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        } else if (frag instanceof FourthFragment) {
            BrewTourFrag bf = new BrewTourFrag();
            Bundle args = new Bundle();
            args.putSerializable(BrewTourFrag.KEY, message);
            bf.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, bf)
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }



     //I commented this out to work on BrewTour we can put it back later and use it as our main page fragment
     // or delete it all together. We also do nit need it to be avilable on a tablet since we are only targeting phones.
//        FourthFragment fourthFragment;
//        fourthFragment = (FourthFragment) getSupportFragmentManager().
//                findFragmentById(R.id.userNameView);
//        if (fourthFragment != null) {
//            fourthFragment.updateContent(message);
//        } else {
//            fourthFragment = new FourthFragment();
//            Bundle args = new Bundle();
//            args.putSerializable(getString(R.string.message), message);
//            fourthFragment.setArguments(args);
//
//            FragmentTransaction transaction = getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainer, fourthFragment)
//                    .addToBackStack(null);
//            // Commit the transaction
//            transaction.commit();
//        }

    }

}
