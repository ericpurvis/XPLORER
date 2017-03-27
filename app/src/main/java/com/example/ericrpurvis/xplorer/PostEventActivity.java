package com.example.ericrpurvis.xplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextDescription, editTextDate, editTextTime;
    private TextView textViewLocation;
    private Button buttonPostEventFinal;
    private DatabaseReference databaseReference;
    boolean hasEvents;
    String ID, locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ID = getIntent().getStringExtra("ID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        // Get the reference for the database entry of the location with the ID passed by the intent extra
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations").child(ID);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextTime = (EditText) findViewById(R.id.editTextTime);
        buttonPostEventFinal = (Button) findViewById(R.id.buttonPostEventFinal);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("+++++PostEventActivity", dataSnapshot.child("name").getValue().toString());
                locationName = "Location: " + dataSnapshot.child("name").getValue().toString();
                Log.d("+++++PostEventActivity", locationName);
                textViewLocation.setText(locationName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonPostEventFinal.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == buttonPostEventFinal) {

//            Toast.makeText(this, "Post Button clicked",
//                Toast.LENGTH_LONG).show();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations").child(ID);

                postEvent();

            Intent intent = new Intent(this, ViewLocationActivity.class);
            intent.putExtra("ID", ID);

            startActivity(intent);
        }
    }

    public void postEvent(){
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dateString = editTextDate.getText().toString().trim();
        String timeString = editTextTime.getText().toString().trim();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");


        try {
            Date dateTime = df.parse(dateString + " " + timeString);
            Event event = new Event(name, description, dateTime, ID);
            databaseReference = databaseReference.child("events");
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(event);
        } catch (ParseException exception) {
            Toast.makeText(PostEventActivity.this, "The Date or Time fields were not valid." + dateString + " " + timeString, Toast.LENGTH_LONG).show();
        }
    }
}
