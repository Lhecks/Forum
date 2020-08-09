package com.hame.forum.bottomSheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class FragmentBottomSheetHospital extends BottomSheetDialogFragment {
    private static final String TAG = FragmentBottomSheetHospital.class.getSimpleName();
    private EditText editHospital, editHospitalAddress;
    private CityItems cityItems = new CityItems();
    private SessionManager sessionManager;
    private HospitalItems hospitalItems = new HospitalItems();
    private Button buttonSubmit;
    private int city_id;
    private ProgressBar progressBar;
    private Context context;
    private String id_city, city_name, hospital_name, hospital_address;
    private LinearLayout linearLayout;

    public static FragmentBottomSheetHospital newInstance() {
        return new FragmentBottomSheetHospital();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_hospital_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        sessionManager = new SessionManager(view.getContext());
        ImageButton imageButton = view.findViewById(R.id.bt_close_hospital);
        editHospital = view.findViewById(R.id.edit_for_hospital_bottom_sheet_hospital);
        editHospitalAddress = view.findViewById(R.id.edit_for_hospital_address_bottom_sheet_hospital);
        progressBar = view.findViewById(R.id.progress_bar_bottom_sheet_hospital);
        buttonSubmit = view.findViewById(R.id.button_bottom_sheet_hospital);
        id_city = sessionManager.getCity().getIdCity();
        city_name = sessionManager.getCity().getCityName();

        Toast.makeText(getContext(), id_city + " ::H second tryout:: " + city_name, Toast.LENGTH_LONG).show();
//        showMessage(id_city+" H Second tryout "+ city_name);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                hospital_name = editHospital.getText().toString();
                hospital_address = editHospitalAddress.getText().toString();
                if (checkInternet()) {
                    if (!hospital_name.isEmpty() && !hospital_address.isEmpty()) {
                        hideKeyboardSoft();
                        Bundle bundle = new Bundle();
                        String serviceBundle = sessionManager.getCity().getIdCity();
                        bundle.putString("id_city", serviceBundle);
//                        addHospital();
                        sendHospital();
                        dismiss();
                    } else {
//                        Log.d(TAG, "");
                        Toast.makeText(getContext(), getString(R.string.empty_field), Toast.LENGTH_LONG).show();
//                        showMessage(getString(R.string.empty_field));
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.check_internet), Toast.LENGTH_LONG).show();
//                    showMessage(getString(R.string.check_internet));
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        hideKeyboardSoft();
    }

    private void addHospital() {
        buttonSubmit.setVisibility(View.GONE);
        hospital_name = editHospital.getText().toString();
        hospital_address = editHospitalAddress.getText().toString();
        id_city = sessionManager.getCity().getIdCity();
        city_name = sessionManager.getCity().getCityName();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    editHospital.getText().clear();
                    editHospitalAddress.getText().clear();
//                    showMessage("City Well Inserted");
//                    hideKeyboardSoft();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
//                    showMessage("Error while adding a new City... Try again");
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
                map.put("hospital_address", hospital_address);
                map.put("id_city", id_city);
                map.put("city_name", city_name);
                return map;
            }
        };
        getStringRequeue(stringRequest);
    }

    private void sendHospital() {
        buttonSubmit.setVisibility(View.GONE);
        hospital_name = editHospital.getText().toString();
        hospital_address = editHospitalAddress.getText().toString();
        id_city = sessionManager.getCity().getIdCity();
        city_name = sessionManager.getCity().getCityName();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "Response: " + response);
                if (response.trim().equalsIgnoreCase("0")) {
                    Log.v(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Hospital Well Inserted");
//                    Toast.makeText(getContext(), "Hospital Well Inserted", Toast.LENGTH_LONG).show();
//                    showMessage("Hospital Well Inserted");
                    hideKeyboardSoft();
                } else {
                    Log.e(TAG, response);
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Error while adding a new Hospital... Try Later");
//                    Toast.makeText(getContext(), "Error while adding a new Hospital... Try Later", Toast.LENGTH_LONG).show();
//                    showMessage("Error while adding a new Hospital... Try Later");
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
                map.put("hospital_address", hospital_address);
                map.put("id_city", id_city);
                map.put("city_name", city_name);
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
            Toast.makeText(getContext(), getString(R.string.volley_network_error), Toast.LENGTH_LONG).show();
//            showMessage(getString(R.string.volley_network_error));
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getString(R.string.volley_server_error), Toast.LENGTH_LONG).show();
//            showMessage(getString(R.string.volley_server_error));
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getString(R.string.volley_auth_fail), Toast.LENGTH_LONG).show();
//            showMessage(getString(R.string.volley_auth_fail));
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getString(R.string.volley_parse_error), Toast.LENGTH_LONG).show();
//            showMessage(getString(R.string.volley_parse_error));
        } else if (error instanceof TimeoutError) {
            progressBar.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getString(R.string.volley_time_out_error), Toast.LENGTH_LONG).show();
//            showMessage(getString(R.string.volley_time_out_error));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
    }

//    private void showMessage(String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//    }

    private void hideKeyboardSoft() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}