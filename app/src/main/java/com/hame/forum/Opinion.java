package com.hame.forum;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hame.forum.adapter.OpinionAdapter;
import com.hame.forum.controller.app.AppConfig;
import com.hame.forum.models.OpinionItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Opinion extends AppCompatActivity {

    private static final String TAG = Opinion.class.getSimpleName();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private OpinionAdapter opinionAdapter;
    private List<OpinionItems> opinionItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.opinion_progress_bar);
        recyclerView = findViewById(R.id.recyclerview_opinion);
        if (checkingInternet()) {
//            showMessage("Be right back");
            getOpinion();
        } else {
            showMessage(getString(R.string.check_internet));
        }

        FloatingActionButton fab = findViewById(R.id.fab_add_new_opinion);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Opinion.this, NewOpinion.class));
            }
        });
    }

    private void getOpinion() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, AppConfig.URL_JSON_GET_OPINION, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, String.valueOf(response));
                opinionItemsList = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id_opinion = jsonObject.getInt("opinion_id");
                        String opinion = jsonObject.getString("opinion_content");
                        String hospital_name = jsonObject.getString("hospital_name");
                        String service_name = jsonObject.getString("service_name");
                        String date_option = jsonObject.getString("opinion_date");
                        String time_option = jsonObject.getString("opinion_time");
                        String user_name = jsonObject.getString("user_name");
                        String rating_bar = jsonObject.getString("rating");
                        OpinionItems opinionItems = new OpinionItems(
                                id_opinion, opinion, hospital_name, service_name,
                                date_option, time_option, user_name, rating_bar);
                        opinionItemsList.add(opinionItems);

                        progressBar.setVisibility(View.GONE);
                        opinionAdapter = new OpinionAdapter(getApplicationContext(), opinionItemsList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(opinionAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }


    public boolean checkingInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
    }

    private void showMessage(String message) {
        Toast.makeText(Opinion.this, message, Toast.LENGTH_SHORT).show();
    }
}