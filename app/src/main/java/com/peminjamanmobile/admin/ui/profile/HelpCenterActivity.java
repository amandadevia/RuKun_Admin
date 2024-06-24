package com.peminjamanmobile.admin.ui.profile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.databinding.ActivityHelpCenterBinding;

public class HelpCenterActivity extends AppCompatActivity {

    private ActivityHelpCenterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpToolbar();
        moveToProfileFragment();

        setUpAction();
    }

    private void setUpAction() {
        binding.cvProceedToWhatsapp.setOnClickListener(v -> {
            String phoneNumber = "+62123544219";
            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        binding.tvDownloadWhatsapp.setOnClickListener(v -> {
            String appPackageName = "com.whatsapp";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
    }


    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.help_center);
    }

    private void moveToProfileFragment() {
        binding.toolbar.ivToolbarArrowBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}