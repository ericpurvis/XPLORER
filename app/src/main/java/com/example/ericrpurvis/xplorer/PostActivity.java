package com.example.ericrpurvis.xplorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private EditText editTextName, editTextDesc, editTextActType;
    private Button buttonPost;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_location);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText) findViewById(R.id.name);
        editTextDesc = (EditText) findViewById(R.id.description);
        editTextActType = (EditText) findViewById(R.id.activityType);

        buttonPost = (Button) findViewById(R.id.buttonPost);

        buttonPost.setOnClickListener(this);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

    }


    private void getInformation(){
        String name = editTextName.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();
        String actType = editTextActType.getText().toString().trim();

        LocationPost location = new LocationPost(name, desc, actType);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(location);

        Toast.makeText(this,"Location Saved", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {

        if(view == buttonPost){
            getInformation();
        }

    }
}
