package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentItems implements Parcelable {
    public static final Creator<CommentItems> CREATOR = new Creator<CommentItems>() {
        @Override
        public CommentItems createFromParcel(Parcel in) {
            return new CommentItems(in);
        }

        @Override
        public CommentItems[] newArray(int size) {
            return new CommentItems[size];
        }
    };
    private int id_comment;
    private String id_user, new_comment, date_comment, time_comment, user_name;

    public CommentItems() {
    }

    public CommentItems(int id_comment, String id_user, String new_comment, String date_comment, String time_comment, String user_name) {
        this.id_comment = id_comment;
        this.id_user = id_user;
        this.new_comment = new_comment;
        this.date_comment = date_comment;
        this.time_comment = time_comment;
        this.user_name = user_name;
    }

    protected CommentItems(Parcel in) {
        id_comment = in.readInt();
        id_user = in.readString();
        new_comment = in.readString();
        date_comment = in.readString();
        time_comment = in.readString();
        user_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_comment);
        dest.writeString(id_user);
        dest.writeString(new_comment);
        dest.writeString(date_comment);
        dest.writeString(time_comment);
        dest.writeString(user_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNew_comment() {
        return new_comment;
    }

    public void setNew_comment(String new_comment) {
        this.new_comment = new_comment;
    }

    public String getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(String date_comment) {
        this.date_comment = date_comment;
    }

    public String getTime_comment() {
        return time_comment;
    }

    public void setTime_comment(String time_comment) {
        this.time_comment = time_comment;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
