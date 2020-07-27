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

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<CountryItems> items;
    private final int mResource;

    public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
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

        TextView idPays = view.findViewById(R.id.id_country);
        TextView nomPays = view.findViewById(R.id.name_country);
        CountryItems paysItem = items.get(position);

        idPays.setText(String.valueOf(paysItem.getIdCountry()));
        nomPays.setText(paysItem.getCountryName());

        return view;
    }

}
