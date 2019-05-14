package com.lilliemountain.edifice.user;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Tenant;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.adapters.TenantAdapter;

import java.util.ArrayList;
import java.util.List;

public class TenantManagementActivity extends AppCompatActivity implements TenantAdapter.OnTenantListener {
    RecyclerView tenant;
    FirebaseDatabase database;
    DatabaseReference instance,tenantz;
    List<Tenant> list=new ArrayList<>();
    List<String> listKey=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_management);
        tenant=findViewById(R.id.tenants);
        database=FirebaseDatabase.getInstance();
        instance=database.getReference(getString(R.string.instance));
        tenantz=instance.child("tenant-management");
        tenant.setLayoutManager(new GridLayoutManager(this,2));
        tenantz.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                listKey.clear();
                for (DataSnapshot uid :
                        dataSnapshot.getChildren()) {
                    for (DataSnapshot tenants :
                            uid.getChildren()) {
                        listKey.add(tenants.getKey());
                        Tenant t=tenants.getValue(Tenant.class);
                        list.add(t);
                        TenantAdapter tenantAdapter=new TenantAdapter(list,TenantManagementActivity.this);
                        tenant.setAdapter(tenantAdapter);
                        Log.e("onDataChange: ",t.toString() );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TenantManagementActivity.this,CreateNewTenantActivity.class));
            }
        });
    }

    @Override
    public void RemoveTenant(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        tenantz.child(uid).child(listKey.get(position)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(TenantManagementActivity.this, "Tenant Removed", Toast.LENGTH_SHORT).show();
                                list.clear();
                                listKey.clear();
                                TenantAdapter tenantAdapter=new TenantAdapter(list,TenantManagementActivity.this);
                                tenant.setAdapter(tenantAdapter);
                            }
                        });
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    @Override
    public void SendSMS(String phone, String smsbody) {
        phoneNo=phone;
        message=smsbody;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    String phoneNo;
    String message;
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
