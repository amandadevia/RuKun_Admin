package com.peminjamanmobile.admin.ui.roomManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.adapter.ListRoomManagementAdapter;
import com.peminjamanmobile.admin.data.RoomManagement;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.FragmentRoomManagementBinding;
import com.peminjamanmobile.admin.ui.roomManagement.addRoom.AddRoomActivity;
import com.peminjamanmobile.admin.ui.roomManagement.editRoom.EditRoomActivity;

import java.util.ArrayList;

public class RoomManagementFragment extends Fragment {

    private FragmentRoomManagementBinding binding;
    private final ArrayList<RoomManagement> list = new ArrayList<>();
    private DatabaseReference roomDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomDatabaseReference = FirebaseDatabase.getInstance().getReference("Ruangan");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar();

        binding.rvRooms.setHasFixedSize(true);
        binding.rvRooms.setLayoutManager(new LinearLayoutManager(requireContext()));

        fetchDataFromFirebase();

        setStatusBarColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        setListeners();
    }

    private void setListeners() {
        binding.fabAddRoom.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddRoomActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void showSelectedRoom(RoomManagement room) {

        CustomToastBinding toastBinding = CustomToastBinding.inflate(getLayoutInflater());

        toastBinding.toastText.setText(room.getNama() + " dipilih");
        toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green));

        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastBinding.getRoot());
        toast.show();
    }

    private void setStatusBarColor(int color) {
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private void fetchDataFromFirebase() {
        roomDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    RoomManagement room = roomSnapshot.getValue(RoomManagement.class);
                    list.add(room);
                }
                showRecyclerList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void showRecyclerList() {
        ListRoomManagementAdapter listRoomManagementAdapter = new ListRoomManagementAdapter(list);
        binding.rvRooms.setAdapter(listRoomManagementAdapter);

        listRoomManagementAdapter.setOnItemClickCallback(data -> {
            showSelectedRoom(data);
            Intent intentToDetail = new Intent(requireContext(), EditRoomActivity.class);
            intentToDetail.putExtra("DATA", data);
            startActivity(intentToDetail);
        });
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.ivToolbarArrowBack.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.toolbar_manage_room);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
