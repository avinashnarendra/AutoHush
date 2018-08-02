package com.autohush.www.dah;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

/**
 * Created by PKBEST on 17-03-2016.
 */
public class LocationInfo extends Activity {

    Spinner dropdown;
    ArrayAdapter<CharSequence> adapter;
    String ID;
    String Name;
    String Pro = "";
    String Lat = "";
    String Lon = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location_info);

        dropdown = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.profile, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Pro = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle extras = getIntent().getExtras();
        String str = extras.getString("extra");
        StringTokenizer st = new StringTokenizer(str," ");
        ID = st.nextToken();
        Name = st.nextToken();

        TextView Title = (TextView)findViewById(R.id.title);
        Title.setText(Name);

        ImageButton abtn = (ImageButton) findViewById(R.id.add);
        abtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationInfo.this,MapsActivity.class);
                intent.putExtra("extra", ID + " " + Pro);
                startActivity(intent);
            }
        });

        ImageButton cbtn = (ImageButton) findViewById(R.id.cur);
        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(LocationInfo.this, "Turn on your Location", Toast.LENGTH_SHORT).show();
                    //Do what you need if enabled...
                }
                else{
                    gpstracker();
                }
            }
        });

        ImageButton dbtn = (ImageButton) findViewById(R.id.del);
        dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = getBaseContext();
                DBoperations db = new DBoperations(ctx);
                db.delInfo(db, ID);
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        ImageButton okbtn = (ImageButton) findViewById(R.id.ok);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Context ctx = getBaseContext();
                    DBoperations db = new DBoperations(ctx);
                    db.updateInfo(db, ID, Lat, Lon, Pro);
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
        });
    }
    public void gpstracker() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        Toast.makeText(LocationInfo.this,"Working On it...." , Toast.LENGTH_SHORT).show();

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
        locationManager.requestSingleUpdate(criteria, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                // reverse geo-code location

                Context ctx = getBaseContext();
                DBoperations db = new DBoperations(ctx);
                Lat = Double.toString(location.getLatitude());
                Lon = Double.toString(location.getLongitude());
                db.updateInfo(db, ID, Lat, Lon, Pro);
                Toast.makeText(LocationInfo.this,"Your Current Location is set" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                Toast.makeText(LocationInfo.this,"Please turn ON your location" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }

        }, null);
    }
}
