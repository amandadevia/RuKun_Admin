package com.peminjamanmobile.admin.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.databinding.ActivityMainBinding;
import com.peminjamanmobile.admin.ui.borrow.BorrowFragment;
import com.peminjamanmobile.admin.ui.home.HomeFragment;
import com.peminjamanmobile.admin.ui.profile.ProfileFragment;
import com.peminjamanmobile.admin.ui.roomManagement.RoomManagementFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new HomeFragment());
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.manage_room) {
                fragment = new RoomManagementFragment();
            } else if (itemId == R.id.borrow) {
                fragment = new BorrowFragment();
            } else if (itemId == R.id.profile) {
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                replaceFragment(fragment);
                return true;
            } else {
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}