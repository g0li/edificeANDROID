package com.lilliemountain.edifice.user;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.Notices;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter2 extends RecyclerView.Adapter<NoticeAdapter2.NoticeHolder>  {
    List<Notices> list=new ArrayList<>();
    private List<Notices> listFiltered;

    public NoticeAdapter2(List<Notices> list) {
        this.list = list;
        this.listFiltered=this.list;
    }

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notice,viewGroup,false);
        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeHolder NoticeHolder, int i) {
        NoticeHolder.title.setText(listFiltered.get(i).getNoticeTitle());
        NoticeHolder.date.setText(listFiltered.get(i).getDate());
        NoticeHolder.position=i;
        try {
            NoticeHolder.description.setText(listFiltered.get(i).getNoticeText().substring(0,100)+"...");
        } catch (Exception e) {
            NoticeHolder.description.setText(listFiltered.get(i).getNoticeText()    );

        }
        NoticeHolder.notices=listFiltered.get(i);

    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    public class NoticeHolder extends RecyclerView.ViewHolder {
        TextView title,description,date;
        Notices notices;
        int position=0;
        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.desc);
            date=itemView.findViewById(R.id.date);
            itemView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetails(v);
                }
            });
            itemView.findViewById(R.id.modify).setVisibility(View.GONE);
        }

    public void showDetails(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_notice);
        TextView title,desc;
        TextView daydate,type;
        title=dialog.findViewById(R.id.name);
        desc=dialog.findViewById(R.id.desc);
        daydate=dialog.findViewById(R.id.daydate);
        type=dialog.findViewById(R.id.type);

        ImageView close=dialog.findViewById(R.id.delete);

        title.setText(notices.getNoticeTitle());
        desc.setText(notices.getNoticeText());
        daydate.setText(notices.getDay()+" , "+notices.getDate());
        type.setText(notices.getNoticeType());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        }
    }

}
