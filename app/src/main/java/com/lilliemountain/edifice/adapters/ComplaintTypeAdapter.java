package com.lilliemountain.edifice.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilliemountain.edifice.R;

import java.util.List;

public class ComplaintTypeAdapter extends RecyclerView.Adapter<ComplaintTypeAdapter.GuestListHolder> {
    private List<String> dataSource;
    public ComplaintTypeAdapter(List<String> dataArgs){
        dataSource = dataArgs;
    }

    @Override
    public GuestListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = new TextView(parent.getContext());
        GuestListHolder viewHolder = new GuestListHolder(view);
        return viewHolder;
    }

    public static class GuestListHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public GuestListHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
            textView.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
            textView.setTextSize(18);
        }
    }

    @Override
    public void onBindViewHolder(GuestListHolder holder, int position) {
        holder.textView.setText(dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}