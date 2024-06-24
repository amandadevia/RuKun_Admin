package com.peminjamanmobile.admin.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.FragmentProfileBinding;
import com.peminjamanmobile.admin.databinding.ItemDialogBinding;
import com.peminjamanmobile.admin.ui.authentication.SignInActivity;
import com.peminjamanmobile.admin.ui.profile.EditProfileActivity;
import com.peminjamanmobile.admin.ui.profile.HelpCenterActivity;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private CustomToastBinding toastBinding;
    private DatabaseReference userRef;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRef = FirebaseDatabase.getInstance().getReference("Admin");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar();
        setUpActions();
        setStatusBarColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nip = sharedPreferences.getString("nip", "");

        if (!nip.isEmpty()) {
            Log.d("ProfileFragment", "NIP from SharedPreferences: " + nip);
            loadUserProfileByNip(nip);
        } else {
            Log.d("ProfileFragment", "NIP not found in SharedPreferences");
            Toast.makeText(requireContext(), "NIP not found in preferences.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProfileByNip(String nip) {
        Log.d("ProfileFragment", "Loading profile for NIP: " + nip);
        Query query = userRef.orderByChild("nip").equalTo(nip);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("nama").getValue(String.class);
                        String nip = userSnapshot.child("nip").getValue(String.class);
                        String position = userSnapshot.child("posisi").getValue(String.class);
                        String photoUrl = userSnapshot.child("foto").getValue(String.class);

                        if (name != null) {
                            binding.tvAdminName.setText(name);
                        }
                        if (nip != null) {
                            binding.tvAdminId.setText(nip);
                        }
                        if (position != null) {
                            binding.tvRole.setText(position);
                        }
                        if (photoUrl != null) {
                            Glide.with(requireContext()).load(photoUrl).into(binding.civProfilePicture);
                        }
                    }
                } else {
                    Log.d("ProfileFragment", "User not found for NIP: " + nip);
                    Toast.makeText(requireContext(), "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileFragment", "Error loading profile data", databaseError.toException());
                Toast.makeText(requireContext(), "Failed to load profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStatusBarColor(int color) {
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private void setUpActions() {
        binding.editProfileContainer.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        binding.helpCenterContainer.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), HelpCenterActivity.class);
            startActivity(intent);
        });

        binding.logoutContainer.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.DialogStyle)
                .setCancelable(false);

        ItemDialogBinding dialogBinding = ItemDialogBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogBinding.ivDialogVector.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.iv_logout));
        dialogBinding.tvInformation.setText(R.string.want_to_logout);
        dialogBinding.btnConfirm.setText(R.string.logout);

        dialogBinding.btnConfirm.setOnClickListener(v -> {
            toastBinding = CustomToastBinding.inflate(getLayoutInflater());

            toastBinding.toastText.setText(R.string.signout_successfully);
            toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green));

            Toast toast = new Toast(requireContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastBinding.getRoot());
            toast.show();

            firebaseAuth.signOut();
            Intent intent = new Intent(requireContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.ivToolbarArrowBack.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.toolbar_profile);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
