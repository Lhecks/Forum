package com.hame.forum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hame.forum.R;
import com.hame.forum.models.OpinionItems;

import java.util.List;


public class OpinionAdapter extends RecyclerView.Adapter<OpinionAdapter.OpinionHolder> {

    private Context context;
    private List<OpinionItems> opinionItemsList;

    public OpinionAdapter(Context context, List<OpinionItems> opinionItemsList) {
        this.context = context;
        this.opinionItemsList = opinionItemsList;
    }

    @NonNull
    @Override
    public OpinionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolderOpinion;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        viewHolderOpinion = layoutInflater.inflate(R.layout.adapter_opinion, parent, false);
        return new OpinionHolder(viewHolderOpinion);
    }

    @Override
    public void onBindViewHolder(@NonNull OpinionHolder holder, int position) {
        OpinionItems opinionItems = opinionItemsList.get(position);
        holder.bindItemsOpinions(opinionItems);
    }

    @Override
    public int getItemCount() {
        return opinionItemsList.size();
    }

    public static class OpinionHolder extends RecyclerView.ViewHolder {
        private TextView userName, dateTime, hospitalName, serviceName, opinionContent;
        private RatingBar ratingBar;

        public OpinionHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.opinion_adapter_get_user_name);
            dateTime = itemView.findViewById(R.id.opinion_adapter_get_date_time);
            hospitalName = itemView.findViewById(R.id.opinion_adapter_get_hospital_name);
            serviceName = itemView.findViewById(R.id.opinion_adapter_get_service_name);
            opinionContent = itemView.findViewById(R.id.opinion_adapter_get_content);
            ratingBar = itemView.findViewById(R.id.rating_bar_opinion);
        }

        void bindItemsOpinions(final OpinionItems opinionItems) {
            userName.setText(opinionItems.getUserName());
            hospitalName.setText(opinionItems.getHospitalName());
            opinionContent.setText(opinionItems.getOpinion());
            serviceName.setText(opinionItems.getServiceName());
            ratingBar.setRating(Float.parseFloat(opinionItems.getRating_bar()));

            String date_time = opinionItems.getDateOpinion() + " at " + opinionItems.getTimeOpinion();
            dateTime.setText(date_time);
        }
    }
}
