package com.sadi.dako.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sadi.dako.R;
import com.sadi.dako.adapter.PlaceArrayAdapter;
import com.sadi.dako.utils.MapHttpConnection;
import com.sadi.dako.utils.PathJSONParser;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,AdapterView.OnItemClickListener {

    Context con;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private AutoCompleteTextView etPicUp,etDestination;

    private PlaceArrayAdapter mPlaceArrayAdapter;
    private double currentLat,currentLng;

    private LatLng picUpLat,destiLat;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.621166, 90.494618), new LatLng(23.914576, 90.387976));
    private Marker markerPicUp,markerDestination;
    private String picPlace,desPlace;
    private boolean isPicUp,isDestination;
    private TextView tvEstimate,tvDone;
    private List<Marker> markers = new ArrayList<Marker>();
    private ImageView imgDestination;

    private GoogleMap.OnCameraIdleListener onCameraIdleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return false;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name, R.string.app_name){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initialize();
    }

    private void initialize() {

        markers.clear();

        imgDestination = (ImageView)findViewById(R.id.imgDestination);
        tvDone = (TextView)findViewById(R.id.tvDone);
        tvEstimate = (TextView)findViewById(R.id.tvEstimate);
        etPicUp =(AutoCompleteTextView)findViewById(R.id.etPicUp);
        etDestination = (AutoCompleteTextView)findViewById(R.id.etDestination);




        etPicUp.setThreshold(1000);
        etDestination.setThreshold(1000);

        etPicUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPicUp.setThreshold(1);
                return false;
            }
        });

        etDestination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etDestination.setThreshold(1);
                return false;
            }
        });

        etDestination.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                tvDone.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                tvDone.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                tvDone.setVisibility(View.GONE);
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDestination.setVisibility(View.GONE);
                createMarkers(etPicUp.getText().toString(),etDestination.getText().toString());

                if(tvDone.getVisibility()== View.VISIBLE){
                    tvDone.setVisibility(View.GONE);
                }

            }
        });



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
        }

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().
                setTypeFilter(Place.TYPE_COUNTRY).setCountry("BD").build();

        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, R.layout.raw_auto,
                BOUNDS_MOUNTAIN_VIEW, typeFilter);

        //etPicUp.setThreshold(3);
        etPicUp.setOnItemClickListener(mAutocompleteClickListener);
        etPicUp.setAdapter(mPlaceArrayAdapter);


       // etDestination.setThreshold(3);
        etDestination.setOnItemClickListener(mAutocompleteClickListener);
        etDestination.setAdapter(mPlaceArrayAdapter);


        picPlace = etPicUp.getText().toString();
        desPlace = etDestination.getText().toString();
        //configureCameraIdle();
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(MapsActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        if (!locality.isEmpty() && !country.isEmpty()){
                            etDestination.setText(locality + "  " + country);

                           // createMarkers(etPicUp.getText().toString(),etDestination.getText().toString());
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("", "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.e("", "placeResult" + placeResult.toString());

            createMarkers(etPicUp.getText().toString(),etDestination.getText().toString());

            hideKeyBoard();

        }
    };

    private void hideKeyBoard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void createMarkers(String picUp,String des) {

         imgDestination.setVisibility(View.GONE);
        if((!TextUtils.isEmpty(picUp))&&(!TextUtils.isEmpty(des))){
            picUpLat = getLocationFromAddress(this, picUp);
            destiLat = getLocationFromAddress(this, des);
            if((picUpLat!=null)&&(destiLat!=null)){
                mMap.clear();

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.picup);
                markerPicUp = mMap.addMarker(new MarkerOptions()
                        .position(picUpLat)
                        .draggable(false)
                        .title(picUp)
                        .icon(icon));


                BitmapDescriptor icondes = BitmapDescriptorFactory.fromResource(R.drawable.dropoff);

                markerDestination = mMap.addMarker(new MarkerOptions()
                        .position(destiLat)
                        .draggable(true)
                        .title(des)
                        .icon(icondes));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(destiLat));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                String url = getMapsApiDirectionsUrl(picUpLat, destiLat);
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);

                CalculationByDistance(picUpLat,destiLat);

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(new LatLng(picUpLat.latitude, picUpLat.longitude))
                        .include(new LatLng(destiLat.latitude,destiLat.longitude)).build();

                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 300, 40));

                markers.add(0,markerPicUp);
                markers.add(1,markerDestination);


                mMap.getUiSettings().setScrollGesturesEnabled(false);

                tvDone.setText("Send Pick Up Request");
            }
        }


    }




    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

        }
    };





    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });


            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng latLng = mMap.getCameraPosition().target;
                    Geocoder geocoder = new Geocoder(MapsActivity.this);

                    try {
                        List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            String locality = addressList.get(0).getAddressLine(0);
                            String country = addressList.get(0).getCountryName();
                            if (!locality.isEmpty() && !country.isEmpty()){
                                etDestination.setText(locality + "  " + country);
                                //imgDestination.setVisibility(View.VISIBLE)
                                //tvDone.setVisibility(View.VISIBLE);
                                //createMarkers(etPicUp.getText().toString(),etDestination.getText().toString());
                            }else {
                                tvDone.setVisibility(View.GONE);
                                imgDestination.setVisibility(View.VISIBLE);
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }



        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {



            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
//                markerPicUp = marker;
                markerDestination = marker;
//
                double picLat,picLng,destLat,destLng;
//
//                picLat = markerPicUp.getPosition().latitude;
//                picLng = markerPicUp.getPosition().longitude;
                destLat = markerDestination.getPosition().latitude;
                destLng = markerDestination.getPosition().longitude;

                getAddressDrug(destLat,destLng);

//                if(marker==markerPicUp){
//                    getAddressDrug(picLat,picLng,"picup");
////                    Toast.makeText(con,""+picLat+" ,"+picLng,Toast.LENGTH_LONG).show();
//                }else if (marker==markerDestination){
//                    getAddressDrug(destLat,destLng,"");
//                   // Toast.makeText(con,""+destLng+" ,"+picLng,Toast.LENGTH_LONG).show();
//                }


            }
        });


    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.e("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        int fair = kmInDec*50;
        tvEstimate.setVisibility(View.VISIBLE);
        tvEstimate.setText("Your estimated fair is:"+fair);
        //Toast.makeText(con,"Your estimated fair is:"+fair,Toast.LENGTH_LONG).show();

        return Radius * c;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                //getAddress(currentLat,currentLng);
//                place marker at current position
//                mGoogleMap.clear();
//                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title("Current Position");
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                mCurrLocationMarker = mMap.addMarker(markerOptions);

               // addMarkersToMap();

            }

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("", "Google Places API connection suspended.");
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        PersistData.setStringData(getApplicationContext(), AppConstant.lat,""+location.getLatitude());
//        PersistData.setStringData(getApplicationContext(), AppConstant.lng,""+location.getLongitude());
        //Toast.makeText(con,location.getLatitude()+""+location.getLongitude(),Toast.LENGTH_SHORT).show();

       // mMap.clear();


        currentLat = location.getLatitude();
        currentLng = location.getLongitude();

        if(markers.size()==0){
            getAddress(currentLat,currentLng,"");
        }
