package com.hame.forum.bottomSheet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hame.forum.NewOpinion;
import com.hame.forum.R;
import com.hame.forum.controller.app.AppConfig;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.CityItems;
import com.hame.forum.models.HospitalItems;
import com.hame.forum.models.ServiceItems;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.hame.forum.controller.utils.Constant.SHARED_PREF_NAME;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     FragmentBottomCity.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class FragmentBottomCity extends BottomSheetDialogFragment {
    private static final String TAG = FragmentBottomCity.class.getSimpleName();
    private EditText editCity;
    private SessionManager sessionManager;
    private CityItems cityItems = new CityItems();
    private Button buttonSubmit;
    private ProgressBar progressBar;
    private Context context;
    private String id_city, city_name;

    public static FragmentBottomCity newInstance() {
        return new FragmentBottomCity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_city_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(view.getContext());
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        editCity = view.findViewById(R.id.edit_for_city_bottom_sheet_city);
        progressBar = view.findViewById(R.id.progress_bar_bottom_sheet_city);
        buttonSubmit = view.findViewById(R.id.button_bottom_sheet_city);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                city_name = editCity.getText().toString();
                if (checkInternet()) {
//                    TODO
                    showMessage("Coming soon");
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });
        hideKeyboardSoft();

    }

    private void sendCiy() {
        progressBar.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.GONE);
        city_name = editCity.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_CITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("City Well Inserted");
//                    hideKeyboardSoft();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("Error while adding a new City... Try Later");
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
//                map.put("city_name", cityItems.getCityName());
                map.put("id", sessionManager.getUser().getId_Country());
                map.put("nom", sessionManager.getUser().getUser_country());
                return map;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void getStringRequeue(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboardSoft() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}