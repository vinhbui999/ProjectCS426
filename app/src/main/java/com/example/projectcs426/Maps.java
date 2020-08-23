package com.example.projectcs426;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Maps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<HelperInfor> _helpers = new ArrayList<>();
    private ArrayList<Marker> _markers = new ArrayList<>();
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getInforHelper();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent (this, Login.class);
                this.startActivity(i);
                return true;
            case R.id.history:
                startActivity(new Intent(this, CurrentHelperHired.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInforHelper() {
        HelperInfor hlpr1 = new HelperInfor();
        HelperInfor hlpr2 = new HelperInfor();
        HelperInfor hlpr3 = new HelperInfor();

        readFromfileHelper(R.drawable.xuan_vinh, hlpr1);
        readFromfileHelper(R.drawable.ngoc_son, hlpr2);
        readFromfileHelper(R.drawable.le_duan, hlpr3);

        _helpers.add(hlpr1);
        _helpers.add(hlpr2);
        _helpers.add(hlpr3);


    }

    private void readFromfileHelper(int avt_ID, HelperInfor mhelper) {
        FileInputStream fis = null;
        String file_name = String.valueOf(avt_ID);

        try {
            fis = openFileInput(file_name + ".txt");
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String text = null;
            StringBuilder sb = new StringBuilder();
            int k = 8;
            while ((text = bufferedReader.readLine()) != null) {
                sb.append(text).append('\n');
                if (k == 8) {
                    mhelper.setHName(text);
                    k--;
                } else if (k == 7) {
                    mhelper.setPhone(text);
                    k--;
                } else if (k == 6) {
                    mhelper.setGender(text);
                    k--;
                } else if (k == 5) {
                    mhelper.setDOB(text);
                    k--;
                } else if (k == 4) {
                    mhelper.setAddress(text);
                    k--;
                } else if (k == 3) {
                    mhelper.setNotes(text);
                    k--;
                } else if (k == 2) {
                    mhelper.setRating(Float.parseFloat(text));
                    k--;
                } else if (k == 1) {
                    mhelper.setAvatar(Integer.parseInt(text));
                    k--;
                } else if (k == 0) {
                    mhelper.setAvailable(Boolean.parseBoolean(text));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void convertAddtoLatlng(HelperInfor helper) throws IOException {
        Geocoder gc = new Geocoder(this);

            List<Address> geoResults = gc.getFromLocationName(helper.getAddress(), 1);
            while (geoResults.size()==0) {
                geoResults = gc.getFromLocationName(helper.Address, 1);
            }
            if (geoResults.size()>0) {
                Address a = geoResults.get(0);
                helper.setLatLng(new LatLng(a.getLatitude(), a.getLongitude()));
            }
        //List<Address> list = gc.getFromLocationName(helper.Address, 1);
        //Address a = list.get(0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(int i=0; i< _helpers.size(); ++i){
            try {
                convertAddtoLatlng(_helpers.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        displayMarkers();

        mMap.setOnMarkerClickListener(this);
    }

    private void displayMarkers() {
        for(int i=0; i<_helpers.size();++i){

            _markers.add(mMap.addMarker(new MarkerOptions()
                    .position(_helpers.get(i).latLng)
                    .title(_helpers.get(i).getHName()))) ;

            _markers.get(i).setTag(i);

            /*
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(_helpers.get(i).getLatLng())     // Sets the center of the map to Mountain View
                    .zoom(15)                           // Sets the zoom
                    .bearing(90)                        // Sets the orientation of the camera to east
                    .tilt(30)                           // Sets the tilt of the camera to 30 degrees
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.762622, 106.660172),10));
    }
    Intent intentOut;

    @Override
    public boolean onMarkerClick(Marker marker) {
        HelperInfor tmp = new HelperInfor();
        int index = Integer.valueOf(String.valueOf(marker.getTag()));
        tmp = _helpers.get(Integer.valueOf(String.valueOf(marker.getTag())));

        intentOut = new Intent( Maps.this, HelperProfile.class);
        if(tmp!= null){
            Bundle arg = new Bundle();

            arg.putParcelable("helperInf", (Parcelable) tmp);

            intentOut.putExtra("bundle_arg", arg);

            startActivity(intentOut);
        }
        return false;
    }
}