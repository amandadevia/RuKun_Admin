package com.peminjamanmobile.admin.ui.borrow.rejectionInformation;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.databinding.ActivityRejectionInformationBinding;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;

public class RejectionInformationActivity extends AppCompatActivity {

    private ActivityRejectionInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRejectionInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
        receiveData();
        setUpToolbar();
        adjustStatusBarColor();
    }

    /**
     * @noinspection deprecation
     */
    private void setListeners() {
        binding.toolbar.ivToolbarArrowBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnReject.setOnClickListener(v -> {
            CustomToastBinding toastBinding = CustomToastBinding.inflate(getLayoutInflater());

            toastBinding.toastText.setText(R.string.loan_reject_successfully);
            toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastBinding.getRoot());
            toast.show();

            onBackPressed();
        });
    }

    private void receiveData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String borrowerName = bundle.getString("borrowerName");
                String borrowerId = bundle.getString("borrowerId");
                String loanId = bundle.getString("loanId");
                int borrowerImageResource = bundle.getInt("borrowerImageResource", 0);

                binding.tvFacilityLoanBorrowerName.setText(borrowerName);
                binding.tvFacilityLoanBorrowerId.setText(borrowerId);
                binding.tvFacilityLoanId.setText(loanId);
                binding.civFacilityLoanBorrowerImage.setImageResource(borrowerImageResource);
            }
        }
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.reject_loan);
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