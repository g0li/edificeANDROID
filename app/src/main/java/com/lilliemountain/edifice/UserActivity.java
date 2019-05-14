package com.lilliemountain.edifice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.lilliemountain.edifice.user.ProfileActivity;
import com.lilliemountain.edifice.user.UserComplaintFragment;
import com.lilliemountain.edifice.user.UserEventsFragment;
import com.lilliemountain.edifice.user.UserLocalServicesActivity;
import com.lilliemountain.edifice.user.UserMaintenanceListFragment;
import com.lilliemountain.edifice.user.UserNoticeListFragment;

public class UserActivity extends AppCompatActivity implements UserNoticeListFragment.OnFragmentInteractionListener ,
        UserMaintenanceListFragment.OnFragmentInteractionListener,
        UserComplaintFragment.OnFragmentInteractionListener ,
        UserEventsFragment.OnFragmentInteractionListener,
        UserLocalServicesActivity.OnFragmentInteractionListener {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle("Notices");
                    openFragment(UserNoticeListFragment.newInstance("",""));
                    return true;
                case R.id.navigation_dashboard:
                    getSupportActionBar().setTitle("Maintenance");
                    openFragment(UserMaintenanceListFragment.newInstance("",""));
                    return true;
                case R.id.navigation_notifications:
                    getSupportActionBar().setTitle("Complaint");
                    openFragment(UserComplaintFragment.newInstance("",""));
                    return true;
                case R.id.navigation_events:
                    getSupportActionBar().setTitle("Events");
                    openFragment(UserEventsFragment.newInstance("",""));
                    return true;
                case R.id.navigation_local_services:
                    getSupportActionBar().setTitle("Local Services");
                    openFragment(UserLocalServicesActivity.newInstance("",""));
                    return true;
            }
            return false;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
                startActivity(new Intent(UserActivity.this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        openFragment(UserNoticeListFragment.newInstance("",""));
        getSupportActionBar().setTitle("Notices");
        String em="e: "+FirebaseAuth.getInstance().getCurrentUser().getEmail();
        getSupportActionBar().setSubtitle(em);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment,fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
