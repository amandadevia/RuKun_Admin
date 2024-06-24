package com.peminjamanmobile.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.peminjamanmobile.admin.R;
import com.peminjamanmobile.admin.data.RoomManagement;

import java.util.ArrayList;

public class ListRoomManagementAdapter extends RecyclerView.Adapter<ListRoomManagementAdapter.ViewHolder> {

    private final ArrayList<RoomManagement> listRoom;
    private OnItemClickCallback onItemClickCallback;

    public ListRoomManagementAdapter(ArrayList<RoomManagement> list) {
        this.listRoom = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_room_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomManagement room = listRoom.get(position);

        holder.tvRoomName.setText(room.getNama());
        holder.tvRoomLocation.setText(room.getLokasi());
        Glide.with(holder.itemView.getContext())
                .load(room.getImage())
                .into(holder.ivRoomImage);

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listRoom.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listRoom.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(RoomManagement data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomLocation;
        ImageView ivRoomImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tv_room_name);
            tvRoomLocation = itemView.findViewById(R.id.tv_room_location);
            ivRoomImage = itemView.findViewById(R.id.civ_room_image);
        }
    }
}