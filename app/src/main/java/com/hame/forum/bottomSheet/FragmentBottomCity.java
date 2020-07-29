package com.hame.forum.bottomSheet;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     FragmentBottomCity.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class FragmentBottomCity extends BottomSheetDialogFragment {
    private static final String TAG = FragmentBottomCity.class.getSimpleName();
    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private EditText editCity, editService, editHospital, editHospitalAddress;
    private SessionManager sessionManager;
    private Button buttonSubmit;
    private ProgressBar progressBar;
    private Context context;
    private String city_name, hospital_name, service_name;

    // TODO: Customize parameters
    public static FragmentBottomCity newInstance(int itemCount) {
        final FragmentBottomCity fragment = new FragmentBottomCity();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_city_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    private void sendCiy() {
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
                map.put("id", sessionManager.getUser().getId_Country());
                map.put("nom", sessionManager.getUser().getUser_country());
                return map;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void sendHospital() {
        buttonSubmit.setVisibility(View.GONE);
        hospital_name = editHospital.getText().toString();
//        hospital_address = editHospitalAddress.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("Hospital Well Inserted");
//                    hideKeyboardSoft();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("Error while adding a new Hospital... Try Later");
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
                map.put("hospital_name", hospital_name);
//                map.put("hospital_address", hospital_address);
//                map.put("id_city", id_city);
                map.put("city_name", city_name);
                return map;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void sendService() {
        buttonSubmit.setVisibility(View.GONE);
        service_name = editService.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_HOSPITAL_SERVICES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("Service Well Inserted");
//                    hideKeyboardSoft();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    showMessage("Error while adding a new Service... Try Later");
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
                map.put("service_name", service_name);
//                map.put("id_hospital", id_hospitals);
                map.put("hospital_name", hospital_name);
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

//    public boolean checkingInternet() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
//        assert connectivityManager != null;
//        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
//    }

//    private void hideKeyboardSoft() {
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}