package com.example.lostandfound;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class CreateAdvertActivity extends AppCompatActivity {

    EditText nameEditText, phoneEditText, descriptionEditText, dateEditText, locationEditText;
    Button submitButton, getCurrentLocationButton;

    FirebaseFirestore fStore;
    RadioGroup postTypeRadioGroup;

    String postTypeString;

    private FusedLocationProviderClient fusedLocationClient;
    LatLng currentLocation;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_advert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        locationEditText = findViewById(R.id.locationEditText);


        submitButton = findViewById(R.id.submitButton);
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        postTypeRadioGroup = findViewById(R.id.postType);

        fStore = FirebaseFirestore.getInstance();


        //gets the post type and stores in postTypeString
        postTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    postTypeString = radioButton.getText().toString();
                }
            }
        });

        //create new item object and add to fireStore DB on submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, phone, description, date;
                LatLng location;

                name = nameEditText.getText().toString();
                phone = phoneEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                date = dateEditText.getText().toString();
                location = currentLocation;

                if (TextUtils.isEmpty(name)
                        || TextUtils.isEmpty(phone)
                        || TextUtils.isEmpty(description)
                        || TextUtils.isEmpty(date)
                        || TextUtils.isEmpty(postTypeString)) {
                    Toast.makeText(CreateAdvertActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                LostFoundItem newItem = new LostFoundItem(name, phone, description, date, postTypeString, location);

                DocumentReference documentReference = fStore.collection("lost_found_items").document(newItem.getId());

                Map<String, Object> item = new HashMap<>();
                item.put("name", newItem.getName());
                item.put("phone", newItem.getPhone());
                item.put("description", newItem.getDescription());
                item.put("date", newItem.getDate());
                item.put("UUID", newItem.getId());
                item.put("postType", newItem.getPostType());
                item.put("LatLng", newItem.getLatLng());

                documentReference.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateAdvertActivity.this, "item added to db", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateAdvertActivity.this, "Unable to add to DB", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(CreateAdvertActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(CreateAdvertActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    currentLocation = new LatLng(latitude, longitude);
                                    locationEditText.setText(currentLocation.toString());
                                }else{
                                    Toast.makeText(CreateAdvertActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });





    }

    private LatLng parseLatLngFromString(String locationString) {
        try {
            // Remove "lat/lng: " and parentheses from the string
            String cleanString = locationString.replace("lat/lng: ", "")
                    .replace("(", "")
                    .replace(")", "");
            String[] latLngArray = cleanString.split(", ");
            if (latLngArray.length == 2) {
                double latitude = Double.parseDouble(latLngArray[0]);
                double longitude = Double.parseDouble(latLngArray[1]);
                Toast.makeText(CreateAdvertActivity.this, "Successful parse", Toast.LENGTH_SHORT).show();
                return new LatLng(latitude, longitude);

            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        Toast.makeText(CreateAdvertActivity.this, "Null returned from LATLNG PARSE", Toast.LENGTH_SHORT).show();
        return null;

    }

}