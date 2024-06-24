package com.peminjamanmobile.admin.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.data.RoomManagement;
import com.peminjamanmobile.admin.databinding.CustomToastBinding;
import com.peminjamanmobile.admin.databinding.ItemDialogBinding;
import com.peminjamanmobile.admin.databinding.ItemRowHomeRoomManagementBinding;

import java.util.ArrayList;

public class ListHomeRoomManagementAdapter extends RecyclerView.Adapter<ListHomeRoomManagementAdapter.ListViewHolder> {

    private final ArrayList<RoomManagement> listRoomManagement;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListHomeRoomManagementAdapter(ArrayList<RoomManagement> list) {
        this.listRoomManagement = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRowHomeRoomManagementBinding binding = ItemRowHomeRoomManagementBinding.inflate(inflater, parent, false);
        return new ListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        RoomManagement roomManagement = listRoomManagement.get(position);
        if (roomManagement != null) {
            holder.bind(roomManagement);
        }

        holder.binding.cvEditRoom.setOnClickListener(v -> onItemClickCallback.onItemClicked(listRoomManagement.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listRoomManagement.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        private final ItemRowHomeRoomManagementBinding binding;
        private final DatabaseReference roomDatabaseReference;

        public ListViewHolder(ItemRowHomeRoomManagementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.roomDatabaseReference = FirebaseDatabase.getInstance().getReference("Ruangan");
        }

        @SuppressLint("ResourceAsColor")
        public void bind(RoomManagement roomManagement) {
            // Menampilkan data RoomManagement ke tampilan
            Glide.with(itemView.getContext())
                    .load(roomManagement.getImage()) // Memuat gambar dari URL (String)
                    .into(binding.ivFacilityLoanImage);

            binding.tvFacilityLoanName.setText(roomManagement.getNama());
            binding.tvFacilityLoanLocation.setText(roomManagement.getLokasi());

            binding.cvDeleteRoom.setOnClickListener(v -> showDeleteRoomDialog());
        }

        private void showDeleteRoomDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext(), R.style.DialogStyle)
                    .setCancelable(false);

            ItemDialogBinding dialogBinding = ItemDialogBinding.inflate(LayoutInflater.from(itemView.getContext()));
            builder.setView(dialogBinding.getRoot());

            AlertDialog dialog = builder.create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            dialogBinding.ivDialogVector.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.iv_delete_room));
            dialogBinding.tvInformation.setText(R.string.want_to_delete_room);
            dialogBinding.btnConfirm.setText(R.string.button_dialog_delete);
            dialogBinding.btnCancel.setText(R.string.button_dialog_cancel);

            dialogBinding.btnConfirm.setOnClickListener(v -> {
                CustomToastBinding toastBinding = CustomToastBinding.inflate(LayoutInflater.from(itemView.getContext()));

                toastBinding.toastText.setText(R.string.delete_room_successfully);
                toastBinding.customToastContainer.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.green));

                Toast toast = new Toast(itemView.getContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(toastBinding.getRoot());
                toast.show();

                dialog.dismiss();
            });

            dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(RoomManagement data);
    }
}
