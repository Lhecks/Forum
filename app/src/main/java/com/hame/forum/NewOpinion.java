package com.hame.forum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import androidx.cardview.widget.CardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import java.util.Objects;

public class NewOpinion extends AppCompatActivity {
    private static final String TAG = NewOpinion.class.getSimpleName();
    private View parent_view, parent_view_2;
    private SharedPreferences.Editor editor;
    private Spinner spinnerService, spinnerHospital, spinnerCity;
    private EditText editOpinion;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private CityItems cityItems = new CityItems();
    private HospitalItems hospitalItems = new HospitalItems();
    private ServiceItems serviceItems = new ServiceItems();
    private ArrayList<CityItems> cityItemsArrayList = new ArrayList<>();
    private ArrayList<HospitalItems> hospitalItemsArrayList = new ArrayList<>();
    private ArrayList<ServiceItems> serviceItemsArrayList = new ArrayList<>();
    private CustomArrayAdapterCity customArrayAdapterCity;
    private CustomArrayAdapterHospital customArrayAdapterHospital;
    private CustomArrayAdapterService customArrayAdapterService;
    private String country_name;
    private SessionManager sessionManager;
    private Button buttonSubmit;
    private String opinion_content, rating, id_city, city_name, id_hospitals,
            hospital_name, id_service, service_name, user_name;
    private LinearLayout linearSpinnerCity, linearSpinnerHospital, linearSpinnerService, layoutHospital, layoutService;
    private TextView textHospital, textServices, textCounting;
    private CardView cardViewSnackBar;
    private RelativeLayout view_parent;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_opinion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        TextView textCity = findViewById(R.id.add_city_text);
        textHospital = findViewById(R.id.add_hospital_text);
        textServices = findViewById(R.id.add_service_text);
        textCounting = findViewById(R.id.text_counting_opinion);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerHospital = findViewById(R.id.spinner_hospital);
        spinnerService = findViewById(R.id.spinner_service_hospital);
        editOpinion = findViewById(R.id.edit_for_subject_new_opinion);
        progressBar = findViewById(R.id.progressbar_for_sending_new_opinion);
        buttonSubmit = findViewById(R.id.button_for_sending_data_new_opinion);
        ratingBar = findViewById(R.id.rating_bar);
        cardViewSnackBar = findViewById(R.id.card_snack_bar);
        linearSpinnerCity = findViewById(R.id.linear_spinner_city);
        linearSpinnerHospital = findViewById(R.id.linear_spinner_hospital);
        linearSpinnerService = findViewById(R.id.linear_spinner_service);
        parent_view = findViewById(android.R.id.content);
        view_parent = findViewById(R.id.relative_view_parent);
//        parent_view_2 = findViewById(R.id.parent_view_2);


        final String id_country = sessionManager.getUser().getId_Country();
        if (checkingInternet()) {
            getCities(id_country);
        } else {
            showMessage(getString(R.string.check_internet));
        }

        editOpinion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentText = editable.toString();
                int currentLength = currentText.length();
                if (currentLength < 2) {
                    textCounting.setText("(" + currentLength + " Character)");
                } else {
                    textCounting.setText("(" + currentLength + " Characters)");
                }
            }
        });

