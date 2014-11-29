package com.restaurantroulette.app.lewisrhine.restaurantroulette;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.reflect.TypeToken;
import com.iainconnor.objectcache.CacheManager;
import com.iainconnor.objectcache.DiskCache;
import com.iainconnor.objectcache.PutCallback;
import com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi.Yelp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ListFragment.OnFragmentInteractionListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, ThePickFragment.OnFragmentInteractionListener {

    //Request code to use when launching the resolution activity
    private final static int REQUEST_RESOLVE_ERROR = 1001;
    //Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private final static String consumerKey = "YBmLcOx_LTdkauRCURoQ_A";
    private final static String consumerSecret = "9fNo65vzgP_gXH4VRp8XdX0Ee-w";
    private final static String token = "A-OrrN8NDKeD3MouputN96UZTbd2Vg7q";
    private final static String tokenSecret = "AYROF4dW9KRcV4dHn_afJHqsSXY";
    private final Yelp yelpApiCall = new Yelp(consumerKey, consumerSecret, token, tokenSecret);

    Double latitude =  41.996390, longitude = -88.030403;

    private int errorCode;

    private ArrayList<Businesses> businessesList;

    private ListFragment listFragment;
    private RestaurantListAdapter whineListAdapter;


    private static final String TAG = "GooglePlayServicesActivity";

    private static final String KEY_IN_RESOLUTION = "is_in_resolution";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;
    private LocationClient locationClient;
    private LocationManager locationManager;
    private Location currentLocation;

    /**
     * Determines if the client is in a resolution state, and
     * waiting for resolution intent to return.
     */
    private boolean mIsInResolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsInResolution = savedInstanceState.getBoolean(KEY_IN_RESOLUTION, false);
        }



        setContentView(R.layout.activity_main);
        locationClient = new LocationClient(this, this, this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //Set the error code for checking if device had Play Service installed
        errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



    }

    @Override
    protected void onStart() {
        //Check to make sure location service are enabled.
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            //Create a dialog alert if location service is off.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(this.getResources().getString(R.string.location_off));
            dialog.setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

                //Load the location settings if ok button is clicked.
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }

        //check to see if there is an error with Play Service, if not connect the location Client.
        if (errorCode != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0).show();
        } else {
            locationClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        locationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position){
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, listFragment = new ListFragment()).commit();
                break;
            case 1:

                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentStart() {
        whineListAdapter = new RestaurantListAdapter(this, loadCache());
        listFragment.setListAdapter(whineListAdapter);
    }

    @Override
    public void onRouletteButtonClicked() {
        Random randomNumber = new Random();

        int number = randomNumber.nextInt(businessesList.size());

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("thePick");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        ThePickFragment thePickFragment = ThePickFragment.newInstance(businessesList.get(number).getName());
        thePickFragment.show(fragmentTransaction, "thePick");
    }


    public void getYelpData() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //Create a string that hold the response from our search from Yelp api
                //Double latitude =  41.996390, longitude = -88.030403;
                Log.d("LR", "Async started");

                String response = yelpApiCall.search("food", latitude, longitude);

                try {
                    //Get the JSON object for our response string from the Yelp api.
                    //Then get the JSONArray businesses from it.
                    JSONObject json = new JSONObject(response);
                    JSONArray businesses = json.getJSONArray("businesses");
                    Log.d("LR", "output " + response);
                    //Setup our array list of Businesses Objects that will hold our parsed data.
                    businessesList = new ArrayList<Businesses>();

                    //Loop through the businesses array from our JSON grabbing the values we need.
                    for (Integer i = 0; i < businesses.length(); i++) {

                        Businesses businessesObj = new Businesses();
                        JSONObject business = businesses.getJSONObject(i);

                        //Set the values for a Businesses Object from the current Json Object in out Json Array
                        businessesObj.setName(business.getString("name"));

                        businessesObj.setImage_url(business.getString("image_url"));
                        businessesObj.setRating(Double.parseDouble(business.getString("rating")));
                        businessesObj.setRating_image_url(business.getString("rating_img_url"));

                        //Yelp puts its categories in an array, since we only want one
                        //We have to make a new JSON array and get the first value.
                        JSONArray categoriesArray = business.getJSONArray("categories");

                        //Here I am cutting out the unwanted JSON code so the user only sees the nice
                        //human readable text for the category. This seems a bit sloppy but not sure how else to do it
                        String catTemp = categoriesArray.get(0).toString();
                        int start = catTemp.indexOf("[");
                        int end = catTemp.lastIndexOf(",");
                        String temp = catTemp.substring((start + 2), (end - 1));
                        businessesObj.setCategories(temp);


                        //Yelp gives meters, so first they have to be converted to miles
                        Double meters = Double.parseDouble(business.getString("distance"));
                        Double miles = meters * 0.00062137119;
                        //then rounded up.
                        Double milesRound = Math.round(miles * 10.0) / 10.0;
                        businessesObj.setDistance(milesRound.toString());

                        //Yelp also puts the address info in an array so we must
                        //once again create a new JSON array and get the three
                        //String values from it (address, city, and state_code)
                        JSONObject locationObj = business.getJSONObject("location");
                        JSONArray addressArray = locationObj.getJSONArray("address");

                        //Since Yelp dose not give latitude and longitude,
                        //and we need them for making map markers we have
                        //use the google geocode api, but first we have
                        //to replace blank space with + signs.
                        String address = addressArray.get(0).toString();
                        address = address.replace(" ", "+");
                        String city = locationObj.getString("city");
                        city = city.replace(" ", "+");
                        String state = locationObj.getString("state_code");

                        //Build a string form the address info we got from yelp
                        //then query the google api using http clint
                        String uri = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                                address + "," + city + "," + state;
                        HttpGet httpGet = new HttpGet(uri);
                        HttpClient client = new DefaultHttpClient();
                        HttpResponse responseGoogle;


                        StringBuilder stringBuilder = new StringBuilder();
                        try {

                            responseGoogle = client.execute(httpGet);
                            HttpEntity entity = responseGoogle.getEntity();
                            InputStream stream = entity.getContent();
                            int b;
                            while ((b = stream.read()) != -1) {
                                stringBuilder.append((char) b);
                            }
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Get the latitude and longitude from the Google geocode response
                        JSONObject jsonObject;
                        double lat;
                        double lng;
                        try {
                            jsonObject = new JSONObject(stringBuilder.toString());

                            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lng");

                            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lat");


                            //Set the latitude and longitude to the businesses object.
                            businessesObj.setLatitude(lat);
                            businessesObj.setLongitude(lng);
                        } catch (JSONException e) {
                            Log.d("LR", "geo get failed");
                            e.printStackTrace();
                        }

                        //Add the businesses object to our list array.
                        businessesList.add(businessesObj);
                    }

                } catch (JSONException e) {
                    Log.d("LR", "Yelp get failed: " + response);
                    e.printStackTrace();
                }

                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                //Create a new adapter from our BusinessListAdapter class,
                //then pass it to the list using the fillList method inside the fragment
                whineListAdapter = new RestaurantListAdapter(getApplicationContext(), businessesList);

                if (listFragment != null) {
                    listFragment.setListAdapter(whineListAdapter);
                }

                //Pass our array list to the SaveCache method to be saved.
                saveCache(businessesList);

                //Create a map marker for each item in your list array.
                //for (Businesses aBusinessesList : businessesList) {
                //    map.addMarker(new MarkerOptions()
                //            .position(new LatLng(aBusinessesList.getLatitude(), aBusinessesList.getLongitude()))
                //            .title(aBusinessesList.getName()));
                // }
                //listFragment.stopRefresh();
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
        return businessesList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

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

    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = locationClient.getLastLocation();
        latitude =  currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();
        getYelpData();

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }
    //Creates a dialog for an error message
    private void showErrorDialog(int errorCode) {
        //Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        //Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    // A fragment to display an error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }
    }
}
