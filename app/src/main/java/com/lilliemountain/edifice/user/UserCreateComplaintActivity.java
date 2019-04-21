package com.lilliemountain.edifice.user;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Complaint;
import com.lilliemountain.edifice.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCreateComplaintActivity extends AppCompatActivity {
    EditText title,date,complaint;
    Spinner category;
    FirebaseDatabase database;
    DatabaseReference instance,complaintRef,userUID,userUID2,complaint_type,users;
    List<String> complaintTypeList=new ArrayList<>();
    String userName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_complaint);
        title=findViewById(R.id.title);
        date=findViewById(R.id.date);
        complaint=findViewById(R.id.complaint);
        setTitle("New Complaint");
        String em= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        getSupportActionBar().setSubtitle("e :"+em);
        category=findViewById(R.id.category);
        database=FirebaseDatabase.getInstance();
        instance=database.getReference(getString(R.string.instance));
        complaintRef=instance.child("complaints");
        final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        userUID=complaintRef.child(uid);
        complaint_type=instance.child("complaint-types");
        users=instance.child("users");
        userUID2=users.child(uid);
        date.setKeyListener(null);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date datex = new Date();
        date.setText(dateFormat.format(datex));
        userUID2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName=dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        complaint_type.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintTypeList.clear();
                GenericTypeIndicator<List<String>> t=new GenericTypeIndicator<List<String>>() {};
                complaintTypeList.addAll(dataSnapshot.getValue(t));
                ArrayAdapter<String> complaintStringArrayAdapter=new ArrayAdapter<>(UserCreateComplaintActivity.this,android.R.layout.simple_spinner_item,complaintTypeList);
                category.setAdapter(complaintStringArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Complaint>> t=new GenericTypeIndicator<List<Complaint>>() {};
                 complaintList=new ArrayList<>();
                complaintList.addAll(dataSnapshot.getValue(t));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Complaint cplnt=new Complaint("",complaintTypeList.get(category.getSelectedItemPosition()),complaint.getText().toString(),date.getText().toString(),userName,"1",title.getText().toString(),uid);
                complaintList.add(cplnt);
                userUID.setValue(complaintList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                        Toast.makeText(UserCreateComplaintActivity.this, "Complaint Posted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    List<Complaint> complaintList;
}
