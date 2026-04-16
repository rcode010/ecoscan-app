package com.ecoscan.app.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ecoscan.app.ui.profile.ProfileFragment;
import com.ecoscan.app.R;
import com.ecoscan.app.ui.scan.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity is a host that holds all fragments and their FrameLayout container.
 * It controls which fragment to render based on the bottom navigation menu.
 * */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load HomeFragment by default
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_scan) {
                loadFragment(new ScanFragment());
            } else if (id == R.id.nav_notifications) {
                loadFragment(new NotificationsFragment());
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
            }
            return true;
        });
    }

    // Helper method to load fragments
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}