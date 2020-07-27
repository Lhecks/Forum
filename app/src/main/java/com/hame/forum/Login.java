package com.hame.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.UserItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hame.forum.controller.app.AppConfig.URL_LOGIN;
import static com.hame.forum.controller.utils.Constant.USER_EMAIL;
import static com.hame.forum.controller.utils.Constant.USER_PASSWORD;

public class Login extends AppCompatActivity {
    private static final String TAG = Login.class.getSimpleName();
    private EditText lUserEmail, lUserPassword;
    private ProgressBar progressBar_l;
    private Button submitButton_l;
    private UserItems userItems = new UserItems();
    private SessionManager sessionManager;
    private String user_email;
    private String user_password;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lUserEmail = findViewById(R.id.login_user_email);
        lUserPassword = findViewById(R.id.login_user_password);
        progressBar_l = findViewById(R.id.login_progress_bar);
        submitButton_l = findViewById(R.id.button_login);
        textView = findViewById(R.id.go_to_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registered.class));
                finish();
            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        submitButton_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = lUserEmail.getText().toString();
                user_password = lUserPassword.getText().toString();
                if (checkingInternet()) {
                    getLogged();
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });
        hideKeyboardSoft();
    }

    private void getLogged() {
        progressBar_l.setVisibility(View.VISIBLE);
        submitButton_l.setVisibility(View.GONE);
        user_email = lUserEmail.getText().toString();
        user_password = lUserPassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d(TAG, "Login Response: " + response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        progressBar_l.setVisibility(View.GONE);
                        submitButton_l.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Phone ou Mot de passe incorrect", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            sessionManager.userLogin(new UserItems(
                                    jsonObject.getString("id_user"),
                                    jsonObject.getString("id"),
                                    jsonObject.getString("user_name"),
                                    jsonObject.getString("user_first_name"),
                                    jsonObject.getString("user_birthday"),
                                    jsonObject.getString("user_sex"),
                                    jsonObject.getString("user_phone"),
                                    jsonObject.getString("user_email"),
                                    jsonObject.getString("nom"),
                                    jsonObject.getString("user_profession")
                            ));

                            sessionManager.setLogin(true);

                            progressBar_l.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle());
                                finish();
                            } else {
                                startActivity(intent);
                                finish();
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
                showMessage(error.getMessage() + "\n ");
                if (error instanceof NetworkError) {
                    progressBar_l.setVisibility(View.GONE);
                    submitButton_l.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_network_error));
                } else if (error instanceof ServerError) {
                    progressBar_l.setVisibility(View.GONE);
                    submitButton_l.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_server_error));
                } else if (error instanceof AuthFailureError) {
                    progressBar_l.setVisibility(View.GONE);
                    submitButton_l.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_auth_fail));
                } else if (error instanceof ParseError) {
                    progressBar_l.setVisibility(View.GONE);
                    submitButton_l.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_parse_error));
                } else if (error instanceof TimeoutError) {
                    progressBar_l.setVisibility(View.GONE);
                    submitButton_l.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_time_out_error));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(USER_EMAIL, user_email);
                params.put(USER_PASSWORD, user_password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 3;
    }

    private boolean emptyField(String fieldEmpty) {
        return !fieldEmpty.equals("");
    }
}
