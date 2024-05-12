package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostandfound.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    FirebaseFirestore fStore;

    ArrayList<LostFoundItem> lostFoundItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        lostFoundItemList = new ArrayList<>();

        fStore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = fStore.collection("lost_found_items");

        //get all items from db and add to a list
        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("Maps", "On Success listener reached");
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        Map<String, Object> locationMap = (Map<String, Object>) documentSnapshot.get("LatLng");

                        if(locationMap != null ){
                            Double latitude = (Double) locationMap.get("latitude");
                            Double longitude = (Double) locationMap.get("longitude");

                            if(latitude != null & longitude != null){

                                LatLng latLng = new LatLng(latitude, longitude);
                                LostFoundItem item = new LostFoundItem(documentSnapshot.get("name").toString(),
                                        documentSnapshot.get("phone").toString(), documentSnapshot.get("description").toString(),
                                        documentSnapshot.get("date").toString(),
                                        documentSnapshot.get("postType").toString(), documentSnapshot.get("UUID").toString(), latLng);
                                lostFoundItemList.add(item);
                            }
                        }

                        //iterate through list of items and create markers for each with the description as the title.
                        for (int i = 0; i < lostFoundItemList.size(); i++) {

                            Log.d("Lost/found item", lostFoundItemList.get(i).toString());
                            LatLng latLng = lostFoundItemList.get(i).getLatLng();
                            mMap.addMarker(new MarkerOptions().position(latLng).title(lostFoundItemList.get(i).getDescription()));

                        }

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                String title = marker.getTitle();
                                Log.d("maps", title);



                                return false;
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Maps", "On Failure listener reached");
                    }
                });

    }}