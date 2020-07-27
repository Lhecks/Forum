package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ForumItems implements Parcelable {
    public static final Creator<ForumItems> CREATOR = new Creator<ForumItems>() {
        @Override
        public ForumItems createFromParcel(Parcel in) {
            return new ForumItems(in);
        }

        @Override
        public ForumItems[] newArray(int size) {
            return new ForumItems[size];
        }
    };
    private int id_forum, id_user;
    private String forum_subject, user_name;
    private String forum_time, date_forum;

    public ForumItems() {
    }

    public ForumItems(int id_forum) {
        this.id_forum = id_forum;
    }

    protected ForumItems(Parcel in) {
        id_forum = in.readInt();
        id_user = in.readInt();
        forum_subject = in.readString();
        user_name = in.readString();
        forum_time = in.readString();
        date_forum = in.readString();
    }

    public ForumItems(int id_forum, int id_user, String forum_subject, String user_name, String forum_time, String date_forum) {
        this.id_forum = id_forum;
        this.id_user = id_user;
        this.forum_subject = forum_subject;
        this.user_name = user_name;
        this.forum_time = forum_time;
        this.date_forum = date_forum;
    }

    public int getId_forum() {
        return id_forum;
    }

    public void setId_forum(int id_forum) {
        this.id_forum = id_forum;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getForum_subject() {
        return forum_subject;
    }

    public void setForum_subject(String forum_subject) {
        this.forum_subject = forum_subject;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getForum_time() {
        return forum_time;
    }

    public void setForum_time(String forum_time) {
        this.forum_time = forum_time;
    }

    public String getDate_forum() {
        return date_forum;
    }

    public void setDate_forum(String date_forum) {
        this.date_forum = date_forum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_forum);
        parcel.writeInt(id_user);
        parcel.writeString(forum_subject);
        parcel.writeString(user_name);
        parcel.writeString(forum_time);
        parcel.writeString(date_forum);
    }
}
