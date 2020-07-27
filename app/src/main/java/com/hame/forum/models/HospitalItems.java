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
    private int idHospital;
    private String hospitalName, city_name;

    public HospitalItems() {
    }

    public HospitalItems(int idHospital, String hospitalName, String city_name) {
        this.idHospital = idHospital;
        this.hospitalName = hospitalName;
        this.city_name = city_name;
    }

    protected HospitalItems(Parcel in) {
        idHospital = in.readInt();
        hospitalName = in.readString();
        city_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idHospital);
        dest.writeString(hospitalName);
        dest.writeString(city_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(int idHospital) {
        this.idHospital = idHospital;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
