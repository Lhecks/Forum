package com.hame.forum;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.hame.forum.controller.utils.Constant;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.ForumItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.hame.forum.controller.app.AppConfig.URL_INSERT_SUBJECT;
import static com.hame.forum.controller.utils.Constant.FORUM_SUBJECT;
import static com.hame.forum.controller.utils.Constant.ID_USER;
import static com.hame.forum.controller.utils.Constant.USER_NAME;

public class NewSubject extends AppCompatActivity {

    private static final String TAG = NewSubject.class.getSimpleName();
    private EditText edit_forum_subject;
    private ProgressBar progressBar;
    private Button button_subject;
    private SessionManager sessionManager;
    private String user_name;
    private TextView textCounting;
    private String forum_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subject);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit_forum_subject = findViewById(R.id.edit_new_subject);
        progressBar = findViewById(R.id.progress_new_subject);
        button_subject = findViewById(R.id.button_new_subject);
        textCounting = findViewById(R.id.text_counting);
        sessionManager = new SessionManager(getApplicationContext());

        edit_forum_subject.addTextChangedListener(new TextWatcher() {
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
                textCounting.setText(currentLength + " Characters");
            }
        });
        button_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forum_subject = edit_forum_subject.getText().toString();
                if (checkingInternet()) {
                    if (!forum_subject.isEmpty() && !forum_subject.startsWith(" ")) {
                        hideKeyboardSoft();
                        sendForum();
                    } else {
                        showMessage(getString(R.string.empty_field));
                    }
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });
        hideKeyboardSoft();
    }

    private void sendForum() {
        user_name = sessionManager.getUser().getUser_name();
        forum_subject = edit_forum_subject.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        button_subject.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_SUBJECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("New Subject Well Inserted");
                    startActivity(new Intent(getApplicationContext(), Forum.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    button_subject.setVisibility(View.VISIBLE);
                    showMessage("Error while adding a new Subject... try later");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    progressBar.setVisibility(View.GONE);
                    button_subject.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_network_error));
                } else if (error instanceof ServerError) {
                    progressBar.setVisibility(View.GONE);
                    button_subject.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_server_error));
                } else if (error instanceof AuthFailureError) {
                    progressBar.setVisibility(View.GONE);
                    button_subject.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_auth_fail));
                } else if (error instanceof ParseError) {
                    progressBar.setVisibility(View.GONE);
                    button_subject.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_parse_error));
                } else if (error instanceof TimeoutError) {
                    progressBar.setVisibility(View.GONE);
                    button_subject.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_time_out_error));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put(FORUM_SUBJECT, forum_subject);
                map.put(USER_NAME, user_name);
                map.put(ID_USER, sessionManager.getUser().getIdUser());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
        Toast.makeText(NewSubject.this, message, Toast.LENGTH_SHORT).show();
    }
}
