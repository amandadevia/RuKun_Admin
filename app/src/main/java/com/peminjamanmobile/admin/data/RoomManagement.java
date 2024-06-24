package com.peminjamanmobile.admin.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomManagement implements Parcelable {
    private String idruangan;
    private String nama;
    private String lokasi;
    private String image;

    public RoomManagement() {
        // Default constructor required for calls to DataSnapshot.getValue(RoomManagement.class)
    }

    public RoomManagement(String idruangan, String nama, String lokasi, String image) {
        this.idruangan = idruangan;
        this.nama = nama;
        this.lokasi = lokasi;
        this.image = image;
    }

    protected RoomManagement(Parcel in) {
        idruangan = in.readString();
        nama = in.readString();
        lokasi = in.readString();
        image = in.readString();
    }

    public static final Creator<RoomManagement> CREATOR = new Creator<RoomManagement>() {
        @Override
        public RoomManagement createFromParcel(Parcel in) {
            return new RoomManagement(in);
        }

        @Override
        public RoomManagement[] newArray(int size) {
            return new RoomManagement[size];
        }
    };

    public String getId() {
        return idruangan;
    }

    public void setId(String id) {
        this.idruangan = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idruangan);
        parcel.writeString(nama);
        parcel.writeString(lokasi);
        parcel.writeString(image);
    }
}