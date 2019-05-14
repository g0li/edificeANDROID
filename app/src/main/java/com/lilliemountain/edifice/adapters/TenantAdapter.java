package com.lilliemountain.edifice.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.Tenant;
import com.lilliemountain.edifice.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TenantAdapter extends RecyclerView.Adapter<TenantAdapter.TenantHolder> {
    List<Tenant> list;
    OnTenantListener onTenantListener;
    public TenantAdapter(List<Tenant> list, OnTenantListener onTenantListener) {
        this.list = list;
        this.onTenantListener = onTenantListener;
    }

    @NonNull
    @Override
    public TenantHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tenant,viewGroup,false);
        return new TenantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenantHolder tenantHolder, final int i) {
        tenantHolder.name.setText("Name : "+list.get(i).getTenantName());
        tenantHolder.t=list.get(i);Picasso.get()
                .load(Uri.parse(list.get(i).getGridy()))
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.userlogin)
                .into(tenantHolder.gridy);
        tenantHolder.i=i;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TenantHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton call,sms,more;
        ImageView delete,gridy;
        Tenant t;
        int i;
        private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
        public TenantHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tenantname);
            call=itemView.findViewById(R.id.call);
            sms=itemView.findViewById(R.id.sms);
            more=itemView.findViewById(R.id.more);
            delete=itemView.findViewById(R.id.delete);
            gridy=itemView.findViewById(R.id.gridy);
            itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTenantListener.RemoveTenant(i);
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO CALL INTENT
                    String no=t.getTenantphone();
                    if(!no.contains("+91"))
                    {
                        no="+91"+no;
                    }
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+no));
                    v.getContext().startActivity(callIntent);

                }
            });
            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO sms INTENT
                    String sms=t.getTenantphone();
                    if(!sms.contains("+91"))
                    {
                        sms="+91"+sms;
                    }
                    String smsbody="Your rent of Rs."+t.getTenantbaseprice()+" is due. Please Make the payment";
                    onTenantListener.SendSMS(sms,smsbody);

                }
            });
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTenant(v,t);
                }
            });
        }
        void showTenant(View v,Tenant t){
            Dialog dialog=new Dialog(v.getContext());
            dialog.setContentView(R.layout.dialog_tenant);
            TextView tenantName,tenantphone,tenantemail,tenantbaseprice,tenantemergencyname,tenantemergencyphone;
            tenantName=dialog.findViewById(R.id.tenantName);
            tenantphone=dialog.findViewById(R.id.tenantphone);
            tenantemail=dialog.findViewById(R.id.tenantemail);
            tenantbaseprice=dialog.findViewById(R.id.tenantbaseprice);
            tenantemergencyname=dialog.findViewById(R.id.tenantemergencyname);
            tenantemergencyphone=dialog.findViewById(R.id.tenantemergencyphone);

            tenantName.setText("Tenant Name :"+t.getTenantName());
            tenantphone.setText("Tenant Ph. No. :"+t.getTenantphone());
            tenantemail.setText("Tenant Email :"+t.getTenantemail());
            tenantbaseprice.setText("Rent :"+t.getTenantbaseprice());
            tenantemergencyname.setText("Emergency Contact :"+t.getTenantemergencyname());
            tenantemergencyphone.setText("Emergency Ph. No. :"+t.getTenantemergencyphone());
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
        }
    }
    public interface OnTenantListener {
        void RemoveTenant(int position);
        void SendSMS(String phone, String smsbody);
    }
}
