package com.peminjamanmobile.admin.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.databinding.ActivityEditProfileBinding;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.ItemDialogBinding;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private CustomToastBinding toastBinding;
    private DatabaseReference userRef;
    private StorageReference storageReference;
    private String nip;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRef = FirebaseDatabase.getInstance().getReference("Admin");
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        moveToProfileFragment();
        setUpToolbar();
        setUpAction();

        // Get the NIP from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nip = sharedPreferences.getString("nip", "");

        if (!nip.isEmpty()) {
            Log.d("EditProfileActivity", "NIP from SharedPreferences: " + nip);
            loadUserProfileByNip(nip);
        } else {
            Log.d("EditProfileActivity", "NIP not found in SharedPreferences");
            Toast.makeText(this, "NIP not found in preferences.", Toast.LENGTH_SHORT).show();
        }

        binding.cvEditProfileImage.setOnClickListener(v -> openGallery());
        binding.btnEditProfile.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserProfileByNip(String nip) {
        Log.d("EditProfileActivity", "Loading profile for NIP: " + nip);
        Query query = userRef.orderByChild("nip").equalTo(nip);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("nama").getValue(String.class);
                        String nip = userSnapshot.child("nip").getValue(String.class);
                        String photoUrl = userSnapshot.child("foto").getValue(String.class);

                        if (name != null) {
                            binding.tvName.setText(name);
                        }
                        if (nip != null) {
                            binding.tvNip.setText(nip);
                        }
                        if (photoUrl != null) {
                            Glide.with(EditProfileActivity.this).load(photoUrl).into(binding.civProfilePicture);
                        }
                    }
                } else {
                    Log.d("EditProfileActivity", "Admin not found for NIP: " + nip);
                    showCustomToast("Admin not found.", R.color.red);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EditProfileActivity", "Error loading profile data", databaseError.toException());
                showCustomToast("Failed to load profile.", R.color.red);
            }
        });
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.ivToolbarArrowBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvToolbarTitle.setText(R.string.edit_profile);
    }

    private void moveToProfileFragment() {
        binding.toolbar.ivToolbarArrowBack.setOnClickListener(v -> onBackPressed());
    }

    private void setUpAction() {
        binding.civProfilePicture.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_profile_admin));
        // Disable editing for name and nip
        binding.tvName.setEnabled(false);
        binding.tvNip.setEnabled(false);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.civProfilePicture.setImageURI(imageUri);
        }
    }

    private void saveProfileChanges() {
        if (imageUri == null) {
            showCustomToast("Pilih gambar terlebih dahulu.", R.color.red);
            return;
        }

        final StorageReference fileRef = storageReference.child(nip + ".jpeg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        updateUserData(downloadUrl);
                    }).addOnFailureListener(e -> {
                        showCustomToast("Gagal mengunggah gambar.", R.color.red);
                        Log.e("EditProfileActivity", "Failed to get download URL.", e);
                    });
                })
                .addOnFailureListener(e -> {
                    showCustomToast("Gagal mengunggah gambar.", R.color.red);
                    Log.e("EditProfileActivity", "Failed to upload image.", e);
                });
    }

    private void updateUserData(String photoUrl) {
        Query query = userRef.orderByChild("nip").equalTo(nip);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userSnapshot.getRef().child("foto").setValue(photoUrl)
                                .addOnSuccessListener(aVoid -> {
                                    showEditProfileDialog();
                                })
                                .addOnFailureListener(e -> {
                                    showCustomToast("Gagal mengunggah gambar.", R.color.red);
                                    Log.e("EditProfileActivity", "Failed to update profile.", e);
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EditProfileActivity", "Error updating profile data", databaseError.toException());
                showCustomToast("Gagal mengunggah gambar.", R.color.red);
            }
        });
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle)
                .setCancelable(false);

        ItemDialogBinding dialogBinding = ItemDialogBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogBinding.ivDialogVector.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_edit_profile));
        dialogBinding.tvInformation.setText(R.string.want_to_edit_profile);

        dialogBinding.btnConfirm.setOnClickListener(v -> {
            toastBinding = CustomToastBinding.inflate(getLayoutInflater());

            toastBinding.toastText.setText(R.string.edit_profile_successfully);
            toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.green));

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastBinding.getRoot());
            toast.show();

            dialog.dismiss();
            finish();  // Close the activity
        });

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showCustomToast(String message, int color) {
        CustomToastBinding toastBinding = CustomToastBinding.inflate(getLayoutInflater());
        toastBinding.toastText.setText(message);
        toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(this, color));

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastBinding.getRoot());
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
