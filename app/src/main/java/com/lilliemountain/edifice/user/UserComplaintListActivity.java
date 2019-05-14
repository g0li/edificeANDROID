package com.lilliemountain.edifice.user;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Committee;
import com.lilliemountain.edifice.POJO.Complaint;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class UserComplaintListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    UserComplainListActivityAdapter uCLAAdapter;
    FirebaseDatabase database;
    DatabaseReference instance, decider,userUID;
    List<Complaint> complaintList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complaint_list);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String id=getIntent().getStringExtra("id");
        switch (id){
            case"localservices":
                setTitle("Society Committee Members");
                String em2= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                getSupportActionBar().setSubtitle("e :"+em2);

                database=FirebaseDatabase.getInstance();
                instance=database.getReference(getString(R.string.instance));
                decider =instance.child("localservices");
                decider.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Committee> committees=new ArrayList<>();
                        for (DataSnapshot dataSnapshot1:
                             dataSnapshot.getChildren()) {
                            Committee committee=dataSnapshot1.getValue(Committee.class);
                            committees.add(committee);
                        }
                        UserCommiteeListActivityAdapter userCommiteeListActivityAdapter=new UserCommiteeListActivityAdapter(committees);
                        recyclerView.setAdapter(userCommiteeListActivityAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case "comlist":
                setTitle("Complaint List");
                database=FirebaseDatabase.getInstance();
                instance=database.getReference(getString(R.string.instance));
                decider =instance.child("complaints");
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String em= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                getSupportActionBar().setSubtitle("e :"+em);
                userUID= decider.child(uid);
                userUID.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<Complaint>> t=new GenericTypeIndicator<List<Complaint>>() {};
                        complaintList.clear();
                        try {
                            complaintList.addAll(dataSnapshot.getValue(t));
                        } catch (Exception e) {
                        }
                        uCLAAdapter=new UserComplainListActivityAdapter(complaintList);
                        recyclerView.setAdapter(uCLAAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
        }
    }
}
