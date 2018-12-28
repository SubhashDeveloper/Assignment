package com.example.subhash.gmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;

import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    Marker marker2;
    String lat;
    String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*check permission*/
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                finish();
            }



            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        LatLng Delhi = new LatLng(28.6139, 77.2090);
        marker2= mMap.addMarker(new MarkerOptions().position(Delhi).title("New Delhi").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Delhi));
        CameraUpdate zoom=CameraUpdateFactory.newLatLngZoom(Delhi,10);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Delhi, 10));
        googleMap.animateCamera(zoom);
        lat=String.valueOf(Delhi.latitude);
        lng=String.valueOf(Delhi.longitude);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
                marker2.remove();
                LatLng latLng=marker.getPosition();
                marker.remove();
                addmarker(latLng);
                lat= String.valueOf(latLng.latitude);
                lng= String.valueOf(latLng.longitude);
    }

    private void addmarker(LatLng lng) {
        mMap.addMarker(new MarkerOptions().position(lng).title(lng.toString()).draggable(true));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "Marker Click", Toast.LENGTH_SHORT).show();
        final Data data=new Data();
        data.setLat(lat);
        data.setLng(lng);
        if (isInternetConnect())
        {
            sendData(lat,lng);
        }
        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
    protected boolean isInternetConnect() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void sendData(String lat, String lng) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String BASE_URL="http://afsanaonline.com/";
//        String BASE_URL="http://192.168.0.101/";
//        I have test it with my local server it properly work
        Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_URL).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson).create()).build();
        Restclient restclient=retrofit.create(Restclient.class);
        restclient.postData(lat,lng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Data user) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.videoply:
                Intent intent=new Intent(getApplicationContext(),VideoPlayer.class);
                startActivity(intent);
                break;
            case R.id.about:
                Toast.makeText(this, "Developed By Subhash", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
