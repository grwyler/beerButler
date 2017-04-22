package group10.tcss450.uw.edu.challengeapp;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements FirstFragment
        .OnFragmentInteractionListener, SecondFragment.OnFragmentInteractionListener, ThirdFragment
        .OnFragmentInteractionListener, FourthFragment.OnFragmentInteractionListener {

    FirstFragment mFirstFragment = new FirstFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, mFirstFragment).commit();
            }
        }
    }

    public void goToSignIn(View view) {
        SecondFragment secondFragment = new SecondFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, secondFragment)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

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
    public void onFragmentInteraction(String message) {
        FourthFragment fourthFragment;
        fourthFragment = (FourthFragment) getSupportFragmentManager().
                findFragmentById(R.id.userNameView);
        if (fourthFragment != null) {
            fourthFragment.updateContent(message);
        } else {
            fourthFragment = new FourthFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.message), message);
            fourthFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fourthFragment)
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
