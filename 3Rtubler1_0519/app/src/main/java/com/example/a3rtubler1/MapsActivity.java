package com.example.a3rtubler1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()+"      "+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
            }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //LatLng myRoom = new LatLng(37.501177, 126.950338);//에이레네 자취방
        //mMap.addMarker(new MarkerOptions().position(myRoom).title("내 자취방 에이레네"));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myRoom,16));


        BitmapDrawable bitmapDrawable =(BitmapDrawable)getResources().getDrawable(R.drawable.greencafemarker);
        Bitmap b = bitmapDrawable.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120,120, false);


        //미국마커
        LatLng us = new LatLng(37.421721, -122.084032);
        MarkerOptions markerOptions4 = new MarkerOptions().position(us)
                .title("상도커피")
                .snippet("텀블러 사용시 할인(0)/텀블러 세척 서비스 제공(0)")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        mMap.addMarker(markerOptions4);





        //자취방근처카페 위경도지정
        LatLng cafe1 = new LatLng(37.501395, 126.949680);
        MarkerOptions markerOptions3 = new MarkerOptions().position(cafe1)
                 .title("상도역 커피나무")
                 .snippet("텀블러 사용시 할인/텀블러 세척 서비스 제공")
        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(markerOptions3);

        LatLng cafe2 = new LatLng(37.503557, 126.950406);
        MarkerOptions markerOptions1 = new MarkerOptions().position(cafe2)
                .title("상도역 펠어커피")
                .snippet("텀블러 사용시 할인")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(markerOptions1);

        LatLng cafe3 = new LatLng(37.498411, 126.952500);
        MarkerOptions markerOptions2 = new MarkerOptions().position(cafe3)
                .title("상도커피")
                .snippet("텀블러 사용시 할인/텀블러 세척 서비스 제공")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(cafe3));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cafe3, 17));
        mMap.addMarker(markerOptions2);

       // mMap.addMarker(markerOptions2);





        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("현위치");
               // .snippet("텀블러 사용시 할인(0)/텀블러 세척 서비스 제공(");
                //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
        //mMap.addMarker(markerOptions);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }


}
