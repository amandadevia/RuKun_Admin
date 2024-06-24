package com.peminjamanmobile.admin.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Notification implements Parcelable {

    private String idAjuan;
    private String borrowingStatus;
    private String borrowingStatusInformation;
    private String notificationTime;
    private Integer notificationIcon;
    private boolean isNew;
    private String timestamp;

    public String getIdAjuan() {
        return idAjuan;
    }

    public void setIdAjuan(String idAjuan) {
        this.idAjuan = idAjuan;
    }

    public String getBorrowingStatus() {
        return borrowingStatus;
    }

    public void setBorrowingStatus(String borrowingStatus) {
        this.borrowingStatus = borrowingStatus;
    }

    public String getBorrowingStatusInformation() {
        return borrowingStatusInformation;
    }

    public void setBorrowingStatusInformation(String borrowingStatusInformation) {
        this.borrowingStatusInformation = borrowingStatusInformation;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public Integer getNotificationIcon() {
        return notificationIcon;
    }

    public void setNotificationIcon(Integer notificationIcon) {
        this.notificationIcon = notificationIcon;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idAjuan);
        dest.writeString(borrowingStatus);
        dest.writeString(borrowingStatusInformation);
        dest.writeString(notificationTime);
        if (notificationIcon == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(notificationIcon);
        }
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeString(timestamp);
    }

    public Notification() {

    }

    private Notification(Parcel in) {
        idAjuan = in.readString();
        borrowingStatus = in.readString();
        borrowingStatusInformation = in.readString();
        notificationTime = in.readString();
        if (in.readByte() == 0) {
            notificationIcon = null;
        } else {
            notificationIcon = in.readInt();
        }
        isNew = in.readByte() != 0;
        timestamp = in.readString();
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
