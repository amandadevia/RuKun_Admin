package com.peminjamanmobile.admin.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.adapter.ListBorrowAdapter;
import com.peminjamanmobile.admin.adapter.ListHomeRoomManagementAdapter;
import com.peminjamanmobile.admin.data.Borrow;
import com.peminjamanmobile.admin.data.RoomManagement;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.FragmentHomeBinding;
import com.peminjamanmobile.admin.ui.borrow.BorrowFragment;
import com.peminjamanmobile.admin.ui.borrow.borrowDetail.BorrowDetailActivity;
import com.peminjamanmobile.admin.ui.notification.NotificationActivity;
import com.peminjamanmobile.admin.ui.profile.ProfileFragment;
import com.peminjamanmobile.admin.ui.roomManagement.RoomManagementFragment;
import com.peminjamanmobile.admin.ui.roomManagement.editRoom.EditRoomActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final ArrayList<Borrow> listBorrow = new ArrayList<>();
    private final ArrayList<RoomManagement> listRoomManagement = new ArrayList<>();
    private DatabaseReference userRef;
    private DatabaseReference roomManagementRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference roomDatabaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRef = FirebaseDatabase.getInstance().getReference("Admin");
        roomManagementRef = FirebaseDatabase.getInstance().getReference("RoomManagement");
        roomDatabaseReference = FirebaseDatabase.getInstance().getReference("Ruangan");
        firebaseAuth = FirebaseAuth.getInstance();

        setUpToolbar();
        adjustStatusBarColor();
        setUpAction();

        setUpItemRecyclerBorrow();
        setUpItemRecyclerRoom();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nip = sharedPreferences.getString("nip", "");

        getUserNameFromDatabase(nip);
        fetchDataFromFirebase();
    }

    private void getUserNameFromDatabase(String nip) {
        Query query = userRef.orderByChild("nip").equalTo(nip);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("nama").getValue(String.class);
                        String photoUrl = userSnapshot.child("foto").getValue(String.class);

                        if (name != null) {
                            binding.textView.setText(name);
                        }
                        if (photoUrl != null) {
                            Glide.with(requireContext()).load(photoUrl).into(binding.civProfilePicture);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileFragment", "Error loading profile data", databaseError.toException());
            }
        });
    }

    private void setUpAction() {
        binding.toolbar.ivToolbarNotification.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), NotificationActivity.class);
            startActivity(intent);
        });

        binding.civProfilePicture.setOnClickListener(view -> {
            replaceFragment(new ProfileFragment());
        });

        binding.tvSeeAllLoanFacility.setOnClickListener(view -> {
            replaceFragment(new BorrowFragment());
        });

        binding.tvSeeAllRoom.setOnClickListener(view -> {
            replaceFragment(new RoomManagementFragment());
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public ArrayList<Borrow> getListBorrows() {
        String[] dataFacilityLoanId = getResources().getStringArray(R.array.facility_loan_ids);
        String[] dataFacilityLoanName = getResources().getStringArray(R.array.facility_loan_names);
        String[] dataFacilityLoanDate = getResources().getStringArray(R.array.facility_loan_dates);
        String[] dataFacilityLoanTime = getResources().getStringArray(R.array.facility_loan_times);
        String[] dataFacilityLoanStatus = getResources().getStringArray(R.array.facility_loan_statuses);
        String[] dataFacilityLoanLocation = getResources().getStringArray(R.array.facility_loan_locations);
        String[] dataFacilityLoanBorrowerName = getResources().getStringArray(R.array.facility_loan_borrower_names);
        String[] dataFacilityLoanBorrowerId = getResources().getStringArray(R.array.facility_loan_borrower_ids);
        String[] dataFacilityLoanActivityDescription = getResources().getStringArray(R.array.facility_loan_activity_descriptions);
        String[] dataFacilityLoanDocument = getResources().getStringArray(R.array.facility_loan_documents);
        TypedArray dataFacilityLoanImages = getResources().obtainTypedArray(R.array.facility_loan_images);
        TypedArray dataFacilityLoanBorrowerImages = getResources().obtainTypedArray(R.array.facility_loan_borrower_images);

        ArrayList<Borrow> listBorrows = new ArrayList<>();

        for (int i = 0; i < dataFacilityLoanId.length; i++) {
            Borrow borrow = new Borrow();
            borrow.setFacilityLoanId(dataFacilityLoanId[i]);
            borrow.setFacilityLoanName(dataFacilityLoanName[i]);
            borrow.setFacilityLoanDate(dataFacilityLoanDate[i]);
            borrow.setFacilityLoanTime(dataFacilityLoanTime[i]);
            borrow.setFacilityLoanStatus(dataFacilityLoanStatus[i]);
            borrow.setFacilityLoanLocation(dataFacilityLoanLocation[i]);
            borrow.setFacilityLoanBorrowerName(dataFacilityLoanBorrowerName[i]);
            borrow.setFacilityLoanBorrowerId(dataFacilityLoanBorrowerId[i]);
            borrow.setFacilityLoanActivityDescription(dataFacilityLoanActivityDescription[i]);
            borrow.setFacilityLoanDocument(dataFacilityLoanDocument[i]);
            borrow.setFacilityLoanImage(dataFacilityLoanImages.getResourceId(i, -1));
            borrow.setFacilityLoanBorrowerImage(dataFacilityLoanBorrowerImages.getResourceId(i, -1));

            listBorrows.add(borrow);
        }

        dataFacilityLoanImages.recycle();
        dataFacilityLoanBorrowerImages.recycle();

        return listBorrows;
    }

    @SuppressLint("SetTextI18n")
    private void showSelectedBorrow(Borrow borrow) {

        CustomToastBinding toastBinding = CustomToastBinding.inflate(getLayoutInflater());

        toastBinding.toastText.setText(borrow.getFacilityLoanName() + " dipilih");
        toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green));

        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastBinding.getRoot());
        toast.show();
    }

    private void showRecyclerBorrow() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvBorrows.setLayoutManager(layoutManager);
        ListBorrowAdapter listBorrowAdapter = new ListBorrowAdapter(listBorrow);
        binding.rvBorrows.setAdapter(listBorrowAdapter);

        listBorrowAdapter.setOnItemClickCallback(data -> {

            showSelectedBorrow(data);

            Intent intentToDetail = new Intent(requireContext(), BorrowDetailActivity.class);
            intentToDetail.putExtra("DATA", data);
            startActivity(intentToDetail);
        });
    }

    private void setUpItemRecyclerBorrow() {
        binding.rvBorrows.setHasFixedSize(true);

        listBorrow.clear();
        listBorrow.addAll(getListBorrows());
        showRecyclerBorrow();
    }

    private void fetchDataFromFirebase() {
        roomDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listRoomManagement.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RoomManagement roomManagement = snapshot.getValue(RoomManagement.class);
                    if (roomManagement != null) {
                        listRoomManagement.add(roomManagement);
                    }
                }
                showRecyclerRoom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HomeFragment", "Failed to read room data.", databaseError.toException());
            }
        });
    }

    private void showRecyclerRoom() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvRooms.setLayoutManager(layoutManager);
        ListHomeRoomManagementAdapter listHomeRoomManagementAdapter = new ListHomeRoomManagementAdapter(listRoomManagement);
        binding.rvRooms.setAdapter(listHomeRoomManagementAdapter);

        listHomeRoomManagementAdapter.setOnItemClickCallback(data -> {

            showSelectedRoom(data);


            Intent intentToDetail = new Intent(requireContext(), EditRoomActivity.class);
            intentToDetail.putExtra("DATA", data);
            startActivity(intentToDetail);
        });
    }

    private void setUpItemRecyclerRoom() {
        binding.rvRooms.setHasFixedSize(true);

        listRoomManagement.clear();
        fetchDataFromFirebase();
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

    private void setUpToolbar() {
        binding.toolbar.ivToolbarArrowBack.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.toolbar_home);
        binding.toolbar.tvToolbarTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
    }

    private void adjustStatusBarColor() {
        int statusBarColor = isNightMode()
                ? ContextCompat.getColor(requireActivity(), R.color.black)
                : ContextCompat.getColor(requireActivity(), R.color.orange_400);

        setStatusBarColor(statusBarColor);
    }

    private void setStatusBarColor(int color) {
        Window window = requireActivity().getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);

        if (isNightMode()) {
            window.getDecorView().setSystemUiVisibility(
                    window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private boolean isNightMode() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}