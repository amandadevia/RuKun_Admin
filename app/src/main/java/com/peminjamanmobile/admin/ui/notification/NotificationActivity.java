package com.peminjamanmobile.admin.ui.notification;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.adapter.ListNotificationAdapter;
import com.peminjamanmobile.admin.data.Notification;
import com.peminjamanmobile.admin.databinding.ActivityNotificationBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private final ArrayList<Notification> list = new ArrayList<>();
    private ListNotificationAdapter listNotificationAdapter;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpToolbar();
        moveToHomeFragment();

        binding.rvNotifications.setHasFixedSize(true);
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("notification_prefs", MODE_PRIVATE);
        listNotificationAdapter = new ListNotificationAdapter(list, this);
        binding.rvNotifications.setAdapter(listNotificationAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("AjuanPeminjaman");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String namaRuangan = dataSnapshot.child("namaRuangan").getValue(String.class);
                    String idAjuan = dataSnapshot.child("idAjuan").getValue(String.class);
                    String timestamp = dataSnapshot.child("timestamp").getValue(String.class);

                    Notification notification = new Notification();
                    notification.setIdAjuan(idAjuan);
                    notification.setBorrowingStatus("Pengajuan Baru Masuk");
                    String message = "Pengajuan peminjaman ruangan " + namaRuangan + " dengan ID " + idAjuan + " masuk.";
                    notification.setBorrowingStatusInformation(message);
                    notification.setNotificationTime(getTimeAgo(timestamp));
                    notification.setNotificationIcon(R.drawable.ic_new_loan_facility);
                    notification.setNew(isNotificationNew(idAjuan));

                    list.add(0, notification);
                }
                listNotificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(NotificationActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.notification);
    }

    private void moveToHomeFragment() {
        binding.toolbar.ivToolbarArrowBack.setOnClickListener(v -> onBackPressed());
    }

    public boolean isNotificationNew(String idAjuan) {
        return sharedPreferences.getBoolean(idAjuan, true);
    }

    public void markNotificationAsRead(String idAjuan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(idAjuan, false);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private String getTimeAgo(String timestamp) {
        try {
            Date date = dateFormat.parse(timestamp);
            long time = date != null ? date.getTime() : 0;
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < TimeUnit.MINUTES.toMillis(1)) {
                return "baru saja";
            } else if (diff < TimeUnit.HOURS.toMillis(1)) {
                return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) + " menit";
            } else if (diff < TimeUnit.DAYS.toMillis(1)) {
                return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) + " jam";
            } else {
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " hari";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
