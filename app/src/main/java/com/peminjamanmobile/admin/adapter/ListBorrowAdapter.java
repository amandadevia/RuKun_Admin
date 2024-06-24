package com.peminjamanmobile.admin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.data.Borrow;
import com.peminjamanmobile.admin.databinding.ItemRowBorrowBinding;

import java.util.ArrayList;

public class ListBorrowAdapter extends RecyclerView.Adapter<ListBorrowAdapter.ListViewHolder> {

    private final ArrayList<Borrow> listBorrow;
    private ArrayList<Borrow> originalList;

    private OnItemClickCallback onItemClickCallback;

    @SuppressLint("NotifyDataSetChanged")
    public void filterByStatus(String status) {
        ArrayList<Borrow> filteredList = new ArrayList<>();
        for (Borrow borrow : originalList) {
            if (borrow.getFacilityLoanStatus().equalsIgnoreCase(status)) {
                filteredList.add(borrow);
            }
        }
        listBorrow.clear();
        listBorrow.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListBorrowAdapter(ArrayList<Borrow> list) {
        this.listBorrow = list;
        this.originalList = list;
    }

    @NonNull
    @Override
    public ListBorrowAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRowBorrowBinding binding = ItemRowBorrowBinding.inflate(inflater, parent, false);
        return new ListViewHolder(binding);
    }

    /**
     * @noinspection deprecation
     */
    @Override
    public void onBindViewHolder(@NonNull final ListBorrowAdapter.ListViewHolder holder, int position) {
        Borrow borrow = listBorrow.get(position);
        if (borrow != null) {
            holder.bind(borrow);
        }

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listBorrow.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listBorrow.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        private final ItemRowBorrowBinding binding;

        public ListViewHolder(ItemRowBorrowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("ResourceAsColor")
        public void bind(Borrow borrow) {

            if (borrow.getFacilityLoanImage() != null) {
                binding.ivFacilityLoanImage.setImageResource(borrow.getFacilityLoanImage());
            }
            binding.tvFacilityLoanName.setText(borrow.getFacilityLoanName());
            binding.tvFacilityLoanDate.setText(borrow.getFacilityLoanDate());
            binding.tvFacilityLoanTime.setText(borrow.getFacilityLoanTime());
            binding.tvFacilityLoanId.setText(borrow.getFacilityLoanId());
            binding.tvFacilityLoanStatus.setText(borrow.getFacilityLoanStatus());

            Context context = binding.getRoot().getContext();

            int color;
            switch (borrow.getFacilityLoanStatus()) {
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

            binding.cvLoanFacilityStatus.setCardBackgroundColor(color);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Borrow data);
    }
}
