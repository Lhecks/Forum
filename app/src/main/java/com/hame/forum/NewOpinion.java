package com.hame.forum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.hame.forum.adapter.CustomArrayAdapterCity;
import com.hame.forum.adapter.CustomArrayAdapterHospital;
import com.hame.forum.adapter.CustomArrayAdapterService;
import com.hame.forum.controller.app.AppConfig;
import com.hame.forum.controller.utils.Constant;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.CityItems;
import com.hame.forum.models.HospitalItems;
import com.hame.forum.models.ServiceItems;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import static com.hame.forum.controller.app.AppConfig.URL_JSON_GET_HOSPITAL;
import static com.hame.forum.controller.utils.Constant.ID_COUNTRY;
import static com.hame.forum.controller.utils.Constant.SHARED_PREF_NAME;
import static com.hame.forum.controller.utils.Constant.COUNTRY_NAME;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewOpinion extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = NewOpinion.class.getSimpleName();
    private View view;
    private View parent_view;
    private SharedPreferences.Editor editor;
    private Spinner spinnerService, spinnerHospital, spinnerCity;
    private EditText editOpinion;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private TextView textHospital, textServices;
    private CityItems cityItems;
    private ArrayList<CityItems> cityItemsArrayList;
    private CustomArrayAdapterCity customArrayAdapterCity;
    private HospitalItems hospitalItems = new HospitalItems();
    private ArrayList<HospitalItems> hospitalItemsArrayList;
    private CustomArrayAdapterHospital customArrayAdapterHospital;
    private ServiceItems serviceItems;
    private ArrayList<ServiceItems> serviceItemsArrayList;
    private CustomArrayAdapterService customArrayAdapterService;
    private String country_name;
    private SessionManager sessionManager;
    private TextView textServiceLabel;
    private Button buttonSubmit;
    private String opinion_content, rating, id_city, city_name, id_hospitals,
            hospital_name, id_service, service_name, user_name;
    private LinearLayout linearSpinnerCity, linearSpinnerHospital, linearSpinnerService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_opinion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        textServices = findViewById(R.id.text_service);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerHospital = findViewById(R.id.spinner_hospital);
        spinnerService = findViewById(R.id.spinner_service_hospital);
        editOpinion = findViewById(R.id.edit_for_subject_new_opinion);
        progressBar = findViewById(R.id.progressbar_for_sending_new_opinion);
        buttonSubmit = findViewById(R.id.button_for_sending_data_new_opinion);
        ratingBar = findViewById(R.id.rating_bar);
        linearSpinnerCity = findViewById(R.id.linear_spinner_city);
        linearSpinnerHospital = findViewById(R.id.linear_spinner_hospital);
        linearSpinnerService = findViewById(R.id.linear_spinner_service);

        String id_country = sessionManager.getUser().getId_Country();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opinion_content = editOpinion.getText().toString();
                rating = String.valueOf(ratingBar.getRating());
                if (checkingInternet()) {
                    if (!opinion_content.isEmpty()) {
                        sendOpinion();
                        hideKeyboardSoft();
                    } else {
                        showMessage(getString(R.string.empty_field));
                    }

                } else {
                    Snackbar.make(view, getString(R.string.check_internet), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        if (checkingInternet()) {
            getCities(id_country);
        } else {
            showMessage(getString(R.string.check_internet));
        }

        spinnerCity.setOnItemSelectedListener(this);

        hideKeyboardSoft();
    }

    private void getCities(final String id_country) {
        country_name = sessionManager.getUser().getUser_country();
        cityItemsArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_JSON_GET_CITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG + " Getting Cities item", response + " City Answers");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        progressBar.setVisibility(View.GONE);
                        showMessage("Error while getting City");
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            cityItems = new CityItems(
                                    jsonObject.getInt("id_city"),
                                    jsonObject.getString("city_name"),
                                    jsonObject.getString("nom"));
                            cityItemsArrayList.add(cityItems);

                            if (cityItemsArrayList.size() > 0) {
                                customArrayAdapterCity = new CustomArrayAdapterCity(getApplicationContext(), R.layout.item_city, cityItemsArrayList);
                                spinnerCity.setAdapter(customArrayAdapterCity);
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                linearSpinnerCity.setVisibility(View.GONE);
                                linearSpinnerHospital.setVisibility(View.GONE);
                                linearSpinnerService.setVisibility(View.GONE);
//                                linearEditCity.setVisibility(View.VISIBLE);
//                                linearEditHospital.setVisibility(View.VISIBLE);
//                                linearEditService.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put(ID_COUNTRY, id_country);
                hashMap.put(COUNTRY_NAME, country_name);
                return hashMap;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void getHospital(final String city_id) {
        hospitalItemsArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_JSON_GET_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                editor.putString(Constant.JSON_HOSPITAL, response);
                editor.apply();
                Log.v(TAG + "Getting Hospital item", response + " Hospital Answers");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        progressBar.setVisibility(View.GONE);
//                        if (id_city != null){
//                            showMessage("No hospitals registered for this city ==:: " + id_city);
//                        }else{
                        showMessage("Error while getting Hospital");
//                        }
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hospitalItems = new HospitalItems(
                                    jsonObject.getInt("id_hospital"),
                                    jsonObject.getString("hospital_name"),
                                    jsonObject.getString("city_name")
                            );
                            hospitalItemsArrayList.add(hospitalItems);
                            id_hospitals = String.valueOf(hospitalItemsArrayList.get(i).getIdHospital());
                            hospital_name = String.valueOf(hospitalItemsArrayList.get(i).getHospitalName());

                            showMessage("Item :" + city_id + " - " + cityItems.getCityName());
                            if (hospitalItemsArrayList.size() > 0) {
                                customArrayAdapterHospital = new CustomArrayAdapterHospital(getApplicationContext(), R.layout.item_hospital, hospitalItemsArrayList);
                                spinnerHospital.setAdapter(customArrayAdapterHospital);
                                if (id_hospitals != null) {
                                    getServices(id_hospitals);
                                    linearSpinnerService.setVisibility(View.VISIBLE);
                                }
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                linearSpinnerHospital.setVisibility(View.GONE);
                                linearSpinnerService.setVisibility(View.GONE);
                                serviceItemsArrayList.clear();
                                snackBarFloating();

//                                linearEditHospital.setVisibility(View.VISIBLE);
//                                linearEditService.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("id_city", city_id);
                return hashMap;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void getServices(final String hospital_id) {
        serviceItemsArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_JSON_GET_HOSPITAL_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                editor.putString(Constant.JSON_SERVICES, response);
                editor.apply();
                Log.v(TAG + "Getting Services item", response + " Services Answers");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        progressBar.setVisibility(View.GONE);
                        showMessage("Error while getting Hospital");
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            serviceItems = new ServiceItems(
                                    jsonObject.getInt("id_service"),
                                    jsonObject.getString("service_name")
                            );
                            serviceItemsArrayList.add(serviceItems);
                            id_service = String.valueOf(serviceItemsArrayList.get(i).getIdService());
                            service_name = String.valueOf(serviceItemsArrayList.get(i).getServiceName());
                            showMessage("Item :" + hospital_id + " - " + hospitalItems.getHospitalName());
                            if (serviceItemsArrayList.size() > 0) {
                                customArrayAdapterService = new CustomArrayAdapterService(getApplicationContext(), R.layout.item_service_hospital, serviceItemsArrayList);
                                spinnerService.setAdapter(customArrayAdapterService);
                            } else {
                                showMessage("empty service");
                                progressBar.setVisibility(View.VISIBLE);
                                linearSpinnerService.setVisibility(View.GONE);
//                                linearEditService.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                snackBarFloating();
                            }
                        }
//                        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                id_service = ((TextView)view.findViewById(R.id.id_service_hospital)).getText().toString();
//                                service_name = ((TextView)view.findViewById(R.id.name_hospital)).getText().toString();
//                                showMessage(id_service + " : " + service_name);
//                            }
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("id_hospital", hospital_id);
                return hashMap;
            }
        };
        getStringRequeue(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        id_city = String.valueOf(cityItemsArrayList.get(i).getIdCity());
//        id_hospitals = ((TextView)view.findViewById(R.id.id_hospital)).getText().toString();
        city_name = ((TextView) view.findViewById(R.id.name_city)).getText().toString();
        showMessage(id_city + " : " + city_name);
        if (id_city != null) {
            getHospital(id_city);
            linearSpinnerHospital.setVisibility(View.VISIBLE);
        } else {
            linearSpinnerHospital.setVisibility(View.GONE);
            hospitalItemsArrayList.clear();
            snackBarFloating();
            showMessage("No city found from outside method cities");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendOpinion() {
        rating = String.valueOf(ratingBar.getRating());
        opinion_content = editOpinion.getText().toString();
        user_name = sessionManager.getUser().getUser_name();
        hideKeyboardSoft();
        progressBar.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.GONE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_OPINION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("New Opinion Well Inserted");
                    startActivity(new Intent(getApplicationContext(), Opinion.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                    hideKeyboardSoft();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("Error while adding a new Opinion... Try Later");
                    buttonSubmit.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getErrors(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("opinion_content", opinion_content);
                map.put("rating", rating);
                map.put("id_city", id_city);
                map.put("city_name", city_name);
                map.put("id_hospital", id_hospitals);
                map.put("hospital_name", hospital_name);
                map.put("id_services", id_service);
                map.put("service_name", service_name);
                map.put("id_user", sessionManager.getUser().getIdUser());
                map.put("user_name", user_name);
                return map;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void snackBarFloating() {
        final Snackbar snackbar = Snackbar.make(parent_view, "", Snackbar.LENGTH_LONG);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_toast_floating, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);
        (custom_view.findViewById(R.id.tv_undo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                Toast.makeText(getApplicationContext(), "UNDO Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    private void getStringRequeue(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(NewOpinion.this);
        requestQueue.add(stringRequest);
    }

    private void getErrors(VolleyError error) {
        if (error instanceof NetworkError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            showMessage(getString(R.string.volley_network_error));
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            showMessage(getString(R.string.volley_server_error));
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            showMessage(getString(R.string.volley_auth_fail));
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            showMessage(getString(R.string.volley_parse_error));
        } else if (error instanceof TimeoutError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            showMessage(getString(R.string.volley_time_out_error));
        }
    }

    private void getError(VolleyError error) {
        if (error instanceof NetworkError) {
            progressBar.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_network_error));
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_server_error));
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_auth_fail));
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_parse_error));
        } else if (error instanceof TimeoutError) {
            progressBar.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_time_out_error));
        }
    }

    public boolean checkingInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
    }

    private void hideKeyboardSoft() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void showMessage(String message) {
        Toast.makeText(NewOpinion.this, message, Toast.LENGTH_SHORT).show();
    }
}