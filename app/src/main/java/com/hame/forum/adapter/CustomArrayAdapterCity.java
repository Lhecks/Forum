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
import com.hame.forum.models.CityItems;

import java.util.List;

public class CustomArrayAdapterCity extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context myContext;
    private final List<CityItems> cityItem;
    private final int myResource;

    public CustomArrayAdapterCity(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);
        myContext = context;
        mInflater = LayoutInflater.from(context);
        myResource = resource;
        cityItem = objects;
    }

    public void updateCityItems(List<CityItems> cityItemsList) {
        cityItem.addAll(cityItemsList);
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

        TextView id_city = view.findViewById(R.id.id_city);
        TextView name_city = view.findViewById(R.id.name_city);
        CityItems cityItems = cityItem.get(position);

        id_city.setText(String.valueOf(cityItems.getIdCity()));
        name_city.setText(cityItems.getCityName());

        return view;
    }

    public void clearItemCity(List<CityItems> cityItems) {
        cityItem.clear();
        this.notifyDataSetChanged();
    }

}
