package com.example.childcare;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapTracker extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapTracker";
    private GoogleMap mMap;
    private ValueEventListener mListener;
    private static final String URL_DEMO = "https://www.gstatic.com/webp/gallery/4.sm.jpg";
    private static final String DB_NAME = "childCarModel";
    ArrayList<ChildModle>children;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    enum ChildChangeListener{
        dataChange,moved,delete
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //getChildrenLocation();
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //uploadDemo();
        getChildrenLocation();
    }

    public void showMarkers(ArrayList<ChildModle> locations){
        if(mMap!=null){
            LatLng mLocation = null;
            mMap.clear();
            for (int i = 0; i < locations.size(); i++) {
                mLocation = new LatLng(locations.get(i).getLocation().getLat(),locations.get(i).getLocation().getLng());
                //Log.d(TAG, "onMapReady location : "+locations.get(i).getLat()+locations.get(i).getLng());
                mMap.addMarker(new MarkerOptions().position(mLocation)
                        .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(this,locations.get(i).getImageUrl(),locations.get(i).getName()))));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation,10));
            }
            if(mLocation!=null){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                dbRef.child(DB_NAME).removeEventListener(mListener);
                dbRef.child(DB_NAME).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.d(TAG, "onChildChanged: child : "+dataSnapshot.getValue(ChildModle.class));
                        Log.d(TAG, "onChildChanged: child : "+s);
                        showSingleMark(dataSnapshot.getValue(ChildModle.class),ChildChangeListener.dataChange);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onChildRemoved: child : "+dataSnapshot.getValue(ChildModle.class));
                        showSingleMark(dataSnapshot.getValue(ChildModle.class),ChildChangeListener.delete);
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.d(TAG, "onChildMoved: child : "+dataSnapshot.getValue(ChildModle.class));
                        showSingleMark(dataSnapshot.getValue(ChildModle.class),ChildChangeListener.moved);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            // Add a marker in Sydney and move the camera
            // LatLng sydney = new LatLng(-34, 151);
            // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        }

    }
    @SuppressLint("CheckResult")
    public Bitmap createCustomMarker(Context context, String ur, String _name) {
        View marker = getLayoutInflater().inflate(R.layout.custom_marker_layout,null);
        CircleImageView markerImage =  marker.findViewById(R.id.user_dp);
        TextView txt_name = marker.findViewById(R.id.marker_child_name);
        txt_name.setText(_name);
        Glide.with(this).asBitmap().load(ur)
        .addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                Log.d(TAG, "onLoadFailed: fail load image");
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                markerImage.setImageBitmap(resource);
                Log.d(TAG, "onResourceReady: success");
                return true;
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(160, 160));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(displayMetrics.widthPixels, displayMetrics.widthPixels, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
    public void  getChildrenLocation(){
        ArrayList<ChildModle> child = new ArrayList<>();
         mListener = dbRef.child(DB_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                child.clear();
                for(DataSnapshot shot: dataSnapshot.getChildren()){
                    child.add(shot.getValue(ChildModle.class));
                    Log.d(TAG, "onDataChange: success");
                    Log.d(TAG, "onDataChange: ");
                }
                showMarkers(child);
                Log.d(TAG, "onDataChange: size : "+child.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: error");
            }
        });

    }
    public void showSingleMark(ChildModle child, ChildChangeListener state){
        Log.d(TAG, "showSingleMark: satate "+state+"\t"+child);
        LatLng loc = new LatLng(child.getLocation().getLat(),child.getLocation().getLng());
        switch (state){
            case delete:
                mMap.addMarker(new MarkerOptions().position(loc)).remove();
                //m.remove();
               // mMap.moveCamera(CameraUpdateFactory.zoomOut());
                break;
            case dataChange:
                mMap.addMarker(new MarkerOptions().position(loc)
                        .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(this,child.getImageUrl(),child.getName())))
                        .title(child.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                break;
        }
//        mMap.addMarker(new MarkerOptions().position(loc)
//                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(this,child.getImageUrl(),child.getName())))
//        .title(child.getName()));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListener!=null){
            FirebaseDatabase.getInstance().getReference().removeEventListener(mListener);
        }
    }



    public void uploadDemo(){



        String id1 ="1001";
        String id2 ="1002";
        String id3 ="1003";
        String id4 ="1004";

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = db.child("childCarModel");

        ChildModle model_1 = new ChildModle();
        ChildLocation loc_1 = new ChildLocation(28.583911,77.319116);
        model_1.setId(id1);
        model_1.setLocation(loc_1);
        model_1.setName("noah");
        model_1.setImageUrl(URL_DEMO);

        ChildModle model_3 = new ChildModle();
        ChildLocation loc_3 = new ChildLocation(28.583078, 77.313744);
        model_3.setId(id3);
        model_3.setLocation(loc_3);
        model_3.setName("mohamed");
        model_3.setImageUrl(URL_DEMO);

        ChildModle model_2 = new ChildModle();
        ChildLocation loc_2 = new ChildLocation(28.580903, 77.317408);
        model_2.setId(id2);
        model_2.setLocation(loc_2);
        model_2.setName("ahmed");
        model_2.setImageUrl(URL_DEMO);

        ChildModle model_4 = new ChildModle();
        ChildLocation loc_4 = new ChildLocation(28.580108, 77.315271);
        model_4.setId(id4);
        model_4.setLocation(loc_4);
        model_4.setName("belal");
        model_4.setImageUrl(URL_DEMO);

        ref.child(id1).setValue(model_1);
        ref.child(id2).setValue(model_2);
        ref.child(id3).setValue(model_3);
        ref.child(id4).setValue(model_4);

    }
    public  void unUsedCode(){
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);
                LatLng customMarkerLocationTwo = new LatLng(28.583078, 77.313744);
                LatLng customMarkerLocationThree = new LatLng(28.580903, 77.317408);
                LatLng customMarkerLocationFour = new LatLng(28.580108, 77.315271);
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapTracker.this,URL_DEMO,"Manish")))).setTitle("iPragmatech Solutions Pvt Lmt");
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapTracker.this,URL_DEMO,"Narender")))).setTitle("Hotel Nirulas Noida");

                mMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapTracker.this,URL_DEMO,"Neha")))).setTitle("Acha Khao Acha Khilao");
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapTracker.this,URL_DEMO,"Nupur")))).setTitle("Subway Sector 16 Noida");

                //LatLngBound will cover all your marker on Google Maps
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(customMarkerLocationOne); //Taking Point A (First LatLng)
                builder.include(customMarkerLocationThree); //Taking Point B (Second LatLng)
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mMap.moveCamera(cu);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        });
    }

}
