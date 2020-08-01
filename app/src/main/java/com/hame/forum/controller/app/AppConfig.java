package com.hame.forum.controller.app;

public class AppConfig {
    // Server urls Links

//    public static final String URL = "http://10.5.51.35/james/";
public static final String URL = "http://192.168.43.253/james/";

    //    Server user Register url
    public static String URL_REGISTER = URL + "register.php";

    //    Server user login url
    public static String URL_LOGIN = URL + "login.php";

    //    Server for JSON Country
    public static String URL_JSON_GET_COUNTRIES = URL + "get_pays.php";

    //    Server Link for the forum: Inserting and Getting the Subject
    public static String URL_INSERT_SUBJECT = URL + "insert_subject.php";
    public static String URL_JSON_GET_FORUM_SUBJECTS = URL + "retrieve_f.php";

    //    Server Link for the Forum: Inserting and Getting the comments
    public static String URL_INPUT_NEW_COMMENT = URL + "insert_comment.php";
    public static String URL_JSON_GET_COMMENT_BY_ID = URL + "get_comment_by_id.php";

    //    Server for JSON Opinions
    public static String URL_INSERT_OPINION = URL + "insert_opinion.php";
    public static String URL_JSON_GET_OPINION = URL + "retrieve_opinion.php";

    //    Server Links for Hospitals
    public static String URL_JSON_GET_HOSPITAL = URL + "retrieve_hospitals.php";
    public static String URL_INSERT_HOSPITAL = URL + "insert_hospital.php";

    //    Server links for Hospitals Services
    public static String URL_JSON_GET_HOSPITAL_SERVICES = URL + "retrieve_services_hospital.php";
    public static String URL_INSERT_HOSPITAL_SERVICES = URL + "insert_services_hospital.php";

    //    Server link for cities
    public static String URL_JSON_GET_CITY = URL + "retrieve_cities.php";
    public static String URL_INSERT_CITY = URL + "insert_cities.php";
}