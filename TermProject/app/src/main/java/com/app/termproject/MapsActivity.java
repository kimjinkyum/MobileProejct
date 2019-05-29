package com.app.termproject;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;


    private int imageId;


    private View infoWindow;


    String city;
    double lat;
    double lng;
    String[] markedCities;
    /*
    reverseGeoHandler reHandler;
    geoHandler geoHan;
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        infoWindow = getLayoutInflater().inflate(R.layout.info, null);


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
        markedCities = new String[]{};


        placeMarker(37, 127);

        placeMarker(36, 127);

        placeMarker(37, 127.01);



        /*
        city = null;
        lat = 0;
        lng = 0;
        reHandler = new reverseGeoHandler();
        geoHan = new geoHandler();


        GetAddress reverseGeo = new GetAddress(37, 127,
                reHandler);
        reverseGeo.start();
        while(city == null)
        {
            continue;
        }
        GetCity getCity = new GetCity(city, geoHan);
        getCity.start();
        while((lat == 0) && (lng == 0))
        {
            continue;
        }

        //마커찍기

        // 여기 시간대를 좀 안 벌리니 이상한 곳에 좌표 찍히더라? 너무 빨리 마커찍으면 안 되는건가
        // 왠지 여기 로그캣 지우면 이상한 곳에 점 찍히더라
        Log.d("marker", "lat:" + lat);
        Log.d("marker", "lng:" + lng);
        LatLng target = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(target).title("target"));

        //그 후 city lat lng 초기화 null, 0으로
        city = null;
        lat = 0;
        lng = 0;
        */







        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-40, 160);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));




        LatLng next = new LatLng(-33, 150);
        mMap.addMarker(new MarkerOptions().position(next).title("next"));
        */


        mMap.setInfoWindowAdapter(new CustomInfoAdapter());


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().compareTo("Sydney") == 0) {
                    imageId = 0;
                } else if (marker.getTitle().compareTo("next") == 0) {
                    imageId = 1;
                }


                return false;
            }
        });


        LatLng center = new LatLng(36.5, 127.5);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 7));


        // Toast.makeText(this, "Toast onMapReady2", Toast.LENGTH_LONG).show();


    }


    public void placeMarker(double latitude, double longitude) {

        city = null;
        lat = 0;
        lng = 0;
        /*
        reHandler = new reverseGeoHandler();
        geoHan = new geoHandler();
        */


        Log.d("progress", "beforeGeo");

        try {
            GetAddress reverseGeo = new GetAddress(latitude, longitude);
            reverseGeo.start();
            reverseGeo.join();
        } catch (Exception e) {
            Log.e("userError", "Exception", e);
        }

        /*
        while(true)
        {
            if (city != null)
                break;

        }
        */


        try {
            GetCity getCity = new GetCity(city);
            getCity.start();
            getCity.join();
        } catch (Exception e) {
            Log.e("userError", "Exception", e);
        }
        /*
        while(true)
        {
            if ((lat != 0) && (lng != 0))
                break;
        }
        */
        Log.d("progress", "geoDone");

        //마커찍기
        if (!Arrays.asList(markedCities).contains(city)) {
            Log.d("marker", "lat:" + lat);
            Log.d("marker", "lng:" + lng);
            LatLng target = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(target).title("target"));
        }


        //그 후 city lat lng 초기화 null, 0으로
        city = null;
        lat = 0;
        lng = 0;


    }


    class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {


        @Override
        public View getInfoContents(Marker marker) {
            if (imageId == 0) {
                ((ImageView) infoWindow.findViewById(R.id.image)).setImageResource(R.drawable.arrow);

            } else if (imageId == 1) {
                ((ImageView) infoWindow.findViewById(R.id.image)).setImageResource(R.drawable.arrow2);
            }

            return infoWindow;
        }

        @Override
        public View getInfoWindow(Marker arg0) {

            return null;
        }


    }








