package com.ecoscan.app.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ecoscan.app.data.EcoScanDatabase;
import com.ecoscan.app.data.User.User;
import com.ecoscan.app.ui.profile.ProfileFragment;
import com.ecoscan.app.R;
import com.ecoscan.app.ui.scan.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MainActivity is a host that holds all fragments and their FrameLayout container.
 * It controls which fragment to render based on the bottom navigation menu.
 * */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create EcoScanUser if it doesn't exist at first startup
        insertUserIfNotExist();

        // Load HomeFragment by default
        loadFragment(new HomeFragment());

        // Set up bottom navigation menu with listeners
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
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

    private void insertUserIfNotExist() {
        /* *
         * executor on the following line is a thread used to run database queries.
         * Running database queries directly on the main thread causes bugs
         * The Room database enforces this behavior because in android apps the main thread is dedicated for ui only.
         * Running queries on the main thread causes the UI to freeze.
         * */
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute((() -> {
            EcoScanDatabase db = EcoScanDatabase.getInstance(this);
            User existingUser = db.userDao().getUser();

            if (existingUser == null) {
                db.userDao().insert(new User("EcoScan User"));
            }
        }));
    }

    // Helper method to load fragments
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}