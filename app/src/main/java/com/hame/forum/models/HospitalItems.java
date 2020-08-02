package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class HospitalItems implements Parcelable {

    public static final Creator<HospitalItems> CREATOR = new Creator<HospitalItems>() {
        @Override
        public HospitalItems createFromParcel(Parcel in) {
            return new HospitalItems(in);
        }

        @Override
        public HospitalItems[] newArray(int size) {
            return new HospitalItems[size];
        }
    };

    public HospitalItems() {
    }

    private String idHospital, hospitalName, hospitalAddress;

    public HospitalItems(String idHospital, String hospitalName, String hospitalAddress) {
        this.idHospital = idHospital;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
    }

    protected HospitalItems(Parcel in) {
        idHospital = in.readString();
        hospitalName = in.readString();
        hospitalAddress = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idHospital);
        dest.writeString(hospitalName);
        dest.writeString(hospitalAddress);
    }

    public String getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(String idHospital) {
        this.idHospital = idHospital;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }
}
