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
import com.hame.forum.models.HospitalItems;
import com.hame.forum.models.ServiceItems;

import java.util.List;

public class CustomArrayAdapterHospital extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context myContext;
    private final List<HospitalItems> itemsListHospitals;
    private final int myResource;

    public CustomArrayAdapterHospital(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);
        myContext = context;
        mInflater = LayoutInflater.from(context);
        myResource = resource;
        itemsListHospitals = objects;
    }

    public void updateServiceItems(List<HospitalItems> hospitalItem) {
        itemsListHospitals.clear();
        itemsListHospitals.addAll(hospitalItem);
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
        final View view = mInflater.inflate(myResource, parent, false);

        TextView id_hospital = view.findViewById(R.id.id_hospital);
        TextView name_hospital = view.findViewById(R.id.name_hospital);
        HospitalItems hospitalItems = itemsListHospitals.get(position);

        id_hospital.setText(String.valueOf(hospitalItems.getIdHospital()));
        name_hospital.setText(hospitalItems.getHospitalName());

        return view;
    }

}
