package com.hame.forum.bottomSheet;

import android.app.Activity;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hame.forum.R;
import com.hame.forum.controller.app.AppConfig;
import com.hame.forum.controller.utils.SessionManager;
import com.hame.forum.models.CityItems;
import com.hame.forum.models.HospitalItems;
import com.hame.forum.models.ServiceItems;

import java.util.HashMap;
import java.util.Map;

public class FragmentBottomSheetService extends BottomSheetDialogFragment {
    private static final String TAG = FragmentBottomSheetService.class.getSimpleName();
    private EditText editService;
    private HospitalItems hospitalItems = new HospitalItems();
    private ServiceItems serviceItems = new ServiceItems();
    private Button buttonSubmit;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    private Context context;
    private String id_hospital, hospital_name, service_name;

    public static FragmentBottomSheetService newInstance() {
        return new FragmentBottomSheetService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_service_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        sessionManager = new SessionManager(view.getContext());
        id_hospital = sessionManager.getHospital().getIdHospital();
        hospital_name = sessionManager.getHospital().getHospitalName();
        showMessage(id_hospital + " Service Second tryout " + hospital_name);

        ImageButton imageButton = view.findViewById(R.id.bt_close_service);
        editService = view.findViewById(R.id.edit_service_bottom_sheet_service);
        progressBar = view.findViewById(R.id.progress_bar_bottom_sheet_service);
        buttonSubmit = view.findViewById(R.id.button_bottom_sheet_service);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                service_name = editService.getText().toString();
                if (checkInternet()) {
                    if (!service_name.isEmpty()) {
                        hideKeyboardSoft();
                        Bundle bundle = new Bundle();
                        String serviceBundle = sessionManager.getHospital().getIdHospital();
                        bundle.putString("Service_message", serviceBundle);
                        sendService();
                        dismiss();
                    } else {
                        showMessage(getString(R.string.empty_field));
                    }
                } else {
                    showMessage(getString(R.string.check_internet));
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void sendService() {
        buttonSubmit.setVisibility(View.GONE);
        service_name = editService.getText().toString();
        id_hospital = sessionManager.getHospital().getIdHospital();
        hospital_name = sessionManager.getHospital().getHospitalName();
//        id_hospital = String.valueOf(hospitalItems.getIdHospital());
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
                map.put("id_hospital", id_hospital);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}