package com.peminjamanmobile.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peminjamanmobile.admin.data.Notification;
import com.peminjamanmobile.admin.databinding.ItemRowNotificationBinding;
import com.peminjamanmobile.admin.ui.notification.NotificationActivity;

import java.util.ArrayList;

public class ListNotificationAdapter extends RecyclerView.Adapter<ListNotificationAdapter.ListViewHolder> {

    private final ArrayList<Notification> listNotification;
    private final NotificationActivity activity;

    public ListNotificationAdapter(ArrayList<Notification> list, NotificationActivity activity) {
        this.listNotification = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRowNotificationBinding binding = ItemRowNotificationBinding.inflate(inflater, parent, false);
        return new ListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Notification notification = listNotification.get(position);
        holder.bind(notification);

        holder.itemView.setOnClickListener(v -> {
            notification.setNew(false);
            activity.markNotificationAsRead(notification.getIdAjuan());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return listNotification.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        private final ItemRowNotificationBinding binding;

        public ListViewHolder(ItemRowNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Notification notification) {
            binding.imgItemIcon.setImageResource(notification.getNotificationIcon());
            binding.tvItemBorrowingStatus.setText(notification.getBorrowingStatus());
            binding.tvItemBorrowingStatusInformation.setText(notification.getBorrowingStatusInformation());
            binding.tvItemNotificationTime.setText(notification.getNotificationTime());

            if (notification.isNew()) {
                binding.imgItemNew.setVisibility(View.VISIBLE);
            } else {
                binding.imgItemNew.setVisibility(View.GONE);
            }
        }
    }
}
