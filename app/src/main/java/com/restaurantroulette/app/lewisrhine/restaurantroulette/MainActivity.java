package com.restaurantroulette.app.lewisrhine.restaurantroulette;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi.Businesses;
import com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi.Yelp;
import com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi.YelpResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ListFragment.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, ThePickFragment.OnFragmentInteractionListener {

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    //Request code to use when launching the resolution activity
    private final static int REQUEST_RESOLVE_ERROR = 1001;
    //Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static String consumerKey = "YBmLcOx_LTdkauRCURoQ_A";
    private final static String consumerSecret = "9fNo65vzgP_gXH4VRp8XdX0Ee-w";
    private final static String token = "A-OrrN8NDKeD3MouputN96UZTbd2Vg7q";
    private final static String tokenSecret = "AYROF4dW9KRcV4dHn_afJHqsSXY";
    private final Yelp yelpApiCall = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
    private static final String TAG = "GooglePlayServicesActivity";
    private static final String KEY_IN_RESOLUTION = "is_in_resolution";
    Double latitude = 41.996390, longitude = -88.030403;
    // Bool to track whether the app is already resolving an error
    private boolean resolvingError = false;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     */
    private CharSequence mTitle;
    private int errorCode;
    private ArrayList<com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi.Businesses> businessesList;
    private ListFragment listFragment;
    private RestaurantListAdapter whineListAdapter;
    /**
     * Google API client.
     */
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private Location currentLocation;
    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Determines if the client is in a resolution state, and
     * waiting for resolution intent to return.
     */
    private boolean mIsInResolution;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsInResolution = savedInstanceState.getBoolean(KEY_IN_RESOLUTION, false);
        }

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    /**
     * Called when the Activity is made visible.
     * A connection to Play Services need to be initiated as
     * soon as the activity is visible. Registers {@code ConnectionCallbacks}
     * and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    // Optionally, add additional APIs and scopes if required.
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /**
     * Called when activity gets invisible. Connection to Play Services needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

    /**
     * Handles Google Play Services resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                retryConnecting();
                break;
        }
    }

    private void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onDisconnected() {

    }

    /**
     * Called when {@code mGoogleApiClient} connection is suspended.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), this, 0, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            retryConnecting();
                        }
                    }).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            retryConnecting();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, listFragment = new ListFragment()).commit();
                break;
            case 1:

                break;
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentStart() {

        if (loadCache() != null) {
            whineListAdapter = new RestaurantListAdapter(this, loadCache());
            listFragment.setListAdapter(whineListAdapter);
            Log.d("LR", "Cache loaded");
        }

        getYelpDataJackson();

    }

    @Override
    public void onListRefresh() {
        getYelpDataJackson();
    }

    @Override
    public void onSpinButtonClicked() {
        Random randomNumber = new Random();

        int number = randomNumber.nextInt(businessesList.size());

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("thePick");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        ThePickFragment thePickFragment = ThePickFragment.newInstance(businessesList.get(number).getName(), businessesList.get(number).getImage_url());
        thePickFragment.show(fragmentTransaction, "thePick");
    }

    public void getYelpDataJackson() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String response = yelpApiCall.search("food", latitude, longitude);


                //Businesses businesses;

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

                try {
                    Log.d("LR", "JSON output " + response);

                    //Businesses businesses = mapper.readValue(response, Businesses.class);
                    YelpResult yelpResult = mapper.readValue(response, YelpResult.class);
                    // Log.d("LR", "Bussness name " + businesses.getName());
                    Log.d("LR", "total name " + yelpResult.getTotal());

                    businessesList = yelpResult.getBusinesses();
                    Log.d("LR", "bussnes name " + businessesList.get(1).getName());

                    //businessesList = new ArrayList<>();

                    //businessesList.add(businesses);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("LR", "Jackson done failed sucker");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                whineListAdapter = new RestaurantListAdapter(getApplicationContext(), businessesList);

                if (listFragment != null) {
                    listFragment.setListAdapter(whineListAdapter);
                    saveCache(businessesList);
                }
            }
        }.execute();
    }


    //I am just using serializable for caching because the size of date is so small.
    //Might replace with an SQL or a lru later down the road
    //Saves Array Lists of Businesses.
    public void saveCache(ArrayList<Businesses> list) {

        //get the default cache directory where the file will be saved.
        final File cache_dir = this.getCacheDir();
        final File listCache = new File(cache_dir.getAbsoluteFile() + "RRListCache");

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        boolean keep = true;

        try {
            fos = new FileOutputStream(listCache);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
            keep = false;
            Log.d("LR", "stream failed");
        } finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
                if (!keep) listCache.delete();
            } catch (Exception ignored) {
                Log.d("LR", "close failed");
            }
        }
    }

    //Loads any saved business array lists saved to the system.
    public ArrayList<Businesses> loadCache() {
        final File cache_dir = this.getCacheDir();
        final File listCache = new File(cache_dir.getAbsoluteFile() + "RRListCache");

        FileInputStream fos = null;
        ObjectInputStream oos = null;
        boolean keep = true;

        try {
            fos = new FileInputStream(listCache);
            oos = new ObjectInputStream(fos);

            businessesList = (ArrayList<Businesses>) oos.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            keep = false;
            //If no cache file is found create a new empty list.
            businessesList = null;
            Log.d("LR", "no cache found.");
        } finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
                if (!keep) listCache.delete();
            } catch (Exception e) {
            }
        }
        return null;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
