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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostOverview extends AppCompatActivity {

    LostFoundItem item;

    TextView postType, name, phone, description, date;

    Button removeButton;
    FirebaseFirestore fStore;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String postTypeString = intent.getStringExtra("postType");
        String nameString = intent.getStringExtra("name");
        String phoneString = intent.getStringExtra("phone");
        String descriptionString = intent.getStringExtra("description");
        String dateString = intent.getStringExtra("date");
        String id = intent.getStringExtra("id");



        fStore = FirebaseFirestore.getInstance();

        removeButton = findViewById(R.id.removeButton);

        postType = findViewById(R.id.postType);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);


        postType.setText(postTypeString);


        name.setText(nameString);


        phone.setText(phoneString);


        description.setText(descriptionString);


        date.setText(dateString);



        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fStore.collection("lost_found_items").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostOverview.this, "Post deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostOverview.this, ShowAllActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostOverview.this, "Unable to remove item", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostOverview.this, ShowAllActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });




    }
}