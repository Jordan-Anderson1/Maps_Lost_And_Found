package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class ShowAllActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    ArrayList<LostFoundItem> lostFoundItemList;
    TextView textView, description;

    RecyclerView recyclerView;

    RecyclerAdapter recyclerAdapter;
    Button returnHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_all);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        returnHomeButton = findViewById(R.id.returnHomeButton);

        description = findViewById(R.id.description);
        textView = findViewById(R.id.textView);

        fStore = FirebaseFirestore.getInstance();
        lostFoundItemList = new ArrayList<>();

        CollectionReference collectionReference = fStore.collection("lost_found_items");

        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        Map<String, Object> locationMap = (Map<String, Object>) documentSnapshot.get("LatLng");

                        if(locationMap != null ){
                            Double latitude = (Double) locationMap.get("latitude");
                            Double longitude = (Double) locationMap.get("longitude");

                            if(latitude != null & longitude != null){
                                Toast.makeText(ShowAllActivity.this, "values not null", Toast.LENGTH_SHORT).show();
                                LatLng latLng = new LatLng(latitude, longitude);
                                LostFoundItem item = new LostFoundItem(documentSnapshot.get("name").toString(),
                                        documentSnapshot.get("phone").toString(), documentSnapshot.get("description").toString(),
                                        documentSnapshot.get("date").toString(),
                                        documentSnapshot.get("postType").toString(), documentSnapshot.get("UUID").toString(), latLng);
                                lostFoundItemList.add(item);
                            }
                        }








                        recyclerView = findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ShowAllActivity.this));

                        recyclerAdapter = new RecyclerAdapter(lostFoundItemList);
                        recyclerView.setAdapter(recyclerAdapter);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowAllActivity.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });


        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAllActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}