/*
    public class reverseGeoHandler extends Handler{
        public void handleMessage(Message msg) {
            city = msg.getData().getString("city");
        }
    }

    public class geoHandler extends Handler{
        public void handleMessage(Message msg) {
            lat = msg.getData().getDouble("lat");
            lng = msg.getData().getDouble("lng");
        }
    }
*/


    public class GetCity extends Thread {
        String city;


        public GetCity(String input) {
            city = input;

        }


        @Override
        public void run() {
            super.run();

            String strUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + city +
                    "&language=ko,+CA&key=AIzaSyAIOKyyEy92A_eaxB4fBMEU6YwqtK-vYfU";
            HttpURLConnection clsConn = null;
            StringBuffer clsBuf = new StringBuffer();

            try {
                URL clsUrl = new URL(strUrl);
                clsConn = (HttpURLConnection) clsUrl.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(clsConn.getInputStream(), "UTF-8"));

                char[] arrBuf = new char[8192];
                int n;

                while ((n = in.read(arrBuf)) > 0) {
                    clsBuf.append(arrBuf, 0, n);
                }

                in.close();

                JSONObject json = new JSONObject(clsBuf.toString());

                JSONArray arrResults = json.getJSONArray("results");


                JSONObject clsItem = (JSONObject) arrResults.get(0);
                JSONObject clsItem2 = (JSONObject) clsItem.get("geometry");
                JSONObject clsItem3 = (JSONObject) clsItem2.get("location");

                    /*
            JSONArray arrAddress = clsItem.getJSONArray( "geometry" );
            JSONObject clsAddress = (JSONObject)arrAddress.get(2);
            */
                Log.d("latlng", clsItem3.getString("lat"));
                Log.d("latlng", clsItem3.getString("lng"));


                /*
                Bundle data = new Bundle();
                data.putDouble("lat", Double.parseDouble(clsItem3.getString( "lat" )));
                data.putDouble("lng", Double.parseDouble(clsItem3.getString( "lng" )));
                Message msg = handler.obtainMessage();
                msg.setData(data);
                handler.sendMessage(msg);
                */

                lat = Double.parseDouble(clsItem3.getString("lat"));
                lng = Double.parseDouble(clsItem3.getString("lng"));



            /*
            int iCount = arrResults.length( );
            for( int i = 0; i < iCount; ++i )
            {
                JSONObject clsItem = (JSONObject)arrResults.get( i );
                JSONArray arrAddress = clsItem.getJSONArray( "geometry" );


                int iAddressCount = arrAddress.length( );
                for( int j = 0; j < iAddressCount; ++j )
                {
                    JSONObject clsAddress = (JSONObject)arrAddress.get( j );

                    Log.d( "lat", clsAddress.getString( "location" ) );
                    // Toast.makeText(this, clsAddress.getString( "long_name" ), Toast.LENGTH_LONG).show();
                }
            }
            */

            } catch (Exception e) {
                Log.e("ConnectionError", "Exception", e);
            } finally {
                if (clsConn != null) clsConn.disconnect();
            }
        }
    }


    public class GetAddress extends Thread {
        double m_dbLatitude, m_dbLongitude;


        public GetAddress(double dbLatitude, double dbLongitude) {
            m_dbLatitude = dbLatitude;
            m_dbLongitude = dbLongitude;

        }


        @Override
        public void run() {
            super.run();

            String strUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                    + m_dbLatitude + "," + m_dbLongitude
                    + "&language=ko&region=KO,+CA&key=AIzaSyAIOKyyEy92A_eaxB4fBMEU6YwqtK-vYfU";
            HttpURLConnection clsConn = null;
            StringBuffer clsBuf = new StringBuffer();

            try {
                URL clsUrl = new URL(strUrl);
                clsConn = (HttpURLConnection) clsUrl.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(clsConn.getInputStream(), "UTF-8"));

                char[] arrBuf = new char[8192];
                int n;

                while ((n = in.read(arrBuf)) > 0) {
                    clsBuf.append(arrBuf, 0, n);
                }

                in.close();

                JSONObject json = new JSONObject(clsBuf.toString());

                JSONArray arrResults = json.getJSONArray("results");


                JSONObject clsItem = (JSONObject) arrResults.get(0);
                JSONArray arrAddress = clsItem.getJSONArray("address_components");
                JSONObject clsAddress = (JSONObject) arrAddress.get(3);
                Log.d("address", clsAddress.getString("long_name"));

                /*
                Bundle data = new Bundle();
                data.putString("city", clsAddress.getString( "long_name" ));
                Message msg = reHan.obtainMessage();
                msg.setData(data);
                reHan.sendMessage(msg);
                */

                city = clsAddress.getString("long_name");


            /*
            int iCount = arrResults.length( );
            for( int i = 0; i < iCount; ++i )
            {
                JSONObject clsItem = (JSONObject)arrResults.get( i );
                JSONArray arrAddress = clsItem.getJSONArray( "address_components" );


                int iAddressCount = arrAddress.length( );
                for( int j = 0; j < iAddressCount; ++j )
                {
                    JSONObject clsAddress = (JSONObject)arrAddress.get( j );

                    Log.d( "address", clsAddress.getString( "long_name" ) );
                    // Toast.makeText(this, clsAddress.getString( "long_name" ), Toast.LENGTH_LONG).show();
                }
            }
            */
            } catch (Exception e) {
                Log.e("ConnectionError", "Exception", e);
            } finally {
                if (clsConn != null) clsConn.disconnect();
            }
        }
    }


}
