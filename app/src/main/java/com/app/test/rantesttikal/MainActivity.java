package com.app.test.rantesttikal;

import android.app.Fragment;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.test.rantesttikal.data.model.Movie;

public class MainActivity extends AppCompatActivity implements
        GalleryFragment.OnGalleryInteractionListener, DetailsFragment.OnDetailsInteractionListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            // 2 fragments side by side
        } else {
            if (savedInstanceState != null) {
                // cleanup any existing fragments in case we are in detailed mode
                getFragmentManager().executePendingTransactions();
                Fragment fragmentById = getFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragmentById!=null) {
                    getFragmentManager().beginTransaction().remove(fragmentById).commit();
                }
            }
            GalleryFragment galleryFragment = new GalleryFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, galleryFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Movie movie) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailFragment);
            detailsFragment.setMovie(movie);
        } else {
            DetailsFragment detailsFragment = DetailsFragment.newInstance(movie);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailsFragment).commit();
            transaction.addToBackStack(null);
        }
    }

    @Override
    public void onDetailsFragmentInteraction(Uri uri) {

    }

}
