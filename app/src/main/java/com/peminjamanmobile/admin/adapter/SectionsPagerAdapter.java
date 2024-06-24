package com.peminjamanmobile.admin.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.peminjamanmobile.admin.ui.borrow.borrowTab.BorrowTabFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 3; // Number of pages/fragments

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return BorrowTabFragment.newInstance(position + 1);
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}

