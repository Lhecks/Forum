package com.hame.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hame.forum.adapter.CustomArrayAdapter;
import com.hame.forum.controller.app.AppConfig;
import com.hame.forum.controller.data.DatabaseHelper;
import com.hame.forum.controller.utils.Constant;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.CountryItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.hame.forum.controller.utils.Constant.COUNTRY_NAME;
import static com.hame.forum.controller.utils.Constant.ID_COUNTRY;
import static com.hame.forum.controller.utils.Constant.USER_BIRTHDAY;
import static com.hame.forum.controller.utils.Constant.USER_FIRST_NAME;
import static com.hame.forum.controller.utils.Constant.USER_NAME;
import static com.hame.forum.controller.utils.Constant.USER_PASSWORD;
import static com.hame.forum.controller.utils.Constant.USER_PHONE;
import static com.hame.forum.controller.utils.Constant.USER_PROFESSION;
import static com.hame.forum.controller.utils.Constant.USER_SEX;

public class Registered extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = Registered.class.getSimpleName();
    private EditText rUserName, rFirstName, rEmail, rPhone, rBirthday, rSex, rProfession, rPassword, rRepeatPassword;
    private Spinner rCountry;
    private ProgressBar progressBar_r;
    private TextView textView;
    private Button submitButton_r;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<CountryItems> countryItemsList = new ArrayList<>();
    private CustomArrayAdapter adapter;

    private String user_name, user_first_name, user_email, user_password, user_password_repeated, user_phone, user_sex,
            user_birthday, user_profession, id_country, user_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        rUserName = findViewById(R.id.register_user_name);
        rFirstName = findViewById(R.id.register_user_first_name);
        rEmail = findViewById(R.id.register_user_email);
        rPhone = findViewById(R.id.register_user_phone);
        rBirthday = findViewById(R.id.register_user_birthday);
        rCountry = findViewById(R.id.register_user_country);
        rSex = findViewById(R.id.register_user_sex);
        rProfession = findViewById(R.id.register_user_profession);
        rPassword = findViewById(R.id.register_user_password);
        rRepeatPassword = findViewById(R.id.register_user_repeat_password);
        progressBar_r = findViewById(R.id.register_progress_bar);
        submitButton_r = findViewById(R.id.register_button);
        textView = findViewById(R.id.go_to_login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        rCountry.setOnItemSelectedListener(this);
        if (checkingInternet()) {
            getCountries();
//            getCountry();
        } else {
            showMessage(getString(R.string.check_internet));
        }

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(Registered.this, MainActivity.class));
            finish();
        }

        submitButton_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = rUserName.getText().toString();
                user_first_name = rFirstName.getText().toString();
                user_email = rEmail.getText().toString();
                user_password = rPassword.getText().toString();
                user_password_repeated = rRepeatPassword.getText().toString();
                user_phone = rPhone.getText().toString();
                user_sex = rSex.getText().toString();
                user_birthday = rBirthday.getText().toString();
                user_profession = rProfession.getText().toString();

                if (checkingInternet()) {
                    if (!user_name.isEmpty() && !user_first_name.isEmpty() && !user_email.isEmpty()
                            && !user_password.isEmpty() && !user_phone.isEmpty() && !user_sex.isEmpty()
                            && !user_birthday.isEmpty() && !user_profession.isEmpty()) {
                        if (matchingPasswords(user_password, user_password_repeated)) {
                            if (!isPasswordValid(user_password)) {
//                            Passwords length
                                showMessage(getString(R.string.password_length));
                            } else {
                                getRegistered();
//                                register();
                            }
                        } else {
//                            Passwords do not match
                            showMessage(getString(R.string.password_matching));
                        }
                    } else {
                        showMessage(getString(R.string.empty_field));
                        hideKeyboardSoft();
                    }
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });
        hideKeyboardSoft();
    }

    private void getRegistered() {
        hideKeyboardSoft();
        progressBar_r.setVisibility(View.VISIBLE);
        submitButton_r.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.e(TAG, response);
                    progressBar_r.setVisibility(View.GONE);
                    showMessage("User Well Inserted");
                    startActivity(new Intent(getApplicationContext(), Login.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if (response.trim().equalsIgnoreCase("1")) {
                    Log.e(TAG, response);
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                    showMessage("Error while adding a new user... try later");
                } else if (response.trim().equalsIgnoreCase("2")) {
                    Log.e(TAG, response);
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                    showMessage("Sorry!!! The User already exist");
                } else {
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_network_error));
                } else if (error instanceof ServerError) {
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_server_error));
                } else if (error instanceof AuthFailureError) {
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_auth_fail));
                } else if (error instanceof ParseError) {
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_parse_error));
                } else if (error instanceof TimeoutError) {
                    progressBar_r.setVisibility(View.GONE);
                    submitButton_r.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_time_out_error));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(USER_NAME, user_name);
                params.put(USER_FIRST_NAME, user_first_name);
                params.put(Constant.USER_EMAIL, user_email);
                params.put(USER_PASSWORD, user_password);
                params.put(USER_PHONE, user_phone);
                params.put(USER_SEX, user_sex);
                params.put(USER_BIRTHDAY, user_birthday);
                params.put(ID_COUNTRY, id_country);
                params.put(COUNTRY_NAME, user_country);
                params.put(USER_PROFESSION, user_profession);
                return params;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void getCountries() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_JSON_GET_COUNTRIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                countryItemsList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("TAG", response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        Log.d("TAG", response);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CountryItems countryItems = new CountryItems(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("nom"),
                                    jsonObject.getString("indicatif")
                            );
                            countryItemsList.add(countryItems);
                            adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.item_country, countryItemsList);
                            rCountry.setAdapter(adapter);
                        }
                        rCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                id_country = ((TextView) view.findViewById(R.id.id_country)).getText().toString();
                                user_country = ((TextView) view.findViewById(R.id.name_country)).getText().toString();
                                showMessage(id_country + " : " + user_country);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
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
        });
        getStringRequeue(stringRequest);
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
        Toast.makeText(Registered.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean matchingPasswords(String first, String second) {
        return first.equals(second);
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 3;
    }

    private boolean emptyField(String fieldEmpty) {
        return fieldEmpty.equals("");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getStringRequeue(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(Registered.this);
        requestQueue.add(stringRequest);
    }

    private void getError(VolleyError error) {
        if (error instanceof NetworkError) {
            progressBar_r.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_network_error));
        } else if (error instanceof ServerError) {
            progressBar_r.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_server_error));
        } else if (error instanceof AuthFailureError) {
            progressBar_r.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_auth_fail));
        } else if (error instanceof ParseError) {
            progressBar_r.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_parse_error));
        } else if (error instanceof TimeoutError) {
            progressBar_r.setVisibility(View.GONE);
            showMessage(getString(R.string.volley_time_out_error));
        }
    }

}
