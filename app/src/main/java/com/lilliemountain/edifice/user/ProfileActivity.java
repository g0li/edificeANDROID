package com.lilliemountain.edifice.user;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.LoginActivity;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.R;

public class ProfileActivity extends AppCompatActivity {
    EditText name,flat,building,extrafeatures,parking,parkingslotno,vehiclemodel,twfourwheeler,emailid,mobileno,landline;
    DatabaseReference userUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.name);
        flat=findViewById(R.id.flat);
        building=findViewById(R.id.building);
        extrafeatures=findViewById(R.id.extrafeatures);
        parking=findViewById(R.id.parking);
        parkingslotno=findViewById(R.id.parkingslotno);
        vehiclemodel=findViewById(R.id.vehiclemodelno);
        twfourwheeler=findViewById(R.id.twofourwheeler);
        emailid=findViewById(R.id.email);
        mobileno=findViewById(R.id.mobile);
        landline=findViewById(R.id.landline);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference instance=database.getReference(getString(R.string.instance));
        DatabaseReference users=instance.child("users");
        final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
         userUID=users.child(uid);
        userUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                user=users;
                name.setText(users.getName());
                flat.setText(users.getFlat());
                getSupportActionBar().setSubtitle("e: "+ users.getEmailid());
                getSupportActionBar().setTitle("n: "+users.getName());
                building.setText(users.getBuilding());
                extrafeatures.setText(users.getExtrafeatures());
                parking.setText(users.getParking());
                parkingslotno.setText(users.getSlotno());
                vehiclemodel.setText(users.getVmodel());
                twfourwheeler.setText(users.getTwofourwheel());
                emailid.setText(users.getEmailid());
                mobileno.setText(users.getMobile());
                landline.setText(users.getLandline());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setVmodel(vehiclemodel.getText().toString());
                user.setLandline(landline.getText().toString());
                user.setMobile(mobileno.getText().toString());
                userUID.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
            }
        });
    }
    Users user;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                return true;
            case R.id.developer:
                String url = "https://www.lilliemountain.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.share:
                String shareBody = getString(R.string.share_text);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Edifice");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using..."));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
