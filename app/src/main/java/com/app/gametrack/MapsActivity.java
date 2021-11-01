package com.app.gametrack;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Double> arrayList=new ArrayList<Double>();
    private DatabaseReference databaseReference;
    private DatabaseReference rf;



    private int i = 0;
    private double Latitude;
    private double Longitude;

    private LocationListener locationListener;
    private LocationListener locationListener_button;
    private LocationManager locationManager;
    private LocationManager locationManager_button;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    private final long MIN_TIME_BUTTON = 0;
    private final long MIN_DIST_BUTTON = 50;

    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextMessage;
    private EditText editTextMessage2;
    private EditText editTextMessage3;
    private EditText editTextMessage4;
    AlertDialog alertDialog;
    AlertDialog alertDialog2;
    AlertDialog alertDialog3;
    AlertDialog alertDialog4;
    AlertDialog alertDialog5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startService(new Intent(this, YourService.class));
        setContentView(R.layout.activity_maps);
        /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        assert powerManager != null;
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        wakeLock.acquire();*/
        //Alarm alarm = new Alarm();
        //alarm.SetAlarm(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //LatLng sydney = new LatLng(-34, 151);
        //LatLng manhattan = new LatLng(40, 73);
        //arrayList.add(sydney);
        //arrayList.add(manhattan);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        //editTextLatitude = findViewById(R.id.editText);
        //editTextLongitude = findViewById(R.id.editText2);

        //editTextMessage = findViewById(R.id.editText3);
        //editTextMessage2 = findViewById(R.id.editText4);
        //editTextMessage3 = findViewById(R.id.editText5);
        //editTextMessage4 = findViewById(R.id.editText6);
        //editTextMessage2.setText("DO NOT SEE COORDINATES? RESTART APP");
        //editTextMessage3.setText("You Can see People GPS in REAL-TIME!");
        //editTextMessage4.setText("Please Leave your APP On!");
        // Loop over all previous positions stored in firebase database


        // elimino posizioni se sono passate nel tempo di 24 H
        databaseReference = FirebaseDatabase.getInstance().getReference("Location");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long time = System.currentTimeMillis();
                Integer timeNow = (int) time;
                String TimeNow = timeNow.toString();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String end = ds.child("timestamp").getValue().toString().substring(1, ds.child("timestamp").getValue().toString().length() - 1);
                        String[] End = end.split("=");
                        String EndFinal = End[1];

                        int endtime = Integer.parseInt(EndFinal.trim());
                        Integer difference = timeNow - endtime;
                        String differenceString = difference.toString();

                        String databaseLatitudeString = ds.child("latitude").getValue().toString().substring(1, ds.child("latitude").getValue().toString().length() - 1);
                        String databaseLongitudeString = ds.child("longitude").getValue().toString().substring(1, ds.child("longitude").getValue().toString().length() - 1);

                        String[] stringLat = databaseLatitudeString.split(", ");
                        Arrays.sort(stringLat);
                        String latitude = stringLat[stringLat.length - 1].split("=")[1];

                        String[] stringLong = databaseLongitudeString.split(", ");
                        Arrays.sort(stringLong);
                        String longitude = stringLong[stringLong.length - 1].split("=")[1];


                        if (difference >= 60*60*1000 ) {
                            ds.getRef().removeValue();
                            mMap.clear();
                        } else {
                            LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                            mMap.addMarker(new MarkerOptions().position(latLng).title("Tracked Person"));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }







            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
















    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;




