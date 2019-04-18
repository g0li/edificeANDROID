package com.lilliemountain.edifice;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lilliemountain.edifice.create.CreateMaintenanceFragment;
import com.lilliemountain.edifice.create.CreateNoticeFragment;
import com.lilliemountain.edifice.create.CreateUserFragment;
import com.lilliemountain.edifice.list.ComplaintListFragment;
import com.lilliemountain.edifice.list.MaintenanceListFragment;
import com.lilliemountain.edifice.list.NoticeListFragment;
import com.lilliemountain.edifice.list.ResidentListFragment;

import java.net.URLEncoder;


public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ResidentListFragment.OnFragmentInteractionListener,
        CreateUserFragment.OnFragmentInteractionListener,
        NoticeListFragment.OnFragmentInteractionListener,
        CreateNoticeFragment.OnFragmentInteractionListener,
        MaintenanceListFragment.OnFragmentInteractionListener,
        CreateMaintenanceFragment.OnFragmentInteractionListener,
        ComplaintListFragment.OnFragmentInteractionListener{
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
