package com.peminjamanmobile.admin.ui.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.databinding.ActivitySignInBinding;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.ui.MainActivity;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");

        setUpAction();
    }

    private void setUpAction() {
        binding.btnSignIn.setOnClickListener(view -> {
            String nip = binding.tietNip.getText().toString().trim();
            String password = binding.tietPassword.getText().toString().trim();

            if (nip.isEmpty()) {
                showCustomToast("Masukkan NIP", R.color.red);
                return;
            }

            if (password.isEmpty()) {
                showCustomToast("Masukkan Password", R.color.red);
                return;
            }

            loginUserAdmin(nip, password);
        });
    }

    private void loginUserAdmin(String nip, String password) {
        databaseReference.orderByChild("nip").equalTo(nip).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String dbPassword = userSnapshot.child("password").getValue(String.class);
                        if (dbPassword != null && dbPassword.equals(password)) {
                            // Admin login berhasil
                            showCustomToast("Admin login berhasil", R.color.green);

                            // Simpan NIP ke SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nip", nip);
                            editor.apply();

                            // Log untuk debugging
                            Log.d("SignInActivity", "NIP saved to SharedPreferences: " + nip);

                            // Jika login berhasil, lanjutkan ke MainActivity
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    // Jika password admin tidak cocok
                    showCustomToast("Password admin salah", R.color.red);
                } else {
                    // Jika NIP admin tidak ditemukan
                    showCustomToast("NIP admin tidak ditemukan", R.color.red);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showCustomToast("Database Error: " + databaseError.getMessage(), R.color.red);
            }
        });
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
