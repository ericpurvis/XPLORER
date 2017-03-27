package com.example.ericrpurvis.xplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Date;

import static java.lang.Math.toIntExact;


public class ViewLocationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewLocationTitle;
    private TextView textViewLocationType;
    private TextView textViewDescription;
    private Button buttonPostEvent;

    private static final String TAG = "ViewLocationActivity";
    private DatabaseReference databaseReference;
    String locationTitle;
    String locationType;
    String description;
    String ID;
    String[] listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);


        textViewLocationTitle = (TextView) findViewById(R.id.textViewLocationTitle);
        textViewLocationType = (TextView) findViewById(R.id.textViewLocationType);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        buttonPostEvent = (Button) findViewById(R.id.buttonPostEventFinal);

        ID = getIntent().getStringExtra("ID");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations").child(ID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationTitle = dataSnapshot.child("name").getValue().toString();
                locationType = dataSnapshot.child("actType").getValue().toString();
                description = dataSnapshot.child("desc").getValue().toString();

                textViewLocationTitle.setText(locationTitle);
                textViewLocationType.setText(locationType);
                textViewDescription.setText(description);
                long numChildrenL = dataSnapshot.child("events").getChildrenCount();
                int numChildren = (int) numChildrenL;
                listItems = new String[numChildren];
                int i = 0;
                for(DataSnapshot event : dataSnapshot.child("events").getChildren()){
                    listItems[i] = new Event(event.child("eventName").getValue().toString(),
                            event.child("description").getValue().toString(),
                            new Date((long) event.child("eventDateTime").child("time").getValue()),
                            event.child("locationId").getValue().toString()).toString();
                    i++;
                }
                if(numChildren == 0){
                    listItems = new String[1];
                    listItems[0] = "No events scheduled. Click \"Post Event\" to fix that!";
                }
                populateListView(listItems);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        buttonPostEvent.setOnClickListener(this);
    }

    private void populateListView(String[] listItems){
        //Create list


        //Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.activ_list_items,
                listItems);

        ListView list = (ListView) findViewById(R.id.listViewActivities);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        if(view == buttonPostEvent){

//            Toast.makeText(this, "Post Event Button Clicked",
//                Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PostEventActivity.class);
            intent.putExtra("ID", ID);

            startActivity(intent);
        }
    }

}
