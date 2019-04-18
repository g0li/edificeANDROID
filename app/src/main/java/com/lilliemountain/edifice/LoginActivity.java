package com.lilliemountain.edifice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private  String TAG = LoginActivity.class.getSimpleName();
    EditText fieldEmail,fieldPassword;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference instance,admins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        fieldEmail=findViewById(R.id.fieldEmail);
        fieldPassword=findViewById(R.id.fieldPassword);
        findViewById(R.id.emailSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(fieldEmail.getText().toString(), fieldPassword.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    if(user!=null)
                                    {

                                        database=FirebaseDatabase.getInstance();
                                        instance=database.getReference(getString(R.string.instance));
                                        admins=instance.child("admins");
                                        admins.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot1:
                                                        dataSnapshot.getChildren()) {
                                                if(dataSnapshot1.getValue().toString().toLowerCase().equals(user.getEmail().toLowerCase())){
                                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                                }
                                                else {
                                                    startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                                }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });
            }
        });
    }
    FirebaseUser currentUser;
    @Override
    public void onStart() {
        super.onStart();
         currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {

            database=FirebaseDatabase.getInstance();
            instance=database.getReference(getString(R.string.instance));
            admins=instance.child("admins");
            admins.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:
                            dataSnapshot.getChildren()) {
                        if(dataSnapshot1.getValue().toString().toLowerCase().equals(currentUser.getEmail().toLowerCase())){
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, UserActivity.class));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
