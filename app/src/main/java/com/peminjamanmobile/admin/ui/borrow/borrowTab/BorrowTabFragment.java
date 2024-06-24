package com.peminjamanmobile.admin.ui.borrow.borrowTab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.adapter.ListBorrowAdapter;
import com.peminjamanmobile.admin.data.Borrow;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.FragmentBorrowTabBinding;
import com.peminjamanmobile.admin.ui.borrow.borrowDetail.BorrowDetailActivity;

import java.util.ArrayList;

public class BorrowTabFragment extends Fragment {

    private FragmentBorrowTabBinding binding;
    private final ArrayList<Borrow> list = new ArrayList<>();

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBorrowTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvBorrows.setHasFixedSize(true);

        list.addAll(getListBorrows());
        showRecyclerList();
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

    public static BorrowTabFragment newInstance(int sectionNumber) {
        BorrowTabFragment fragment = new BorrowTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
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

    private void showRecyclerList() {
        binding.rvBorrows.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        ListBorrowAdapter listBorrowAdapter = new ListBorrowAdapter(list);
        binding.rvBorrows.setAdapter(listBorrowAdapter);

        listBorrowAdapter.setOnItemClickCallback(data -> {
            showSelectedBorrow(data);

            Intent intentToDetail = new Intent(requireContext(), BorrowDetailActivity.class);
            intentToDetail.putExtra("DATA", data);
            startActivity(intentToDetail);
        });

        int index = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER, 0) : 0;
        String status = getStatusFromTabIndex(index);

        listBorrowAdapter.filterByStatus(status);
    }

    private String getStatusFromTabIndex(int index) {
        switch (index) {
            case 1:
                return "Diajukan";
            case 2:
                return "Ditolak";
            default:
                return "Diterima";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}