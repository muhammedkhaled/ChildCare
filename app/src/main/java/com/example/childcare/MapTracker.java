package com.example.childcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapTracker extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapTracker";
    private GoogleMap mMap;
    private ValueEventListener mListener;

    ArrayList<ChildrenLocation>children;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracker);
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

        showMarkers(demoObject());

    }

    public void showMarkers(ArrayList<ChildrenLocation> locations){
        if(mMap!=null){

            LatLng mLocation;

            for (int i = 0; i < locations.size(); i++) {
                mLocation = new LatLng(locations.get(i).getLat(),locations.get(i).getLng());
                Log.d(TAG, "onMapReady location : "+locations.get(i).getLat()+locations.get(i).getLng());

                mMap.addMarker(new MarkerOptions().position(mLocation)
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.project)))
                        .title(locations.get(i).getChildName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation,10));
            }

            // Add a marker in Sydney and move the camera
            // LatLng sydney = new LatLng(-34, 151);
            // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));


        }
    }

    public Bitmap getBitMap( int resource){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(100, 100, conf);
        Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

// modify canvas
        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.project), 0,0, color);
        canvas1.drawText("User Name!", 30, 40, color);

        return bmp;
    }

    public ArrayList<ChildrenLocation> demoObject(){
        ArrayList<ChildrenLocation> obj = new ArrayList<>();
        obj.add(new ChildrenLocation("-31.952854,115.857342"));
        obj.add(new ChildrenLocation("-33.87365, 151.20689"));
        obj.add(new ChildrenLocation("-27.47093, 153.0235"));
        //obj.add("mansoura");
        return obj;
    }

    public ArrayList<ChildrenLocation>  getChildrenLocation(){

        ArrayList<ChildrenLocation> children = new ArrayList<>();
         mListener = dbRef.child("location ").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot shot: dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // FirebaseDatabase.getInstance().getReference().removeEventListener(mListener);


        return null;
    }

    public class ChildrenLocation{
        String latLng;
        double lat;
        double lng;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        String childName;

        public ChildrenLocation(String latLng) {
            this.latLng = latLng;
            childName = "demoo";
            String [] loc = latLng.split(",");
            lat = Double.parseDouble(loc[0]);
            lng = Double.parseDouble(loc[1]);
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListener!=null){
            FirebaseDatabase.getInstance().getReference().removeEventListener(mListener);
        }
    }
}