//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    //editTextLatitude.setText(Double.toString(location.getLatitude()));
                    //editTextLongitude.setText(Double.toString(location.getLongitude()));
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();



                } catch (Exception e) {
                    e.printStackTrace();
            }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateButtonOnclick(View view){
        double value = 0;
        if ((Latitude != value) && (Longitude != value))
        {
            long Time = System.currentTimeMillis();
            Integer timeNow = (int) Time;
            String TimeNow = timeNow.toString();
            databaseReference = FirebaseDatabase.getInstance().getReference("Location").push();
            databaseReference.child("latitude").push().setValue(String.valueOf(Latitude));
            databaseReference.child("longitude").push().setValue(String.valueOf(Longitude));
            databaseReference.child("timestamp").push().setValue(TimeNow).toString();
            //editTextMessage.setText("YOUR POSITION HAS BEEN SENT!");
            alertDialog = new AlertDialog.Builder(MapsActivity.this).setTitle("GREAT  !!! YOUR POSITION HAS BEEN SENT!!!").setMessage("PLEASE LEAVE YOUR APP IN BACKGROUND BY PRESSING THE DOWN CIRCULAR BUTTON AND DO NOT LOCK YOUR SCREEN !!! EVERY 30 SECONDS YOUR POSITION WILL BE DELETED FOR PRIVACY REASONS !!! THANK YOU !!! ").setCancelable(true).show();
            LatLng pos = new LatLng(Double.parseDouble(String.valueOf(Latitude)), Double.parseDouble(String.valueOf(Longitude)));
            mMap.addMarker(new MarkerOptions().position(pos).title("Tracked Person").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos , 14f));

        } else {
            alertDialog2 = new AlertDialog.Builder(MapsActivity.this).setTitle("Warning !!!").setMessage("The Coordinates of Your Location were not found! Please Restart the App!").setCancelable(true).show();
        }

        locationListener_button = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    //editTextLatitude.setText(Double.toString(location.getLatitude()));
                    //editTextLongitude.setText(Double.toString(location.getLongitude()));
                    mMap.clear();
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    long Time = System.currentTimeMillis();
                        Integer timeNow = (int) Time;
                        String TimeNow = timeNow.toString();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Location").push();
                        databaseReference.child("latitude").push().setValue(String.valueOf(Latitude));
                        databaseReference.child("longitude").push().setValue(String.valueOf(Longitude));
                        databaseReference.child("timestamp").push().setValue(TimeNow).toString();
                        LatLng pos = new LatLng(Double.parseDouble(String.valueOf(Latitude)), Double.parseDouble(String.valueOf(Longitude)));
                        mMap.addMarker(new MarkerOptions().position(pos).title("Tracked Person").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos , 14f));



                        // elimino posizioni se sono passate nel tempo di 10 secondi
                        databaseReference = FirebaseDatabase.getInstance().getReference("Location");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long time = System.currentTimeMillis();
                                Integer timeNow = (int) time;
                                for (DataSnapshot itemSnapshot : snapshot.getChildren())
                                    try {
                                        String end = itemSnapshot.child("timestamp").getValue().toString().substring(1, itemSnapshot.child("timestamp").getValue().toString().length() - 1);
                                        //convert String and long to int
                                        String[] End = end.split("=");
                                        String EndFinal = End[1];
                                        int endtime = Integer.parseInt(EndFinal.trim());


                                        Integer difference = timeNow - endtime;
                                        if (difference >= 20*1000 )  {
                                            itemSnapshot.getRef().removeValue();


                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                } catch (Exception e) {
                    e.printStackTrace();
                }










            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager_button = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        try {
            locationManager_button.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BUTTON, MIN_DIST_BUTTON, locationListener_button);
            locationManager_button.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BUTTON, MIN_DIST_BUTTON, locationListener_button);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


    public void updateButton(View view) throws InterruptedException {

        //alertDialog5 = new AlertDialog.Builder(MapsActivity.this).setTitle("Please Wait...").setMessage("I'm looking for positive potentials for COVID-19 around You ! ").setCancelable(true).show();
        double value = 0;
        if ((Latitude != value) && (Longitude != value)) {


            // Loop over all previous positions stored in firebase database

            databaseReference = FirebaseDatabase.getInstance().getReference("Location");
            int i = 0;
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            String databaseLatitudeString = ds.child("latitude").getValue().toString().substring(1, ds.child("latitude").getValue().toString().length() - 1);
                            String databaseLongitudeString = ds.child("longitude").getValue().toString().substring(1, ds.child("longitude").getValue().toString().length() - 1);

                            String[] stringLat = databaseLatitudeString.split(", ");
                            Arrays.sort(stringLat);
                            String latitude = stringLat[stringLat.length - 1].split("=")[1];

                            String[] stringLong = databaseLongitudeString.split(", ");
                            Arrays.sort(stringLong);
                            String longitude = stringLong[stringLong.length - 1].split("=")[1];


                            LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Tracked Person"));

                            double earthRadius = 3958.75;
                            double dLat = Math.toRadians(Double.parseDouble(latitude) - Latitude);
                            double dLng = Math.toRadians(Double.parseDouble(latitude) - Latitude);
                            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                                    Math.cos(Math.toRadians(Latitude)) * Math.cos(Math.toRadians(Double.parseDouble(latitude))) *
                                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
                            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                            double distance = earthRadius * c;
                            //arrayList.add(distance);
                            System.out.println(distance);
                            if (distance <= 0.060) {
                                arrayList.add(distance);
                            }
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            LatLng posit = new LatLng(Double.parseDouble(String.valueOf(Latitude)), Double.parseDouble(String.valueOf(Longitude)));
            mMap.addMarker(new MarkerOptions().position(posit).title("Your Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posit, 20f));
            //editTextMessage.setText("You have "+arrayList.size()+" People around You");
            Integer numPeople = arrayList.size();
            String numPeopleString = numPeople.toString();
            alertDialog3 = new AlertDialog.Builder(MapsActivity.this).setTitle("Warning !!!").setMessage("You have " + numPeopleString + " potential People positions around You. However, it is recommended to click again the button to check people for a more accurate search! To exit the Alert Dialog please click wherever you want on the map!").setCancelable(true).show();

            arrayList.clear();

        } else {
            alertDialog2 = new AlertDialog.Builder(MapsActivity.this).setTitle("Warning !!!").setMessage("The Coordinates of Your Location were not found! Please Restart the App!").setCancelable(true).show();
        }

    }

    public void pandemicButton(View view){
        Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
        myWebLink.setData(Uri.parse("https://www.google.com/search?rlz=1C1CHBF_itIT904IT904&ei=e2SYX_GyJNGVsAe0342wAw&q=" +
                "global+covid+pandemic&oq=globacovid+pandemic&gs_lcp=CgZwc3ktYWIQAxgAMggIABAHEB4QEzIECAAQEzIECAAQEzIECAAQEzIKCAAQCBAHEB4" +
                "QEzIKCAAQCBAHEB4QEzIKCAAQCBAHEB4QEzIKCAAQCBAHEB4QEzIKCAAQCBAHEB4QEzIKCAAQCBAHEB4QEzoJCAAQsAMQCBAeOggIABAIEAcQHjoHCAAQsQMQQzoCCAA6BAgAEA06BggAEAcQHlDBJljBOGCpSWgCcAB4AIABqQKIAe4KkgEFMC44LjG" +
                "YAQCgAQGqAQdnd3Mtd2l6yAEFwAEB&sclient=psy-ab"));
        startActivity(myWebLink);
    }









}
