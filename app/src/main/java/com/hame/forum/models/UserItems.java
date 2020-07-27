package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserItems implements Parcelable {
    public static final Creator<UserItems> CREATOR = new Creator<UserItems>() {
        @Override
        public UserItems createFromParcel(Parcel in) {
            return new UserItems(in);
        }

        @Override
        public UserItems[] newArray(int size) {
            return new UserItems[size];
        }
    };
    private String idUser, id_Country;
    private String user_name, user_first_name, user_birthday, user_sex, user_phone;
    private String user_email, user_profession, user_country;

    public UserItems() {
    }

    public UserItems(String idUser, String id_Country, String user_name, String user_first_name,
                     String user_birthday, String user_sex, String user_phone,
                     String user_email, String user_profession, String user_country) {
        this.idUser = idUser;
        this.id_Country = id_Country;
        this.user_name = user_name;
        this.user_first_name = user_first_name;
        this.user_birthday = user_birthday;
        this.user_sex = user_sex;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_profession = user_profession;
        this.user_country = user_country;
    }

    protected UserItems(Parcel in) {
        idUser = in.readString();
        id_Country = in.readString();
        user_name = in.readString();
        user_first_name = in.readString();
        user_birthday = in.readString();
        user_sex = in.readString();
        user_phone = in.readString();
        user_email = in.readString();
        user_profession = in.readString();
        user_country = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUser);
        dest.writeString(id_Country);
        dest.writeString(user_name);
        dest.writeString(user_first_name);
        dest.writeString(user_birthday);
        dest.writeString(user_sex);
        dest.writeString(user_phone);
        dest.writeString(user_email);
        dest.writeString(user_profession);
        dest.writeString(user_country);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId_Country() {
        return id_Country;
    }

    public void setId_Country(String id_Country) {
        this.id_Country = id_Country;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profession() {
        return user_profession;
    }

    public void setUser_profession(String user_profession) {
        this.user_profession = user_profession;
    }

    public String getUser_country() {
        return user_country;
    }

    public void setUser_country(String user_country) {
        this.user_country = user_country;
    }
}
