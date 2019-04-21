package com.lilliemountain.edifice;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Committee;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.create.CreateMaintenanceFragment;
import com.lilliemountain.edifice.create.CreateNoticeFragment;
import com.lilliemountain.edifice.create.CreateUserFragment;
import com.lilliemountain.edifice.list.CommitteeListFragment;
import com.lilliemountain.edifice.list.ComplaintListFragment;
import com.lilliemountain.edifice.list.MaintenanceListFragment;
import com.lilliemountain.edifice.list.NoticeListFragment;
import com.lilliemountain.edifice.list.ResidentListFragment;

import java.util.ArrayList;
import java.util.List;


public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ResidentListFragment.OnFragmentInteractionListener,
        CreateUserFragment.OnFragmentInteractionListener,
        NoticeListFragment.OnFragmentInteractionListener,
        CreateNoticeFragment.OnFragmentInteractionListener,
        MaintenanceListFragment.OnFragmentInteractionListener,
        CreateMaintenanceFragment.OnFragmentInteractionListener,
        ComplaintListFragment.OnFragmentInteractionListener,
        CommitteeListFragment.OnFragmentInteractionListener{
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         fab = findViewById(R.id.fab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        residentFragment();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        getSupportActionBar().setSubtitle("e:"+email);
    }

    void residentFragment()
    {
        getSupportActionBar().setTitle("Resident List");
        final ResidentListFragment residentListFragment = new ResidentListFragment();
        openFragment(residentListFragment);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(CreateUserFragment.newInstance("create"));
            }
        });

    }

    void noticeFragment()
    {
        getSupportActionBar().setTitle("Notice Board");
        final NoticeListFragment noticeListFragment = new NoticeListFragment();
        openFragment(noticeListFragment);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(CreateNoticeFragment.newInstance("create"));
            }
        });

    }

    void maintenanceFragment()
    {
        getSupportActionBar().setTitle("Maintenance File");
        final MaintenanceListFragment maintenanceListFragment = new MaintenanceListFragment();
        openFragment(maintenanceListFragment);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             openFragment(CreateMaintenanceFragment.newInstance("create",""));
            }
        });

    }
    void complaintFragment()
    {
        getSupportActionBar().setTitle("Complaint File");
        final ComplaintListFragment complaintListFragment = new ComplaintListFragment();
        openFragment(complaintListFragment);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//             openFragment(CreateMaintenanceFragment.newInstance("create",""));
            }
        });

    }
    void committeeFragment()
    {
        getSupportActionBar().setTitle("Committee");
        final CommitteeListFragment committeeListFragment = new CommitteeListFragment();
        openFragment(committeeListFragment);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCommittee(view);
            }
        });

    }

    Users users1;


    public void addCommittee(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_add_committee);
        final TextView email,building,flat;
        final Spinner role,name;
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference instance=database.getReference(getString(R.string.instance));
        DatabaseReference users=instance.child("users");
        name=dialog.findViewById(R.id.username);
        email=dialog.findViewById(R.id.email);
        building=dialog.findViewById(R.id.building);
        flat=dialog.findViewById(R.id.flat);
        role=dialog.findViewById(R.id.role);
        final DatabaseReference committee = instance.child("committee");

        final List<Users> namelisty=new ArrayList<>();
        final List<String> namelistyStr=new ArrayList<>();
        final List<Committee> Cnamelisty=new ArrayList<>();
        final List<String> CnamelistyStr=new ArrayList<>();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    Users u=child.getValue(Users.class);
                    namelisty.add(u);
                    namelistyStr.add(u.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        committee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    Committee u=child.getValue(Committee.class);
                    Cnamelisty.add(u);
                    CnamelistyStr.add(u.getName());
                }
                for (int i = 0; i < namelistyStr.size(); i++) {
                    if(namelistyStr.get(i).toLowerCase().equals(CnamelistyStr.get(i).toLowerCase()))
                    {
                        namelistyStr.remove(i);
                        namelisty.remove(i);
                    }
                }
                ArrayAdapter<String> namelistyStringArrayAdapter=new ArrayAdapter<>(DashboardActivity.this,android.R.layout.simple_list_item_1,namelistyStr);
                name.setAdapter(namelistyStringArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                users1=namelisty.get(position);
                email.setText(users1.getEmailid());
                building.setText(users1.getBuilding());
                flat.setText(users1.getFlat());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final List<String>tempLst=new ArrayList<>();
        tempLst.add("Member");
        tempLst.add("Secretary");
        tempLst.add("Manager");
        tempLst.add("Treasurer");
        ArrayAdapter<String> aa=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,tempLst);
        role.setAdapter(aa);
        dialog.show();
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Committee committeex=new Committee(users1.getBuilding(),users1.getEmailid(),users1.getFlat(),users1.getName(),users1.getMobile(),tempLst.get(role.getSelectedItemPosition()));
                committee.push().setValue(committeex).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DashboardActivity.this,committeex.getName()+" added as "+committeex.getRole()+" of the committee",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment,fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
        fab.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_residents) {
            residentFragment();
        } else if (id == R.id.nav_notice) {
            noticeFragment();
        } else if (id == R.id.nav_maintenaince) {
            maintenanceFragment();
        } else if (id == R.id.nav_complaints) {
            complaintFragment();
        }else if (id == R.id.nav_committee) {
            committeeFragment();
        } else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
        } else if (id == R.id.nav_share) {
            String shareBody = getString(R.string.share_text);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Edifice");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "share using..."));
        } else if (id == R.id.nav_send) {
            showDetails(fab);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showDetails(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_contact_team);
        dialog.findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","iprogramyourapp@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email from Edifice");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        dialog.findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
        dialog.show();
    }
    private void openWhatsApp() {
        PackageManager packageManager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=+918169777677&text=Hello lilliemountain";
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onFragmentInteraction(String uri) {
        Log.e( "onFragmentInteraction: ",uri );
        switch (uri)
        {
            case "ResidentListFragment":
            case "NoticeListFragment":
            case "MaintenanceListFragment":
                fab.setVisibility(View.VISIBLE);
                break;
            case "CreateUserFragment":
            case "CreateNoticeFragment":
            case "CreateMaintenanceFragment":
                fab.setVisibility(View.GONE);
                break;
        }
    }

}
