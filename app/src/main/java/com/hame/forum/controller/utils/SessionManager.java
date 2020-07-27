package com.hame.forum.controller.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hame.forum.models.UserItems;

import static com.hame.forum.controller.utils.Constant.LOGGED_IN_SHARED_PREF;
import static com.hame.forum.controller.utils.Constant.USER_BIRTHDAY;
import static com.hame.forum.controller.utils.Constant.ID_COUNTRY;
import static com.hame.forum.controller.utils.Constant.COUNTRY_NAME;
import static com.hame.forum.controller.utils.Constant.USER_EMAIL;
import static com.hame.forum.controller.utils.Constant.USER_FIRST_NAME;
import static com.hame.forum.controller.utils.Constant.USER_ID;
import static com.hame.forum.controller.utils.Constant.USER_NAME;
import static com.hame.forum.controller.utils.Constant.USER_PHONE;
import static com.hame.forum.controller.utils.Constant.USER_PROFESSION;
import static com.hame.forum.controller.utils.Constant.USER_SEX;


public class SessionManager {

    //Shared preferences file name
    private static final String PREF_NAME = "James";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void userLogin(UserItems user) {
        editor.putBoolean(LOGGED_IN_SHARED_PREF, true);
        editor.putString(USER_ID, String.valueOf(user.getIdUser()));
        editor.putString(USER_NAME, user.getUser_name());
        editor.putString(USER_FIRST_NAME, user.getUser_first_name());
        editor.putString(USER_EMAIL, user.getUser_email());
        editor.putString(USER_PHONE, String.valueOf(user.getUser_phone()));
        editor.putString(USER_BIRTHDAY, user.getUser_birthday());
        editor.putString(USER_SEX, user.getUser_sex());
        editor.putString(ID_COUNTRY, user.getId_Country());
        editor.putString(COUNTRY_NAME, user.getUser_country());
        editor.putString(USER_PROFESSION, user.getUser_profession());

        editor.apply();
    }


    public UserItems getUser() {
        return new UserItems(
                pref.getString(USER_ID, null),
                pref.getString(ID_COUNTRY, null),
                pref.getString(USER_NAME, null),
                pref.getString(USER_FIRST_NAME, null),
                pref.getString(USER_BIRTHDAY, null),
                pref.getString(USER_SEX, null),
                pref.getString(USER_PHONE, null),
                pref.getString(USER_EMAIL, null),
                pref.getString(COUNTRY_NAME, null),
                pref.getString(USER_PROFESSION, null));
    }
//    public ForumItems getForum() {
//        return new ForumItems(
//            pref.getString(FORUM_ID, null),
//            pref.getString(USER_ID, null),
//            pref.getString(FORUM_SUBJECT, null),
//            pref.getString(FORUM_SUBJECT, null),
//            pref.getString(FORUM_SUBJECT_DATE, null),
//            pref.getString(FORUM_SUBJECT_TIME, null));
//    }

    public void logOut() {
        editor.clear();
        editor.apply();
        setLogin(false);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, true);
    }
}
