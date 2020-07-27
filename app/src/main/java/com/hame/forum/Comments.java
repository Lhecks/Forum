package com.hame.forum;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hame.forum.adapter.CommentAdapter;
import com.hame.forum.controller.utils.Constant;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.CommentItems;
import com.hame.forum.models.ForumItems;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.hame.forum.controller.app.AppConfig.URL_INPUT_NEW_COMMENT;
import static com.hame.forum.controller.app.AppConfig.URL_JSON_GET_COMMENT_BY_ID;
import static com.hame.forum.controller.utils.Constant.FORUM_ID;
import static com.hame.forum.controller.utils.Constant.ID_USER;
import static com.hame.forum.controller.utils.Constant.NEW_COMMENT;
import static com.hame.forum.controller.utils.Constant.USER_NAME;

public class Comments extends AppCompatActivity {
    public static final String EXTRA_ID_FORUM = "com.hame.forum.comment.id_forum";
    private static final String TAG = Comments.class.getSimpleName();
    ForumItems forumItems;
    int id_forum;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<CommentItems> commentsList;
    private EditText edit_forum_comment;
    private SessionManager sessionManager;
    private String user_name;
    private String new_comment;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview_comment);
        progressBar = findViewById(R.id.comment_progress_bar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        forumItems = Objects.requireNonNull(getIntent().getExtras()).getParcelable("forum_subject");
//        ForumItems forumItems = getIntent().getExtras().getParcelable("forum_subject");
        assert forumItems != null;
        id_forum = forumItems.getId_forum();
        TextView text_user_name = findViewById(R.id.forum_detail_get_user_name);
        TextView text_forum_subject = findViewById(R.id.forum_detail_get_subject);
        TextView text_forum_date_time = findViewById(R.id.forum_detail_get_date_time);

        String user_name = "Published by: " + forumItems.getUser_name();
        String forum_subject = forumItems.getForum_subject();
        String date_time = forumItems.getForum_time() + " at " + forumItems.getDate_forum();

        text_user_name.setText(user_name);
        text_forum_subject.setText(forum_subject);
        text_forum_date_time.setText(date_time);

        showMessage("id forum " + id_forum);

        if (checkingInternet()) {
            getComment(String.valueOf(id_forum));
        } else {
            showMessage(getString(R.string.check_internet));
        }

        edit_forum_comment = findViewById(R.id.edit_new_comments);
        FloatingActionButton fab = findViewById(R.id.fab_add_new_comments);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_comment = edit_forum_comment.getText().toString();
                if (checkingInternet()) {
                    if (!new_comment.isEmpty()) {
                        sendComment();
                        hideKeyboardSoft();
//                        getComment(String.valueOf(id_forum));
                    } else {
                        showMessage(getString(R.string.empty_field));
                    }
                } else {
                    showMessage(getString(R.string.check_internet));
                }
//                Intent intentForum = new Intent(Comments.this, NewComment.class);
////                intentForum.putExtra("forum_subject", forumItemsList.get(position));
//                intentForum.putExtra(EXTRA_ID_FORUM, forumItems.getId_forum());
//                Log.e("COmment id forum ","ID FORUM "+ forumItems.getId_forum());
//                startActivity(intentForum);
//                finish();
            }
        });
        hideKeyboardSoft();
    }

    private void getComment(final String id_forum) {
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_JSON_GET_COMMENT_BY_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, String.valueOf(response));
                commentsList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (response.equals("[]") || jsonArray.isNull(0)) {
                        progressBar.setVisibility(View.GONE);
                        showMessage("Error while getting Comments");
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CommentItems commentItems = new CommentItems(
                                    jsonObject.getInt("id_comment"),
                                    jsonObject.getString("id_user"),
                                    jsonObject.getString("new_comment"),
                                    jsonObject.getString("date_comment"),
                                    jsonObject.getString("time_comment"),
                                    jsonObject.getString("user_name"));
                            commentsList.add(commentItems);

                            commentAdapter = new CommentAdapter(getApplicationContext(), commentsList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(commentAdapter);
                            progressBar.setVisibility(View.GONE);
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
                Map<String, String> map = new HashMap<>();
                map.put("id_forum", id_forum);
                return map;
            }
        };
        getRequestQueue(stringRequest);
    }

    private void sendComment() {
        hideKeyboardSoft();
        user_name = sessionManager.getUser().getUser_name();
        new_comment = edit_forum_comment.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INPUT_NEW_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("New Comment Well Inserted" + id_forum);
                    edit_forum_comment.getText().clear();
                    getComment(String.valueOf(id_forum));
                    hideKeyboardSoft();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("Error while adding a new Comment  vn... try later");
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
                Map<String, String> map = new HashMap<>();
                map.put(NEW_COMMENT, new_comment);
                map.put(ID_USER, sessionManager.getUser().getIdUser());
                map.put(FORUM_ID, String.valueOf(id_forum));
                map.put(USER_NAME, user_name);
                return map;
            }
        };
        getRequestQueue(stringRequest);
    }

    public boolean checkingInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
    }

    private void showMessage(String message) {
        Toast.makeText(Comments.this, message, Toast.LENGTH_SHORT).show();
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

    private void getRequestQueue(StringRequest stringRequest) {
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void hideKeyboardSoft() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}