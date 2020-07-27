package com.hame.forum.controller.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.hame.forum.models.ForumItems;
import com.hame.forum.models.UserItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hame.forum.controller.utils.Constant.CREATE_TABLE_FORUM_COMMENTS;
import static com.hame.forum.controller.utils.Constant.CREATE_TABLE_FORUM_SUBJECTS;
import static com.hame.forum.controller.utils.Constant.CREATE_TABLE_USER;
import static com.hame.forum.controller.utils.Constant.FORUM_SUBJECT;
import static com.hame.forum.controller.utils.Constant.FORUM_SUBJECT_TIME;
import static com.hame.forum.controller.utils.Constant.TABLE_COMMENTS;
import static com.hame.forum.controller.utils.Constant.TABLE_SUBJECTS;
import static com.hame.forum.controller.utils.Constant.TABLE_USER;
import static com.hame.forum.controller.utils.Constant.USER_BIRTHDAY;
import static com.hame.forum.controller.utils.Constant.ID_COUNTRY;
import static com.hame.forum.controller.utils.Constant.USER_EMAIL;
import static com.hame.forum.controller.utils.Constant.USER_FIRST_NAME;
import static com.hame.forum.controller.utils.Constant.USER_ID;
import static com.hame.forum.controller.utils.Constant.USER_NAME;
import static com.hame.forum.controller.utils.Constant.USER_PHONE;
import static com.hame.forum.controller.utils.Constant.USER_PROFESSION;
import static com.hame.forum.controller.utils.Constant.USER_SEX;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "james_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creation des tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_FORUM_SUBJECTS);
        db.execSQL(CREATE_TABLE_FORUM_COMMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        // Creation des  tables
        onCreate(db);
    }

    public long insertUsers(UserItems userItems) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ID, userItems.getIdUser());
        values.put(USER_NAME, userItems.getUser_name());
        values.put(USER_FIRST_NAME, userItems.getUser_first_name());
        values.put(USER_BIRTHDAY, userItems.getUser_birthday());
        values.put(USER_SEX, userItems.getUser_sex());
        values.put(USER_PHONE, userItems.getUser_phone());
        values.put(USER_EMAIL, userItems.getUser_email());
        values.put(ID_COUNTRY, userItems.getUser_country());
        values.put(USER_PROFESSION, userItems.getUser_profession());

        long id = db.insert(TABLE_USER, null, values);
        // closing db connection
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + id);
        return id;
    }

    public long insertSubjectsForum(ForumItems forumItems) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ID, forumItems.getId_forum());
        values.put(FORUM_SUBJECT, forumItems.getForum_subject());
        values.put(FORUM_SUBJECT_TIME, forumItems.getDate_forum());

        long id = db.insert(TABLE_SUBJECTS, null, values);
        // closing db connection
        db.close();
        Log.d(TAG, "New user inserted into sqlite: " + id);
        return id;
    }
//    Getting the user date form the database

    public HashMap<String, String> getUserData() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor myCursor = sqLiteDatabase.rawQuery(selectQuery, null);
        myCursor.moveToFirst();
        if (myCursor.getCount() > 0) {
            user.put("user_name", myCursor.getString(1));
            user.put("user_first_name", myCursor.getString(2));
            user.put("user_birthday", myCursor.getString(3));
            user.put("user_sex", myCursor.getString(4));
            user.put("user_phone", myCursor.getString(5));
            user.put("user_email", myCursor.getString(6));
            user.put("user_country", myCursor.getString(7));
            user.put("user_profession", myCursor.getString(8));
        }
        myCursor.close();
        sqLiteDatabase.close();
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    //    Getting the user date form the database
    public HashMap<String, String> getInputCRHospitalData() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String selectQueryCRH = "SELECT  * FROM " + TABLE_SUBJECTS;
        SQLiteDatabase sqLiteDatabaseCRH = this.getReadableDatabase();
        Cursor myCursorCHR = sqLiteDatabaseCRH.rawQuery(selectQueryCRH, null);
        myCursorCHR.moveToFirst();
        if (myCursorCHR.getCount() > 0) {
            hashMap.put("forum_subject", myCursorCHR.getString(1));
            hashMap.put("time_subject", myCursorCHR.getString(2));
        }
        myCursorCHR.close();
        sqLiteDatabaseCRH.close();
        Log.d(TAG, "Fetching user from Sqlite: " + hashMap.toString());

        return hashMap;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
        Log.d(TAG, "Deleting Users from the table");
    }

    public void deleteHospitalCR() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBJECTS, null, null);
        db.close();
        Log.d(TAG, "Deleting Users from the table");
    }

    /**
     * Cette méthode recupère tous les plats dans la base de données
     *
     * @author Lhex
     ***/
    public List<ForumItems> getAllRecipes() {
        List<ForumItems> forumItemsArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SUBJECTS + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Going through the forum items table
        if (cursor.moveToFirst()) {
            do {
                ForumItems forumItems = new ForumItems();
                forumItems.setId_forum(Integer.parseInt(cursor.getString(cursor.getColumnIndex(USER_ID))));
                forumItems.setForum_subject(cursor.getString(cursor.getColumnIndex(FORUM_SUBJECT)));
                forumItems.setDate_forum(cursor.getString(cursor.getColumnIndex(FORUM_SUBJECT_TIME)));

                forumItemsArrayList.add(forumItems);
            } while (cursor.moveToNext());
        }

        // Closing the database
        db.close();

        // Get the list forum articles
        return forumItemsArrayList;
    }

    /**
     * Cette méthode permet de modifier l'utilisateur
     * elle sera utile lorsque l'utilisateur completera ces infos
     *
     * @param userItems le model
     * @author Lhex
     ***/
    public int updateUser(UserItems userItems) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, userItems.getIdUser());
        values.put(USER_NAME, userItems.getUser_name());
        values.put(USER_FIRST_NAME, userItems.getUser_first_name());
        values.put(USER_BIRTHDAY, userItems.getUser_birthday());
        values.put(USER_SEX, userItems.getUser_sex());
        values.put(USER_PHONE, userItems.getUser_phone());
        values.put(USER_EMAIL, userItems.getUser_email());
        values.put(ID_COUNTRY, userItems.getUser_country());
        values.put(USER_PROFESSION, userItems.getUser_profession());

        // updating row
        return db.update(TABLE_USER, values, USER_ID + " = ?", new String[]{String.valueOf(userItems.getIdUser())});
    }

    /**
     * Recreate database
     * Delete all tables then recreate them again
     */
    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
    }

}