//        sendCityItems(bundle);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opinion_content = editOpinion.getText().toString();
                rating = String.valueOf(ratingBar.getRating());
                if (checkingInternet()) {
                    if (!opinion_content.isEmpty() && !opinion_content.startsWith(" ")) {
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

//        Opening the dialogCity for the city to add a new city a the spinner
        textCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogCity();
            }
        }); //End of City BottomSheet fragment
        hideKeyboardSoft();
    }

    //    Get the cities items using the spinner
    private void getCities(final String id_country) {
        country_name = sessionManager.getUser().getUser_country();
        cityItemsArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_JSON_GET_CITY, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.v(TAG + " Getting Cities item", response + " City Answers");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        linearSpinnerCity.setVisibility(View.VISIBLE);
                        snackBarCity();
//                        customArrayAdapterCity.updateCityItems(cityItemsArrayList);
//                        openDialogCity();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            cityItems = new CityItems(
                                    jsonObject.getString("id_city"),
                                    jsonObject.getString("city_name"),
                                    jsonObject.getString("nom"));
                            cityItemsArrayList.add(cityItems);
                            customArrayAdapterCity = new CustomArrayAdapterCity(getApplicationContext(), R.layout.item_city, cityItemsArrayList);
                            spinnerCity.setAdapter(customArrayAdapterCity);
                            spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    id_city = ((TextView) view.findViewById(R.id.id_city)).getText().toString();
                                    city_name = ((TextView) view.findViewById(R.id.name_city)).getText().toString();
                                    if (id_city != null) {
                                        for (int num = 0; num < cityItemsArrayList.size(); num++) {
                                            try {
                                                sessionManager.cityPref(new CityItems(
                                                        jsonObject.getString("id_city"),
                                                        jsonObject.getString("city_name"),
                                                        jsonObject.getString("nom")
                                                ));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            sessionManager.setLogin(true);
                                        }
                                        showMessage(id_city + ":::" + city_name);
                                        getHospital(id_city);
                                        linearSpinnerHospital.setVisibility(View.VISIBLE);
                                    } else {
                                        showMessage("Empty list of City");
                                        linearSpinnerHospital.setVisibility(View.GONE);
                                        openDialogCity();
                                        customArrayAdapterCity.updateCityItems(cityItemsArrayList);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
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
//    End of Spinner Cities Spinner

    //    Get the hospitals items using the spinner
    private void getHospital(final String city_id) {
        hospitalItemsArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_JSON_GET_HOSPITAL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                editor.putString(Constant.JSON_HOSPITAL, response);
                editor.apply();
                Log.v(TAG + "Getting Hospital item", response + " Hospital Answers");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        progressBar.setVisibility(View.GONE);
                        linearSpinnerService.setVisibility(View.GONE);
                        linearSpinnerHospital.setVisibility(View.GONE);
                        openDialogHospital(id_city, city_name);
//                        customArrayAdapterHospital.updateHospitalItems(hospitalItemsArrayList);
                        showMessage("Empty Hospital List");
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hospitalItems = new HospitalItems(
                                    jsonObject.getString("id_hospital"),
                                    jsonObject.getString("hospital_name"),
                                    jsonObject.getString("hospital_address"));
                            hospitalItemsArrayList.add(hospitalItems);
                            id_hospitals = String.valueOf(hospitalItemsArrayList.get(i).getIdHospital());
                            hospital_name = String.valueOf(hospitalItemsArrayList.get(i).getHospitalName());

                            customArrayAdapterHospital = new CustomArrayAdapterHospital(getApplicationContext(), R.layout.item_hospital, hospitalItemsArrayList);
                            spinnerHospital.setAdapter(customArrayAdapterHospital);
                            spinnerHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    id_hospitals = ((TextView) view.findViewById(R.id.id_hospital)).getText().toString();
                                    hospital_name = ((TextView) view.findViewById(R.id.name_hospital)).getText().toString();
                                    showMessage(id_hospitals + ":::" + hospital_name);

                                    if (id_hospitals != null) {
                                        for (int a = 0; a < cityItemsArrayList.size(); a++) {
                                            try {
                                                sessionManager.hospitalPref(new HospitalItems(
                                                        jsonObject.getString("id_hospital"),
                                                        jsonObject.getString("hospital_name"),
                                                        jsonObject.getString("hospital_address")
                                                ));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            sessionManager.setLogin(true);
                                        }
                                        getServices(id_hospitals);
                                        linearSpinnerService.setVisibility(View.VISIBLE);

                                        textHospital.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                openDialogHospital(id_city, city_name);
//                                                customArrayAdapterHospital.updateHospitalItems(hospitalItemsArrayList);
                                            }
                                        });
                                    } else {
                                        showMessage("Empty list of Hospital");
                                        linearSpinnerHospital.setVisibility(View.GONE);
                                        linearSpinnerService.setVisibility(View.GONE);
                                        textHospital.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                openDialogHospital(id_city, city_name);
//                                                customArrayAdapterHospital.updateHospitalItems(hospitalItemsArrayList);
                                            }
                                        });
                                        customArrayAdapterHospital.updateHospitalItems(hospitalItemsArrayList);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
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
//    Enf of the Hospital Spinner

    //    Get the service of the hospital using the Spinner
    private void getServices(final String hospital_id) {
        serviceItemsArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_JSON_GET_HOSPITAL_SERVICES, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                editor.putString(Constant.JSON_SERVICES, response);
                editor.apply();
                Log.v(TAG + "Getting Services item", response + " Services Answers");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        showMessage("Empty Service List");
                        linearSpinnerService.setVisibility(View.GONE);
                        openDialogService(hospital_id, hospital_name);
//                        customArrayAdapterService.updateServiceItems(serviceItemsArrayList);
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
                            customArrayAdapterService = new CustomArrayAdapterService(getApplicationContext(), R.layout.item_service_hospital, serviceItemsArrayList);
                            spinnerService.setAdapter(customArrayAdapterService);
                            spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    id_service = ((TextView) view.findViewById(R.id.id_service_hospital)).getText().toString();
                                    service_name = ((TextView) view.findViewById(R.id.name_service_hospital)).getText().toString();
                                    showMessage(id_service + ":::" + service_name);
                                    textServices.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            openDialogService(id_hospitals, hospital_name);
//                                            customArrayAdapterService.updateServiceItems(serviceItemsArrayList);
                                        }
                                    });
                                    customArrayAdapterService.updateServiceItems(serviceItemsArrayList);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
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
                hashMap.put("id_hospital", hospital_id);
                return hashMap;
            }
        };
        getStringRequeue(stringRequest);
    }//    End of th Service Hospitals Spinner

    //    Method sendOpinion to send the the user request in the opinion table
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
    }//    End of the sendOpinion method

    //    Customizing the SnackBar
    private void snackBarCity() {
        final Snackbar snackbar = Snackbar.make(view_parent, "", Snackbar.LENGTH_LONG);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_city, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);
        (custom_view.findViewById(R.id.tv_undo)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                openDialogCity();
//                Toast.makeText(getApplicationContext(), "UNDO Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    private void snackBarHospital() {
        final Snackbar snackbar = Snackbar.make(view_parent, "", Snackbar.LENGTH_LONG);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_hospital, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);
        (custom_view.findViewById(R.id.tv_add_new_hospital)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
//                openDialogHospital();
//                Toast.makeText(getApplicationContext(), "UNDO Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    private void snackBarService() {
        final Snackbar snackbar = Snackbar.make(view_parent, "", Snackbar.LENGTH_LONG);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_services, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);
        (custom_view.findViewById(R.id.tv_add_new_service)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                Toast.makeText(getApplicationContext(), "UNDO Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }//    End pf Custom SnackBar

    //    This method handles
    private void getStringRequeue(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(NewOpinion.this);
        requestQueue.add(stringRequest);
    }//    End of

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDialogCity() {
        cityItemsArrayList = new ArrayList<>();
        customArrayAdapterCity = new CustomArrayAdapterCity(getApplicationContext(), R.layout.item_city, cityItemsArrayList);
        final String id_country;
        final EditText edit_city_name;
        final Button buttonSubmit;
        final ProgressBar progressBar;
        ImageButton imageButton;
        final Dialog dialogCity = new Dialog(this);
        dialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCity.setContentView(R.layout.dialog_city);
        dialogCity.setCancelable(true);

        edit_city_name = dialogCity.findViewById(R.id.edit_city_dialog);
        buttonSubmit = dialogCity.findViewById(R.id.button_city_dialog);
        imageButton = dialogCity.findViewById(R.id.bt_close_city_dialog);
        progressBar = dialogCity.findViewById(R.id.progress_bar_city_dialog);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialogCity.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCity.dismiss();
            }
        });

        id_country = sessionManager.getUser().getId_Country();
        country_name = sessionManager.getUser().getUser_country();

        showMessage(id_country + ":::\n City dialog:::\n" + country_name);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String city_name = edit_city_name.getText().toString();
                if (checkingInternet()) {
                    if (!city_name.isEmpty() && !city_name.startsWith(" ")) {
                        buttonSubmit.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_CITY, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v(TAG, "Response: " + response);
                                if (response.trim().equalsIgnoreCase("0")) {
                                    Log.v(TAG, response);
                                    progressBar.setVisibility(View.GONE);
                                    edit_city_name.getText().clear();
                                    showMessage("City Well Inserted");
                                    dialogCity.dismiss();
                                    customArrayAdapterCity.updateCityItems(cityItemsArrayList);
                                    getCities(id_country);
                                    hideKeyboardSoft();
                                } else {
                                    Log.e(TAG, response);
                                    progressBar.setVisibility(View.GONE);
                                    showMessage("Error while adding a new City... Try again");
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
                                map.put("city_name", city_name);
                                map.put("id", id_country);
                                map.put("nom", country_name);
                                return map;
                            }
                        };
                        getStringRequeue(stringRequest);
                    } else {
                        showMessage(getString(R.string.empty_field));
                    }
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });
        dialogCity.show();
        dialogCity.getWindow().setAttributes(layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDialogHospital(final String city_id, final String name_city) {
        customArrayAdapterHospital = new CustomArrayAdapterHospital(getApplicationContext(), R.layout.item_hospital, hospitalItemsArrayList);
        spinnerHospital.setAdapter(customArrayAdapterHospital);
        final String id_city, city_name, hospital_name, hospital_address;
        final EditText edit_name_hospital, edit_hospital_address;
        final Button buttonSubmit;
        final ProgressBar progressBar;
        ImageButton imageButton;
        final Dialog dialogHospital = new Dialog(this);
        dialogHospital.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogHospital.setContentView(R.layout.dialog_hospital);
        dialogHospital.setCancelable(true);

        edit_name_hospital = dialogHospital.findViewById(R.id.edit_hospital_dialog);
        edit_hospital_address = dialogHospital.findViewById(R.id.edit_hospital_address_dialog);
        buttonSubmit = dialogHospital.findViewById(R.id.button_hospital_dialog);
        imageButton = dialogHospital.findViewById(R.id.bt_close_hospital_dialog);
        progressBar = dialogHospital.findViewById(R.id.progress_bar_hospital_dialog);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialogHospital.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHospital.dismiss();
            }
        });

        id_city = sessionManager.getCity().getIdCity();
        final String id_country = sessionManager.getUser().getId_Country();
