package com.hame.forum.controller.utils;

public class Constant {
    public static final String TABLE_USER = "user_authentication";
    public static final String TABLE_SUBJECTS = "forum_subject";
    public static final String TABLE_COMMENTS = "forum_comment";
    public static final String TABLE_OPINIONS = "opinions";
    /*User*/
    public static final String USER_ID = "id_user";
    public static final String ID_USER = "id_user";
    public static final String USER_NAME = "user_name";
    public static final String USER_FIRST_NAME = "user_first_name";
    public static final String USER_BIRTHDAY = "user_birthday";
    public static final String USER_SEX = "user_sex";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PROFESSION = "user_profession";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_REPEAT_PASSWORD = "repeat_password";
    public static final String ID_COUNTRY = "id";
    public static final String COUNTRY_NAME = "nom";
    public static final String JSON_HOSPITAL = "json_hospital";
    public static final String JSON_SERVICES = "json_services";
    /**
     * Forum Opinion and Comments Items
     */
    public static final String FORUM_ID = "id_forum";
    public static final String COMMENT_ID = "id_comment";
    public static final String FORUM_SUBJECT = "forum_subject";
    public static final String NEW_COMMENT = "new_comment";
    public static final String FORUM_SUBJECT_TIME = "time_subject";
    public static final String FORUM_SUBJECT_DATE = "time_subject";
    public static final String COMMENT_TIME = "time_comment";
    public static final String COMMENT_DATE = "date_comment";
    public static final String OPINION_CONTENT = "opinion_content";
    public static final String RATING = "rating";
    /**
     * Hospitals  and Services Items
     */
    public static final String HOSPITAL_ID = "id_hospital ";
    public static final String ID_SERVICES = "id_services";
    public static final String ID_CITY = "id_city";
    public static final String CITY_NAME = "city_name";
    public static final String HOSPITAL_NAME = "hospital_name ";
    public static final String HOSPITAL_ADDRESS = "hospital_address";
    public static final String SERVICE_NAME = "service_name";
    //SharedPreferences
    public static final String SHARED_PREF_NAME = "my_login_app";
    public static final String LOGGED_IN_SHARED_PREF = "logged_in";
    /*Time out post request Volley*/
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    // Creating the User table
    public static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + USER_ID + " INTEGER PRIMARY KEY ,"
                    + USER_NAME + " TEXT,"
                    + USER_FIRST_NAME + " TEXT,"
                    + USER_EMAIL + " TEXT,"
                    + USER_PHONE + " TEXT,"
                    + USER_BIRTHDAY + " TEXT,"
                    + USER_SEX + " TEXT,"
                    + ID_COUNTRY + " TEXT,"
                    + USER_PROFESSION + " TEXT,"
                    + ")";
    // Creating the Forum_Subjects table
    public static final String CREATE_TABLE_FORUM_SUBJECTS =
            "CREATE TABLE " + TABLE_SUBJECTS + "("
                    + USER_ID + " INTEGER PRIMARY KEY ,"
                    + FORUM_SUBJECT + " TEXT,"
                    + FORUM_SUBJECT_TIME + " TEXT,"
                    + ")";
    // Creating the Forum_Comment table
    public static final String CREATE_TABLE_FORUM_COMMENTS =
            "CREATE TABLE " + TABLE_COMMENTS + "("
                    + USER_ID + " INTEGER PRIMARY KEY ,"
                    + NEW_COMMENT + " TEXT,"
                    + COMMENT_TIME + " TEXT,"
                    + COMMENT_DATE + " TEXT,"
                    + ")";
    // Creating the User table
    public static final String CREATE_TABLE_OPINION =
            "CREATE TABLE " + TABLE_OPINIONS + "("
                    + USER_ID + " INTEGER PRIMARY KEY ,"
                    + USER_NAME + " TEXT,"
                    + USER_FIRST_NAME + " TEXT,"
                    + USER_EMAIL + " TEXT,"
                    + USER_PHONE + " TEXT,"
                    + USER_BIRTHDAY + " TEXT,"
                    + USER_SEX + " TEXT,"
                    + ID_COUNTRY + " TEXT,"
                    + USER_PROFESSION + " TEXT,"
                    + ")";
    //private static final String SERVER = "http://192.168.43.77/kosunga/";
    private static final String SERVER = "http://es.brascodigital.com/";
    /*Account*/
    public static final String LOGIN_USER = SERVER + "login.php";
    public static final String REGISTER_USER = SERVER + "register.php";
    /*Opinion*/
    public static final String INSERT_ASSURANCE = SERVER + "insert_assurance.php";
    public static final String INSERT_PCCU = SERVER + "insert_pccu.php";
    public static final String GET_ASSURANCE = SERVER + "get_assurance.php";
    public static final String GET_PCCU = SERVER + "get_pccu.php";
    public static final String GET_ASSURANCE_PCCU = SERVER + "get_assurance_pccu.php";
    /*Forum*/
    public static final String INSERT_FORUM_TITLE_QUESTION = SERVER + "insert_forum_title_question.php";
    public static final String INSERT_FORUM_DETAILED_QUESTION = SERVER + "insert_forum_detailed_question.php";
    public static final String INSERT_FORUM_COMMENT = SERVER + "insert_forum_comment.php";
    public static final String INSERT_FORUM_TIME = SERVER + "insert_forum_time.php";
    public static final String GET_FORUM_TITLE_QUESTION = SERVER + "get_forum_title.php";
    public static final String GET_FORUM_DETAILED_QUESTION = SERVER + "get_forum_detailed_question.php";
    public static final String GET_FORUM_COMMENT = SERVER + "get_forum_comment.php";
    public static final String GET_FORUM_TIME = SERVER + "get_forum_time.php";


}
