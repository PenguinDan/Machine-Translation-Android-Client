package com.example.daniel.meetkai_test;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.daniel.meetkai_test.Fragments.MonitorFragment;
import com.example.daniel.meetkai_test.Fragments.SettingsFragment;
import com.example.daniel.meetkai_test.Fragments.TranslateFragment;
import com.example.daniel.meetkai_test.Utilities.UserUtilities;

public class MainActivityContainer extends Activity {
    // Fragments
    private TranslateFragment translateFragment;
    private MonitorFragment monitorFragment;
    private SettingsFragment settingsFragment;
    // Main Activity Container Views
    private BottomNavigationView bottomNavigationView;


    // All of the enumerator values
    public enum FragmentType{
        MONITOR,
        TRANSLATE,
        SETTINGS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Fragments
        translateFragment = new TranslateFragment();
        monitorFragment = new MonitorFragment();
        settingsFragment = new SettingsFragment();

        // Initialize views
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        createBottomNavigationView();
    }

    /***
     * Display a list of fragments for the user to choose
     */
    private BottomNavigationView.OnNavigationItemSelectedListener NavItemListen =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.monitor:
                            beginFragment(FragmentType.MONITOR);
                            break;
                        case R.id.translate:
                            beginFragment(FragmentType.TRANSLATE);
                            break;
                        case R.id.settings:
                            beginFragment(FragmentType.SETTINGS);
                            break;
                    }
                    return true;
                }
            };


    /**
     * Begins a fragment depending on the argument type
     *
     * @param fragmentType The fragment to change to
     */
    private void beginFragment(FragmentType fragmentType) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch(fragmentType) {
            case MONITOR:{
                fragmentTransaction.replace(R.id.main_display_frame, monitorFragment);
            }
            break;
            case TRANSLATE:{
                fragmentTransaction.replace(R.id.main_display_frame, translateFragment);
            }
            break;
            case SETTINGS:{
                fragmentTransaction.replace(R.id.main_display_frame, settingsFragment);
            }
            break;
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Creates the bottom navigation for the activity container. Sets the home screen as default.
     */
    private void createBottomNavigationView() {
        bottomNavigationView.setSelectedItemId(R.id.translate);
        bottomNavigationView.setOnNavigationItemSelectedListener(NavItemListen);
        beginFragment(FragmentType.TRANSLATE);
    }

    /**
     * Logs the user out and clears the system's cached information
     */
    public void logUserOut() {
        UserUtilities.clearUserCache(this);
        startActivity(new Intent(this, AuthenticationContainer.class));
        finish();
    }
}
