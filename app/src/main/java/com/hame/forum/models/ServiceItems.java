package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceItems implements Parcelable {
    public static final Creator<ServiceItems> CREATOR = new Creator<ServiceItems>() {
        @Override
        public ServiceItems createFromParcel(Parcel in) {
            return new ServiceItems(in);
        }

        @Override
        public ServiceItems[] newArray(int size) {
            return new ServiceItems[size];
        }
    };
    private int idService;
    private String ServiceName, hospitalName;

    public ServiceItems() {
    }

    public ServiceItems(int idService, String serviceName, String hospitalName) {
        this.idService = idService;
        this.ServiceName = serviceName;
        this.hospitalName = hospitalName;
    }

    public ServiceItems(int idService, String serviceName) {
        this.idService = idService;
        this.ServiceName = serviceName;
    }

    protected ServiceItems(Parcel in) {
        idService = in.readInt();
        ServiceName = in.readString();
        hospitalName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idService);
        dest.writeString(ServiceName);
        dest.writeString(hospitalName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
