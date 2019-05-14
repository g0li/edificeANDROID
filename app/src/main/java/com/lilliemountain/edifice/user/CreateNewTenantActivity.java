package com.lilliemountain.edifice.user;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Tenant;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.R;

public class CreateNewTenantActivity extends AppCompatActivity {
    TextInputEditText tenantName,tenantphone,tenantemail,tenantbaseprice,tenantemergencyname,tenantemergencyphone;
    Tenant tenant;
    FirebaseDatabase database;
    DatabaseReference instance,tenantz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tenant);

        tenantName=findViewById(R.id.tenantName);
        tenantphone=findViewById(R.id.tenantphone);
        tenantemail=findViewById(R.id.tenantemail);
        tenantbaseprice=findViewById(R.id.tenantbaseprice);
        tenantemergencyname=findViewById(R.id.tenantemergencyname);
        tenantemergencyphone=findViewById(R.id.tenantemergencyphone);
        database=FirebaseDatabase.getInstance();
        instance=database.getReference(getString(R.string.instance));
        tenantz=instance.child("tenant-management");
        DatabaseReference userUID;
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference instance=database.getReference(getString(R.string.instance));
        DatabaseReference users=instance.child("users");
        final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        userUID=users.child(uid);

        userUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                getSupportActionBar().setSubtitle("e: "+ users.getEmailid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                tenant=new Tenant(tenantName.getText().toString(),
                        tenantphone.getText().toString(),
                        tenantemail.getText().toString(),
                        tenantbaseprice.getText().toString(),
                        tenantemergencyname.getText().toString(),
                        tenantemergencyphone.getText().toString(),
                                "https://robohash.org/"+tenantName.getText().toString().replace(" ","*").toLowerCase()+".png?set=set4");
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                tenantz.child(uid).push().setValue(tenant).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(v,"Tenant : "+tenant.getTenantName()+" added.",Snackbar.LENGTH_LONG).show();
                    }
                });
                onBackPressed();
            }
        });

    }
}
