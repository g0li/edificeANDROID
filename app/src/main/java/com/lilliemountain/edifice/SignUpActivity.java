package com.lilliemountain.edifice;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.adapters.ComplaintTypeAdapter;
import com.lilliemountain.edifice.adapters.MaintenanceBillAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText societyName,yourName,emailID,complaintType,particularName,basePrice;
    RadioGroup btype;
    RadioButton quarterly,monthly;
    Button addParticular,addComplaintType,createDataObject;
    RecyclerView complainttypes,particulars;
    List<String> complaintTypeList=new ArrayList<>();
    List<Bill> particularList=new ArrayList<>();
    ComplaintTypeAdapter ctAdapter;
    MaintenanceBillAdapter mbAdapter;
    boolean isCreateDataObject=false;
    FirebaseDatabase database;
    DatabaseReference instance,billfor,committeeRoles,committee,complaintStatus,complainTypes,notice_board,noticeType,particularsREF,salutation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        database=FirebaseDatabase.getInstance();
        addParticular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                particularList.add(new Bill(Integer.parseInt(basePrice.getText().toString()),particularName.getText().toString()));
                mbAdapter=new MaintenanceBillAdapter(particularList);
                particulars.setAdapter(mbAdapter);
                particularName.setText("");
                basePrice.setText("");
            }
        });
        addComplaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintTypeList.add(complaintType.getText().toString());
                ctAdapter=new ComplaintTypeAdapter(complaintTypeList);
                particulars.setAdapter(ctAdapter);
                complaintType.setText("");
            }
        });
        createDataObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(societyName.getText()))
                {
                    societyName.setError("Enter Society Name");
                    isCreateDataObject=false;
                }else {
                    isCreateDataObject=true;
                }
                if(TextUtils.isEmpty(yourName.getText()))
                {
                    yourName.setError("Enter Your Name");
                    isCreateDataObject=false;
                }else {
                    isCreateDataObject=true;
                }
                if(TextUtils.isEmpty(emailID.getText())||!emailID.getText().toString().contains("@"))
                {
                    emailID.setError("Enter proper email id");
                    isCreateDataObject=false;
                }else {
                    isCreateDataObject=true;
                }
                if(complaintTypeList.size()<1)
                {
                    complaintType.setError("Enter Complaint types");
                    isCreateDataObject=false;
                }else {
                    isCreateDataObject=true;
                }
                if(particularList.size()<1)
                {
                    particularName.setError("Enter Maintenance particulars");
                    isCreateDataObject=false;
                }else {
                    isCreateDataObject=true;
                }
                if(isCreateDataObject)
                {
                    instance=database.getReference(societyName.getText().toString().toLowerCase().replace(" ","-"));
                    billfor=instance.child("billfor");
                    List<String>billFor=new ArrayList<>();
                    switch (btype.getCheckedRadioButtonId())
                    {
                        case R.id.quarterly:
                            billFor.add("APR-JUN");
                            billFor.add("JUL-SEP");
                            billFor.add("OCT-DEC");
                            billFor.add("JAN-MAR");
                            billfor.setValue(billFor);
                            break;
                            default:
                                billFor.add("JAN");
                                billFor.add("FEB");
                                billFor.add("MAR");
                                billFor.add("APR");
                                billFor.add("MAY");
                                billFor.add("JUN");
                                billFor.add("JUL");
                                billFor.add("AUG");
                                billFor.add("SEP");
                                billFor.add("OCT");
                                billFor.add("NOV");
                                billFor.add("DEC");
                                billfor.setValue(billFor);
                                break;
                    }
                    HashMap<String ,String> hasher=new HashMap<>();
                    hasher.put("roleName","Member");
                    committeeRoles.push().setValue(hasher);
                    hasher.remove("roleName");
                    hasher.put("roleName","Secretary");
                    committeeRoles.push().setValue(hasher);
                    hasher.remove("roleName");
                    hasher.put("roleName","Manager");
                    committeeRoles.push().setValue(hasher);
                    hasher.remove("roleName");
                    hasher.put("roleName","Manager");
                    committeeRoles.push().setValue(hasher);
                    complaintStatus.
//                    complaintStatus,complainTypes,notice_board,noticeType,particularsREF,salutation;

                }
            }
        });
    }

    private void initUI() {
        setContentView(R.layout.activity_sign_up);
        societyName=findViewById(R.id.societyName);
        yourName=findViewById(R.id.yourName);
        emailID=findViewById(R.id.emailID);
        complaintType=findViewById(R.id.complaintType);
        particularName=findViewById(R.id.particularName);
        basePrice=findViewById(R.id.basePrice);
        btype=findViewById(R.id.btype);
        monthly=findViewById(R.id.monthly);
        quarterly=findViewById(R.id.quarterly);
        addParticular=findViewById(R.id.addParticular);
        addComplaintType=findViewById(R.id.addComplaintType);
        createDataObject=findViewById(R.id.createDataObject);
        complainttypes=findViewById(R.id.complainttypes);
        particulars=findViewById(R.id.particulars);
        complainttypes.setLayoutManager(new LinearLayoutManager(this));
        particulars.setLayoutManager(new LinearLayoutManager(this));
    }
}
