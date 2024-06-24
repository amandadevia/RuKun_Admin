package com.peminjamanmobile.admin.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Borrow implements Parcelable {

    private Integer facilityLoanImage;
    private String facilityLoanId;
    private String idLoanFacility;
    private String facilityLoanName;
    private String facilityLoanTime;
    private String facilityLoanStatus;
    private String facilityLoanDate;
    private String facilityLoanLocation;
    private Integer facilityLoanBorrowerImage;
    private String facilityLoanBorrowerName;
    private String facilityLoanBorrowerId;
    private String facilityLoanActivityDescription;
    private String facilityLoanDocument;

    // Constructor kosong
    public Borrow() {
    }

    // Constructor yang digunakan oleh Parcelable
    protected Borrow(Parcel in) {
        if (in.readByte() == 0) {
            facilityLoanImage = null;
        } else {
            facilityLoanImage = in.readInt();
        }
        facilityLoanId = in.readString();
        idLoanFacility = in.readString();
        facilityLoanName = in.readString();
        facilityLoanTime = in.readString();
        facilityLoanStatus = in.readString();
        facilityLoanDate = in.readString();
        facilityLoanLocation = in.readString();
        if (in.readByte() == 0) {
            facilityLoanBorrowerImage = null;
        } else {
            facilityLoanBorrowerImage = in.readInt();
        }
        facilityLoanBorrowerName = in.readString();
        facilityLoanBorrowerId = in.readString();
        facilityLoanActivityDescription = in.readString();
        facilityLoanDocument = in.readString();
    }

    public static final Creator<Borrow> CREATOR = new Creator<Borrow>() {
        @Override
        public Borrow createFromParcel(Parcel in) {
            return new Borrow(in);
        }

        @Override
        public Borrow[] newArray(int size) {
            return new Borrow[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (facilityLoanImage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(facilityLoanImage);
        }
        dest.writeString(facilityLoanId);
        dest.writeString(idLoanFacility);
        dest.writeString(facilityLoanName);
        dest.writeString(facilityLoanTime);
        dest.writeString(facilityLoanStatus);
        dest.writeString(facilityLoanDate);
        dest.writeString(facilityLoanLocation);
        if (facilityLoanBorrowerImage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(facilityLoanBorrowerImage);
        }
        dest.writeString(facilityLoanBorrowerName);
        dest.writeString(facilityLoanBorrowerId);
        dest.writeString(facilityLoanActivityDescription);
        dest.writeString(facilityLoanDocument);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter and Setter
    public Integer getFacilityLoanImage() {
        return facilityLoanImage;
    }

    public void setFacilityLoanImage(Integer facilityLoanImage) {
        this.facilityLoanImage = facilityLoanImage;
    }

    public String getFacilityLoanId() {
        return facilityLoanId;
    }

    public void setFacilityLoanId(String facilityLoanId) {
        this.facilityLoanId = facilityLoanId;
    }

    public String getIdLoanFacility() {
        return idLoanFacility;
    }

    public void setIdLoanFacility(String idLoanFacility) {
        this.idLoanFacility = idLoanFacility;
    }

    public String getFacilityLoanName() {
        return facilityLoanName;
    }

    public void setFacilityLoanName(String facilityLoanName) {
        this.facilityLoanName = facilityLoanName;
    }

    public String getFacilityLoanTime() {
        return facilityLoanTime;
    }

    public void setFacilityLoanTime(String facilityLoanTime) {
        this.facilityLoanTime = facilityLoanTime;
    }

    public String getFacilityLoanStatus() {
        return facilityLoanStatus;
    }

    public void setFacilityLoanStatus(String facilityLoanStatus) {
        this.facilityLoanStatus = facilityLoanStatus;
    }

    public String getFacilityLoanDate() {
        return facilityLoanDate;
    }

    public void setFacilityLoanDate(String facilityLoanDate) {
        this.facilityLoanDate = facilityLoanDate;
    }

    public String getFacilityLoanLocation() {
        return facilityLoanLocation;
    }

    public void setFacilityLoanLocation(String facilityLoanLocation) {
        this.facilityLoanLocation = facilityLoanLocation;
    }

    public Integer getFacilityLoanBorrowerImage() {
        return facilityLoanBorrowerImage;
    }

    public void setFacilityLoanBorrowerImage(Integer facilityLoanBorrowerImage) {
        this.facilityLoanBorrowerImage = facilityLoanBorrowerImage;
    }

    public String getFacilityLoanBorrowerName() {
        return facilityLoanBorrowerName;
    }

    public void setFacilityLoanBorrowerName(String facilityLoanBorrowerName) {
        this.facilityLoanBorrowerName = facilityLoanBorrowerName;
    }

    public String getFacilityLoanBorrowerId() {
        return facilityLoanBorrowerId;
    }

    public void setFacilityLoanBorrowerId(String facilityLoanBorrowerId) {
        this.facilityLoanBorrowerId = facilityLoanBorrowerId;
    }

    public String getFacilityLoanActivityDescription() {
        return facilityLoanActivityDescription;
    }

    public void setFacilityLoanActivityDescription(String facilityLoanActivityDescription) {
        this.facilityLoanActivityDescription = facilityLoanActivityDescription;
    }

    public String getFacilityLoanDocument() {
        return facilityLoanDocument;
    }

    public void setFacilityLoanDocument(String facilityLoanDocument) {
        this.facilityLoanDocument = facilityLoanDocument;
    }
}