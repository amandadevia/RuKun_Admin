package com.peminjamanmobile.admin.ui.borrow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.adapter.ListBorrowAdapter;
import com.peminjamanmobile.admin.adapter.SectionsPagerAdapter;
import com.peminjamanmobile.admin.data.Borrow;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.FragmentBorrowBinding;
import com.peminjamanmobile.admin.ui.borrow.borrowDetail.BorrowDetailActivity;

import java.util.ArrayList;

public class BorrowFragment extends Fragment {

    private FragmentBorrowBinding binding;

    private static final int[] TAB_TITLES = new int[]{
            R.string.proposed,
            R.string.rejected,
            R.string.approved
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBorrowBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar();
        setStatusBarColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        setTabLayout();
    }

    private void setTabLayout() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(requireActivity());
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = binding.tabs;
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(getString(TAB_TITLES[position])))
                .attach();
    }

    private void setStatusBarColor(int color) {
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private void setUpToolbar() {
        binding.toolbar.ivToolbarNotification.setVisibility(View.GONE);
        binding.toolbar.ivToolbarArrowBack.setVisibility(View.GONE);
        binding.toolbar.tvToolbarTitle.setText(R.string.toolbar_borrow);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
