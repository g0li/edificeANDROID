package com.lilliemountain.edifice.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.POJO.maintenance.Maintenance;
import com.lilliemountain.edifice.R;
import com.paykun.sdk.Events;
import com.paykun.sdk.GlobalBus;
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.PaykunHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MaintenanceActivity extends AppCompatActivity {
    UserMaintenanceActivityAdapter userMaintenanceActivityAdapter;
    Maintenance maintenance;
    TextView name,buildingflat,billfor,unitarea,billdate,subtotal,interest,totalbill,pending,tdap,totalamount2;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_activity);
        maintenance=getIntent().getParcelableExtra("maintenance");
        List<Bill> bills=maintenance.getBill();
        userMaintenanceActivityAdapter=new UserMaintenanceActivityAdapter(bills);
        name=findViewById(R.id.name);
        buildingflat=findViewById(R.id.buildingflat);
        billfor=findViewById(R.id.billfor);
        unitarea=findViewById(R.id.unitarea);
        billdate=findViewById(R.id.billdate);
        subtotal=findViewById(R.id.subTotal);
        interest=findViewById(R.id.interest);
        totalbill=findViewById(R.id.totalbill);
        pending=findViewById(R.id.pending);
        tdap=findViewById(R.id.tdap);
        totalamount2=findViewById(R.id.totalamount2);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userMaintenanceActivityAdapter);
        name.setText("Name : "+maintenance.getHeader().getResident());
        buildingflat.setText(maintenance.getHeader().getBuilding()+" \n"+maintenance.getHeader().getFlat());
        billfor.setText("Bill For : "+maintenance.getHeader().getBillfor());
        unitarea.setText("Unit Area : "+maintenance.getHeader().getCarpetarea()+" sq. ft.");
        billdate.setText("Bill Date : "+maintenance.getHeader().getDate());
        subtotal.setText("Subtotal : "+maintenance.getSubtotal());
//        interest.setText("Interest : ");
//        totalbill.setText("Total Bill : "+maintenance.getTotalbill());
        pending.setText("Pending : "+maintenance.getPending());
        totalbill.setText("Total Bill : "+(maintenance.getTotalbill()+maintenance.getPayable())+"+ 5% transaction fees");
//        tdap.setText("Total Due & Payable Amount : "+(maintenance.getTotalbill()+maintenance.getPayable()));
        amount=(maintenance.getTotalbill()+maintenance.getPayable());
        amount=amount+ (5*amount)/100;
        tdap.setText("Total Due & Payable Amount : "+amount);

        totalamount2.setText("Total : "+amount);
        final String em= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("merchant_id",getString(R.string.merchantId));
                    object.put("access_token",getString(R.string.accessTokenTest));
                    object.put("customer_name",maintenance.getHeader().getResident());
                    object.put("customer_email",em);
                    object.put("customer_phone",maintenance.getHeader().getPhoneno());
                    object.put("product_name","Maintenance: "+maintenance.getHeader().getBillfor());
                    object.put("order_no",System.currentTimeMillis()); // order no. should have 10 to 30 character in numeric format
                    object.put("amount",amount+"");  // minimum amount should be 10
                    object.put("isLive",false); // need to send false if you are in sandbox mode
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new PaykunApiCall.Builder(MaintenanceActivity.this).sendJsonObject(object);
            }
        });
    }
    long amount;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getResults(Events.PaymentMessage message) {
        if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SUCCESS)){
            // do your stuff here
            // message.getTransactionId() will return your failed or succeed transaction id
            if(!TextUtils.isEmpty(message.getTransactionId())) {
                maintenance.setStatus("Paid");
                Toast.makeText(MaintenanceActivity.this, "Your Transaction is succeed with transaction id : "+message.getTransactionId(), Toast.LENGTH_SHORT).show();
            }
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_FAILED)){
            // do your stuff here
            Toast.makeText(MaintenanceActivity.this,"Your Transaction is failed",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)){
            // do your stuff here
            Toast.makeText(MaintenanceActivity.this, PaykunHelper.MESSAGE_SERVER_ISSUE,Toast.LENGTH_SHORT).show();
        }else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)){
            // do your stuff here
            Toast.makeText(MaintenanceActivity.this,"Access Token missing",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)){
            // do your stuff here
            Toast.makeText(MaintenanceActivity.this,"Merchant Id is missing",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)){
            Toast.makeText(MaintenanceActivity.this,"Invalid Request",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Register this activity to listen to event.
        GlobalBus.getBus().register(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unregister from activity
        GlobalBus.getBus().unregister(this);
    }
}