//
//
//
//        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//           // mCurrLocationMarker.remove();
//        }
//
//        //Place current location marker
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mMap.addMarker(markerOptions.draggable(true));
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//       // addMarkersToMap(location);
//        //stop location updates
        if (mGoogleApiClient != null) {
           // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    }

    private void getAddressDrug(double lat,double lng) {
        Geocoder geocoder = new Geocoder(con, Locale.getDefault());

        List<Address> addresses  = null;
        try {

            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                    etDestination.setText(address + "," + city);

                createMarkers(etPicUp.getText().toString(),etDestination.getText().toString());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private void getAddress(double lat,double lng,String what) {
        Geocoder geocoder = new Geocoder(con, Locale.getDefault());

        List<Address> addresses  = null;
        try {

            addresses = geocoder.getFromLocation(lat,lng, 1);
            if(addresses!=null){
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                if(what.equalsIgnoreCase("picup")){

                }
                etPicUp.setText(address+","+city);
                String picUp = etPicUp.getText().toString();


                //Place current location marker

                mMap.clear();
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.picup);
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Pic Up:"+picUp);
                markerOptions.icon(icon);
                mCurrLocationMarker = mMap.addMarker(markerOptions.draggable(false));

                markers.add(mCurrLocationMarker);
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                tvEstimate.setVisibility(View.GONE);
                etDestination.setText("");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Toast.makeText(con,address+","+city+""+country,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                         mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }



    private String  getMapsApiDirectionsUrl(LatLng origin,LatLng dest) {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;

    }



    private class ReadTask extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                MapHttpConnection http = new MapHttpConnection();
                data = http.readUr(url[0]);


            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

    }

    private class ParserTask extends AsyncTask<String,Integer, List<List<HashMap<String , String >>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            // TODO Auto-generated method stub
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);

        }}


}
