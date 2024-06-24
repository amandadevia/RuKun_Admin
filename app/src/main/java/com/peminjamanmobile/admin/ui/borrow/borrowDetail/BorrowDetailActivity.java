package com.peminjamanmobile.admin.ui.borrow.borrowDetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.data.Borrow;
import com.peminjamanmobile.admin.databinding.ActivityBorrowDetailBinding;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.ItemDialogBinding;
import com.peminjamanmobile.admin.ui.borrow.rejectionInformation.RejectionInformationActivity;

import android.content.res.Configuration;
import android.view.View;
import android.widget.Toast;

public class BorrowDetailActivity extends AppCompatActivity {

    private ActivityBorrowDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBorrowDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpListeners();
        receiveData();
        setUpToolbar();
        adjustStatusBarColor();
    }

    /**
     * @noinspection deprecation
     */
    private void setUpListeners() {
        binding.btnAccept.setOnClickListener(v -> {
            CustomToastBinding toastBinding = CustomToastBinding.inflate(getLayoutInflater());

            toastBinding.toastText.setText(R.string.loan_accept_successfully);
            toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.green));

            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastBinding.getRoot());
            toast.show();
        });
        binding.btnCancel.setOnClickListener(v -> showRejectDialog());
        binding.toolbar.ivToolbarArrowBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void receiveData() {
        Borrow borrowData = getIntent().getParcelableExtra("DATA");
        if (borrowData != null) {
            binding.civFacilityLoanBorrowerImage.setImageResource(borrowData.getFacilityLoanBorrowerImage());
            binding.ivFacilityLoanImage.setImageResource(borrowData.getFacilityLoanImage());
            binding.tvFacilityLoanStatus.setText(borrowData.getFacilityLoanStatus());
            binding.tvFacilityLoanBorrowerName.setText(borrowData.getFacilityLoanBorrowerName());
            binding.tvFacilityLoanBorrowerId.setText(borrowData.getFacilityLoanBorrowerId());
            binding.tvFacilityLoanName.setText(borrowData.getFacilityLoanName());
            binding.tvFacilityLoanLocation.setText(borrowData.getFacilityLoanLocation());
            binding.tvFacilityLoanId.setText(borrowData.getFacilityLoanId());
            binding.tvFacilityLoanDate.setText(borrowData.getFacilityLoanDate());
            binding.tvFacilityLoanTime.setText(borrowData.getFacilityLoanTime());
            binding.tvFacilityLoanActivityDescription.setText(borrowData.getFacilityLoanActivityDescription());
            binding.tvFacilityLoanPdf.setText(borrowData.getFacilityLoanDocument());

            Context context = binding.getRoot().getContext();

            int color;
            switch (borrowData.getFacilityLoanStatus()) {
                case "Diterima":
                    color = ContextCompat.getColor(context, R.color.green);
                    break;
                case "Diajukan":
                    color = ContextCompat.getColor(context, R.color.yellow);
                    break;
                default:
                    color = ContextCompat.getColor(context, R.color.red);
                    break;
            }

            binding.cvFacilityLoanStatus.setCardBackgroundColor(color);
        }
    }

    private void showRejectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(false);

        ItemDialogBinding dialogBinding = ItemDialogBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogBinding.ivDialogVector.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_loan_accept));
        dialogBinding.tvInformation.setText(R.string.want_to_reject);
        dialogBinding.btnConfirm.setText(R.string.reject);
        dialogBinding.btnCancel.setText(R.string.button_dialog_cancel);

        dialogBinding.btnConfirm.setOnClickListener(v -> {
            Borrow borrowData = getIntent().getParcelableExtra("DATA");

            Intent intent = new Intent(BorrowDetailActivity.this, RejectionInformationActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("borrowerName", binding.tvFacilityLoanBorrowerName.getText().toString());
            bundle.putString("borrowerId", binding.tvFacilityLoanBorrowerId.getText().toString());
            bundle.putString("loanId", binding.tvFacilityLoanId.getText().toString());

            if (borrowData != null) {
                bundle.putInt("borrowerImageResource", borrowData.getFacilityLoanBorrowerImage());
            }

            intent.putExtras(bundle);
            startActivity(intent);

            dialog.dismiss();
        });

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.borrow_detail);
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