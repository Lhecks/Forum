package com.hame.forum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hame.forum.R;
import com.hame.forum.models.CountryItems;
import com.hame.forum.models.ServiceItems;

import java.util.List;

public class CustomArrayAdapterService extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<ServiceItems> itemsServices;
    private final int mResource;

    public CustomArrayAdapterService(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        itemsServices = objects;
    }

    public void updateServiceItems(List<ServiceItems> serviceItems) {
        itemsServices.clear();
        itemsServices.addAll(serviceItems);
        this.notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView id_service_hospital = view.findViewById(R.id.id_service_hospital);
        TextView nameServiceHospital = view.findViewById(R.id.name_service_hospital);
        ServiceItems serviceItems = itemsServices.get(position);

        id_service_hospital.setText(String.valueOf(serviceItems.getIdService()));
        nameServiceHospital.setText(serviceItems.getServiceName());

        return view;
    }
}
