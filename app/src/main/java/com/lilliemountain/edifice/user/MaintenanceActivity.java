package com.lilliemountain.edifice.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.POJO.maintenance.Maintenance;
import com.lilliemountain.edifice.R;

import java.util.List;

public class MaintenanceActivity extends AppCompatActivity {
    UserMaintenanceActivityAdapter userMaintenanceActivityAdapter;
    Maintenance maintenance;
    TextView name,buildingflat,billfor,unitarea,billdate,subtotal,interest,totalbill,pending,tdap,totalamount2;
    RecyclerView recyclerView;

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
        interest.setText("Interest : "+maintenance.getId());
        totalbill.setText("Total Bill : "+maintenance.getTotalbill());
        pending.setText("Pending : "+maintenance.getPending());
        tdap.setText("Total Due & Payable Amount : "+(maintenance.getTotalbill()+maintenance.getPayable()));
        totalamount2.setText("Total : "+(maintenance.getTotalbill()+maintenance.getPayable()));
        long amount=(maintenance.getTotalbill()+maintenance.getPayable());
    }
}
