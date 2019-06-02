package com.app.termproject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<String> inputLat;
    ArrayList<String> inputLng;
    ArrayList<String> inputUri;

    private GoogleMap mMap;
    private View infoWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        inputLat = (ArrayList<String>) getIntent().getSerializableExtra("lat");
        inputLng = (ArrayList<String>) getIntent().getSerializableExtra("lng");
        inputUri = (ArrayList<String>) getIntent().getSerializableExtra("uri");


        /*
        // test
        inputLat.add((float)37);
        inputLat.add((float)36);
        inputLng.add((float)127);
        inputLng.add((float)127);
        inputUrl.add("https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/KakaoTalk_20190531_175626070.jpg?alt=media&token=8e9ff651-340d-474a-9513-349ba2a3cab2");
        inputUrl.add("https://firebasestorage.googleapis.com/v0/b/termproject-12d58.appspot.com/o/imagesimage%3A216?alt=media&token=051350ac-6848-4474-bf5a-27a401dab41e");
        */

        infoWindow=getLayoutInflater().inflate(R.layout.info, null);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        /*
        // test
        LatLng tempLatLng = new LatLng(inputLat.get(0), inputLng.get(0));
        mMap.addMarker(new MarkerOptions().position(tempLatLng).title("0"));
        tempLatLng = new LatLng(inputLat.get(1), inputLng.get(1));
        mMap.addMarker(new MarkerOptions().position(tempLatLng).title("1"));
        */



        for (int i = 0; i < inputLat.size(); i++) {
            LatLng tempLatLng = new LatLng(Double.valueOf(inputLat.get(i)), Double.valueOf(inputLng.get(i)));
            mMap.addMarker(new MarkerOptions().position(tempLatLng).title(i + ""));
        }



        mMap.setInfoWindowAdapter(new CustomInfoAdapter());




        // 혹시 쓸 일이 있을 까 해서  남겨 놓는 onMarkerClick 리스너 - 현재 아무 것도 안함
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                /*
                if (marker.getTitle().compareTo("Sydney") == 0) {
                    imageId = 0;
                }

                else if (marker.getTitle().compareTo("next") == 0) {
                    imageId = 1;
                }
                */
                return false;
            }
        });



        LatLng center = new LatLng(36.5, 127.5);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 7));

    }


    class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {


        @Override
        public View getInfoContents(Marker marker) {

            return infoWindow;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            Picasso.with(getApplicationContext()).load(inputUri.get(Integer.valueOf
                    (marker.getTitle()))).placeholder(R.drawable.loading)
                    .into((ImageView)infoWindow.findViewById(R.id.image), new MarkerCallback((marker)));


            return null;
        }


    }



    public class MarkerCallback implements Callback {
        Marker marker = null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }

}
