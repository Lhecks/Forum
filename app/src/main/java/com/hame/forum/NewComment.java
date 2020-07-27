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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.hame.forum.controller.app.AppConfig.URL_INPUT_NEW_COMMENT;
import static com.hame.forum.controller.utils.Constant.FORUM_ID;
import static com.hame.forum.controller.utils.Constant.ID_USER;
import static com.hame.forum.controller.utils.Constant.NEW_COMMENT;
import static com.hame.forum.controller.utils.Constant.USER_NAME;

public class NewComment extends AppCompatActivity {
    private static final String TAG = NewComment.class.getSimpleName();
    private EditText edit_forum_comment;
    private ProgressBar progressBar;
    private Button button_comment;
    private SessionManager sessionManager;
    private int forum_id;
    private String user_name;
    private String new_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edit_forum_comment = findViewById(R.id.edit_new_comment);
        progressBar = findViewById(R.id.progress_new_comment);
        button_comment = findViewById(R.id.button_new_comment);
        sessionManager = new SessionManager(getApplicationContext());

  /*      assert forumItems != null;
        forumItems = new ForumItems(forum_id);*/
//        forum_id = forumItems.getId_forum();
        Intent extras = getIntent();
        forum_id = extras.getIntExtra(Comments.EXTRA_ID_FORUM, 0);
        Log.e("Comment id forum ", "ID FORUM " + forum_id);
        showMessage("" + forum_id);
  /*      if (extras!=null){
            forum_id = extras.getInt(Comments.EXTRA_ID_FORUM);
            showMessage(""+forum_id);
        }
        else
            showMessage("pas de id");*/

        button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_comment = edit_forum_comment.getText().toString();
                if (checkingInternet()) {
                    if (!new_comment.isEmpty()) {
                        sendComment();
//                        sendComment(forumItems);
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

    //    private void sendComment(final ForumItems forumItems) {
    private void sendComment() {
        //forum_subject_id = forumItems.getId_forum();
        user_name = sessionManager.getUser().getUser_name();
        new_comment = edit_forum_comment.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        button_comment.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INPUT_NEW_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("New Comment Well Inserted");
                    startActivity(new Intent(getApplicationContext(), Forum.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    button_comment.setVisibility(View.VISIBLE);
                    showMessage("Error while adding a new Comment  vn... try later");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    progressBar.setVisibility(View.GONE);
                    button_comment.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_network_error));
                } else if (error instanceof ServerError) {
                    progressBar.setVisibility(View.GONE);
                    button_comment.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_server_error));
                } else if (error instanceof AuthFailureError) {
                    progressBar.setVisibility(View.GONE);
                    button_comment.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_auth_fail));
                } else if (error instanceof ParseError) {
                    progressBar.setVisibility(View.GONE);
                    button_comment.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_parse_error));
                } else if (error instanceof TimeoutError) {
                    progressBar.setVisibility(View.GONE);
                    button_comment.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.volley_time_out_error));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put(NEW_COMMENT, new_comment);
                map.put(ID_USER, sessionManager.getUser().getIdUser());
                map.put(FORUM_ID, String.valueOf(forum_id));
                map.put(USER_NAME, user_name);
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
        Toast.makeText(NewComment.this, message, Toast.LENGTH_SHORT).show();
    }
}