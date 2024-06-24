package com.peminjamanmobile.admin.ui.roomManagement.editRoom;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.data.RoomManagement;
import com.peminjamanmobile.admin.databinding.ActivityEditRoomBinding;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;

public class EditRoomActivity extends AppCompatActivity {

    private ActivityEditRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditRoomBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        receiveData();
        setUpListeners();

        adjustStatusBarColor();
        setUpToolbar();
    }

    private void setUpListeners() {
        binding.btnEditRoom.setOnClickListener(v -> {
            CustomToastBinding toastBinding = CustomToastBinding.inflate(getLayoutInflater());

            toastBinding.toastText.setText(R.string.edit_room_successfully);
            toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.green));

            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastBinding.getRoot());
            toast.show();

            onBackPressed();
        });
        binding.toolbar.ivToolbarArrowBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void receiveData() {
        RoomManagement roomData = getIntent().getParcelableExtra("DATA");
        if (roomData != null) {
            Glide.with(this)
                    .load(roomData.getImage()) // Menggunakan Glide untuk memuat gambar dari URL
                    .into(binding.ivEditImage);
            binding.tietFacilityName.setText(roomData.getNama());
            binding.tietRoomLocation.setText(roomData.getLokasi());
        }
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.edit_room);
    }

    private void adjustStatusBarColor() {
        int statusBarColor = isNightMode()
                ? ContextCompat.getColor(this, R.color.black)
                : ContextCompat.getColor(this, R.color.white);

        setStatusBarColor(statusBarColor);
    }

    private void setStatusBarColor(int color) {
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);

        if (isNightMode()) {
            window.getDecorView().setSystemUiVisibility(
                    window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            window.getDecorView().setSystemUiVisibility(
                    window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private boolean isNightMode() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}
