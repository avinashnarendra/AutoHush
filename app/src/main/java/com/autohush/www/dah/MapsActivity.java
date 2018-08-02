package com.autohush.www.dah;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by ADMIN on 19-03-2016.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mapFragment; // Might be null if Google Play services APK is not available.
    private Marker marker;
    private static Geocoder geocoder=null;
    EditText location_tf;
    ImageButton Bsearch;
    public final static String key = "abc";
    public final static String key1 = "abcd";
    String ID;
    String Pro;


    final int maxResult =5;
    int position;
    List<Address> addressList= new ArrayList<>();
    Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(this);
        Bsearch=(ImageButton) findViewById(R.id.Bsearch);
        location_tf = (EditText) findViewById(R.id.TFaddress);
        location_tf.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    Bsearch.performClick();
                    return true;
                }
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        String str = extras.getString("extra");
        StringTokenizer st = new StringTokenizer(str," ");
        ID = st.nextToken();
        Pro = st.nextToken();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // setUpMapIfNeeded();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            position=extras.getInt(key1);
        }
        searchMarker(position);
    }

    public void searchMarker(int position){


        address = addressList.get(position);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        String lat = Double.toString(address.getLatitude());
        String lon = Double.toString(address.getLongitude());

        Context ctx = getBaseContext();
        DBoperations db = new DBoperations(ctx);
        db.updateInfo(db, ID, lat, lon, Pro);

        if (marker != null) {
            marker.remove();
        }
        marker = mapFragment.addMarker(new MarkerOptions().position(latLng).title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mapFragment.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12.0f));
       // mapFragment.animateCamera(CameraUpdateFactory.zoomTo( 17.0f ));
        //mapFragment.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),12.0f));

    }
    public void onSearch(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();

        if (internet_available()) {

            geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location,maxResult);


            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList.size() == 0) {
                Toast.makeText(getApplicationContext(), "Address Not Found",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), ""+addressList.size(),
                        Toast.LENGTH_SHORT).show();
                String[] mystring=new String[addressList.size()];


                for (int j = 0; j < addressList.size(); j++) {
                    address = addressList.get(j);
                    String str=address.getAddressLine(0); //Location name
                    str+=" "+address.getAddressLine(1); //city name
                    str+=" "+address.getAddressLine(2); //country name

                    mystring[j]=str;

                }
                Intent intent = new Intent(this, RelatedSearches.class);
                intent.putExtra(key, mystring);
                startActivityForResult(intent, 1);
            }
        }

        else {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }

    }


    public boolean internet_available() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        // not connected to the internet
        return false;
    }

    public void changeType(View view) {
        if (mapFragment .getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mapFragment .setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else
            mapFragment .setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapFragment .setMyLocationEnabled(true);
        mapFragment.getUiSettings().setZoomControlsEnabled(true);

        geocoder=new Geocoder(this);
        mapFragment.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                List<Address> addresses = new ArrayList<>();
                if (internet_available()) {

                    try {

                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() != 0 && addresses != null) {
                        android.location.Address address = addresses.get(0);
                        if (address != null) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i) + "\n");

                            }
                            Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Print NUll",
                                Toast.LENGTH_SHORT).show();
                    }


                    //remove previously placed Marker
                    if (marker != null) {
                        marker.remove();
                    }

                    //place marker where user just clicked
                    marker = mapFragment.addMarker(new MarkerOptions().position(latLng).title("Marker")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    String lat = Double.toString(latLng.latitude);
                    String lon = Double.toString(latLng.longitude);

                    Context ctx = getBaseContext();
                    DBoperations db = new DBoperations(ctx);
                    db.updateInfo(db,ID,lat,lon,Pro);

                    mapFragment.animateCamera(CameraUpdateFactory.newLatLng(latLng));
       //             mapFragment.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),12.0f));
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapFragment=googleMap;
        setUpMap();
    }

    public void ondone(View view){
                finish();
    }
}
