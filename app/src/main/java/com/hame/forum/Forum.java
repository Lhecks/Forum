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
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hame.forum.adapter.ForumAdapter;
import com.hame.forum.models.ForumItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hame.forum.controller.app.AppConfig.URL_JSON_GET_FORUM_SUBJECTS;

public class Forum extends AppCompatActivity {
    private static final String TAG = Forum.class.getSimpleName();
    private List<ForumItems> forumList;
    private ForumAdapter adapterForum;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.forum_progress_bar);
        recyclerView = findViewById(R.id.recyclerview_forum);
        ImageView imageViewInternet = findViewById(R.id.image_forum_check_internet);
        Glide.with(this).load(R.drawable.no_internet).into(imageViewInternet);

        if (checkingInternet()) {
            imageViewInternet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getForum();
        } else {
            imageViewInternet.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            showMessage(getString(R.string.check_internet));
        }

        FloatingActionButton fab_forum = findViewById(R.id.fab_add_new_forum);
        fab_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Forum.this, NewSubject.class));
                finish();
            }
        });
    }

    private void getForum() {
        progressBar.setVisibility(View.VISIBLE);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_JSON_GET_FORUM_SUBJECTS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, String.valueOf(response));
                        forumList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                int id_forum = object.getInt("id_forum");
                                int id_users = object.getInt("id_user");
                                String forum_subject = object.getString("forum_subject");
                                String forum_time = object.getString("forum_time");
                                String forum_date = object.getString("forum_date");
                                String user_name = object.getString("user_name");

                                ForumItems forum = new ForumItems(id_forum, id_users, forum_subject, user_name, forum_time, forum_date);
                                forumList.add(forum);

                                progressBar.setVisibility(View.GONE);
                                adapterForum = new ForumAdapter(getApplicationContext(), forumList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(adapterForum);
                            }
                        } catch (JSONException e) {
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
        Toast.makeText(Forum.this, message, Toast.LENGTH_SHORT).show();
    }
}