//        city_name = sessionManager.getCity().getCityName();
        showMessage(city_id + " \nHospital Dialog \n" + name_city);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String hospital_name = edit_name_hospital.getText().toString();
                final String hospital_address = edit_hospital_address.getText().toString();
//                showMessage(id_city + " \nHospital Dialog \n" + city_name);
                if (checkingInternet()) {
                    if (!hospital_name.isEmpty() && !hospital_name.startsWith(" ") && !hospital_address.startsWith(" ")) {
                        buttonSubmit.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_HOSPITAL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v(TAG, "Response: " + response);
                                if (response.trim().equalsIgnoreCase("0")) {
                                    Log.v(TAG, response);
                                    hideKeyboardSoft();
                                    progressBar.setVisibility(View.GONE);
                                    showMessage("Hospital Well Inserted");
                                    getHospital(id_city);
                                    customArrayAdapterHospital.addAll();
                                    customArrayAdapterHospital.notifyDataSetChanged();
                                    linearSpinnerHospital.setVisibility(View.VISIBLE);
                                    dialogHospital.dismiss();
                                } else {
                                    Log.e(TAG, response);
                                    progressBar.setVisibility(View.GONE);
                                    showMessage("Error while adding a new Hospital... Try Later");
                                    buttonSubmit.setVisibility(View.VISIBLE);
//                                    customArrayAdapterHospital.clearHospitalItems(hospitalItemsArrayList);
                                    customArrayAdapterHospital.clear();
                                    linearSpinnerHospital.setVisibility(View.GONE);
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
                                map.put("hospital_name", hospital_name);
                                map.put("hospital_address", hospital_address);
                                map.put("id_city", city_id);
                                map.put("city_name", name_city);
                                return map;
                            }
                        };
                        getStringRequeue(stringRequest);
                    } else {
                        showMessage(getString(R.string.empty_field));
                    }
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });
        dialogHospital.show();
        dialogHospital.getWindow().setAttributes(layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDialogService(final String hospital_id, final String hospitals_name) {
        customArrayAdapterService = new CustomArrayAdapterService(getApplicationContext(), R.layout.item_service_hospital, serviceItemsArrayList);
        spinnerService.setAdapter(customArrayAdapterService);
        final EditText edit_service;
        final Button buttonSubmit;
        final String id_hospital, hospital_name;
        final ProgressBar progressBar;
        ImageButton imageButton;
        final Dialog dialogService = new Dialog(this);
        dialogService.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogService.setContentView(R.layout.dialog_services);
        dialogService.setCancelable(true);

        edit_service = dialogService.findViewById(R.id.edit_service_dialog);
        buttonSubmit = dialogService.findViewById(R.id.button_service_dialog);
        progressBar = dialogService.findViewById(R.id.progress_bar_service_dialog);
        imageButton = dialogService.findViewById(R.id.bt_close_service_dialog);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialogService.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        id_hospital = sessionManager.getHospital().getIdHospital();
        hospital_name = sessionManager.getHospital().getHospitalName();

        final String id_country = sessionManager.getUser().getId_Country();
        showMessage(hospital_id + " ::From Service Dialog:: " + hospitals_name);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String service_name = edit_service.getText().toString();
                if (checkingInternet()) {
                    if (!service_name.isEmpty() && !service_name.startsWith(" ")) {
                        buttonSubmit.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_HOSPITAL_SERVICES, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v(TAG, "Response: " + response);
                                if (response.trim().equalsIgnoreCase("0")) {
                                    Log.v(TAG, response);
                                    hideKeyboardSoft();
                                    showMessage("Service Well Inserted");
                                    getServices(id_hospital);
                                    customArrayAdapterService.addAll();
                                    customArrayAdapterService.notifyDataSetChanged();
                                    linearSpinnerService.setVisibility(View.VISIBLE);
                                    dialogService.dismiss();
                                } else {
                                    Log.e(TAG, response);
                                    progressBar.setVisibility(View.GONE);
                                    showMessage("Error while adding a new Service... Try Later");
                                    buttonSubmit.setVisibility(View.VISIBLE);
                                    customArrayAdapterService.clear();
//                                    customArrayAdapterService.clearServiceItems(serviceItemsArrayList);
                                    linearSpinnerService.setVisibility(View.GONE);
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
                                map.put("service_name", service_name);
                                map.put("id_hospital", hospital_id);
                                map.put("hospital_name", hospitals_name);
                                return map;
                            }
                        };
                        getStringRequeue(stringRequest);
                    } else {
                        showMessage(getString(R.string.empty_field));
                    }
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogService.dismiss();
            }
        });
        dialogService.show();
        dialogService.getWindow().setAttributes(layoutParams);
    }

    //    Method to manage the errors with Volley
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
//    End of the management of the Volley Errors

    //    Method to check the internet
    public boolean checkingInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
    }
//    End of the method that allow us to check the internet

    //    This method allow us to hide the keyboard
    private void hideKeyboardSoft() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
//    End of the @hideKeyboardSoft method

    //    This method shows a message using the Toast
    private void showMessage(String message) {
        Toast.makeText(NewOpinion.this, message, Toast.LENGTH_SHORT).show();
    } //    End of @showMessage method
}