package com.autohush.www.dah;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import java.util.Calendar;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Naveen on 10-03-2016.
 */
public class MyService extends Service {

    Handler handler = new Handler();
    final double specifiedlimit=0.01;
    int f = 0;
    String previousLocation=null;

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        final long intervalTime = 10000; // 10 sec
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gpstracker();
                handler.postDelayed(this, intervalTime);
            }
        }, intervalTime);

        return START_STICKY;

    }

    private void timetracker() {
        Context ctx = getBaseContext();
        DBoperations db = new DBoperations(ctx);
        Cursor cr = db.gettimeInfo(db);
        boolean empty = cr.moveToFirst();
        if(empty) {
            do {
                int sth = Integer.parseInt(cr.getString(1));
                int stm = Integer.parseInt(cr.getString(2));
                int eth = Integer.parseInt(cr.getString(3));
                int etm = Integer.parseInt(cr.getString(4));
                String pro = cr.getString(5);

                int st = sth*100+stm;
                int et = eth*100+etm;

                Calendar cal = Calendar.getInstance();

                int minute = cal.get(Calendar.MINUTE);
                int hourofday = cal.get(Calendar.HOUR_OF_DAY);
                int Current = hourofday * 100 + minute;

                if (st <= Current && Current <= et) {
                    AudioManager myManager;
                    myManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (pro.equals("Silent")) {
                        myManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    } else if (pro.equals("Vibration")) {
                        myManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    } else if (pro.equals("Ringing")) {
                        myManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }
                    NotifyT(pro);
                }

            } while (cr.moveToNext());
        }
    }

    private void NotifyT(String pro) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo32)
                        .setContentTitle("AutoHush")
                        .setContentText("Profile set to: " + pro);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(100, builder.build());
    }

    public void Notify(String name,String Pro) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo32)
                        .setContentTitle("Location Detected: " + name)
                        .setContentText("Profile set to: " + Pro);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(100, builder.build());

    }

    public boolean distanceformula(double a,double b,double x,double y,String pro,String name)
    {
        double distance=sqrt(pow((a-x),2)+pow((b-y),2));
        boolean flag = false;
        if(distance<=specifiedlimit)
        {
            flag = true;
            AudioManager myAudioManager;
            myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if(pro.equals("Silent")){
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
            else if(pro.equals("Vibration")){
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
            else if(pro.equals("Ringing")){
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
            if(!name.equals(previousLocation))
             Notify(name,pro);
        }
        return flag;
    }




    public void gpstracker() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

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
                Cursor cr = db.getInfo(db);
                boolean empty = cr.moveToFirst();

                f = 0;
                if(empty) {
                    do {

                        String name = cr.getString(1);

                        Double la;
                        Double lo;

                        if (cr.getString(2) == null)
                            la = 0.0;
                        else
                            la = Double.parseDouble(cr.getString(2));

                        if (cr.getString(3) == null)
                            lo = 0.0;
                        else
                            lo = Double.parseDouble(cr.getString(3));

                        String pro = cr.getString(4);
                        if (distanceformula(la, lo, location.getLatitude(), location.getLongitude(), pro, name)) {
                            f = 1;
                            previousLocation=name;
                            break;
                        }
                        else
                            f = 0;

                    }
                    while (cr.moveToNext());
                }
                if (f == 0)
                    timetracker();
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

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


    @Override
    public void onDestroy() {
        Toast.makeText(this, "AutoHush is Off", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
