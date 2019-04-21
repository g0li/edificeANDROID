package com.lilliemountain.edifice;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lilliemountain.edifice.POJO.Committee;
import com.lilliemountain.edifice.POJO.Notices;
import com.lilliemountain.edifice.POJO.Salutations;
import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.POJO.maintenance.Particulars;
import com.lilliemountain.edifice.adapters.ComplaintTypeAdapter;
import com.lilliemountain.edifice.adapters.MaintenanceBillAdapter;
import com.lilliemountain.edifice.adapters.MaintenanceBillAdapter2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText societyName,yourName,emailID,complaintType,particularName,basePrice;
    RadioGroup btype;
    RadioButton quarterly,monthly;
    Button addParticular,addComplaintType,createDataObject;
    RecyclerView complainttypes,particulars;
    List<String> complaintTypeList=new ArrayList<>();
    List<Particulars> particularList=new ArrayList<>();
    ComplaintTypeAdapter ctAdapter;
    MaintenanceBillAdapter2 mbAdapter;
    boolean isCreateDataObject=false;
    FirebaseDatabase database;
    DatabaseReference instance,billfor,committeeRoles,complaintStatus,complainTypes,notice_board,noticeType,particularsREF,salutation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        database=FirebaseDatabase.getInstance();
        addParticular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                particularList.add(new Particulars(particularName.getText().toString(),Long.parseLong(basePrice.getText().toString())));
//                particularList.add(new Particulars(Integer.parseInt(basePrice.getText().toString()),particularName.getText().toString()));
                mbAdapter=new MaintenanceBillAdapter2(particularList);
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
                setCreateDataObject();
            }
        });
    }

    private void setCreateDataObject(){
        final ProgressDialog dialog=new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Registering "+societyName.getText().toString()+"....");
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
            dialog.show();
            instance=database.getReference(societyName.getText().toString().toLowerCase().replace(" ","-"));

            committeeRoles=instance.child("committeeRoles");
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
            committeeRoles.push().setValue(hasher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    complaintStatus=instance.child("complaint-status");
                    HashMap<Integer ,String> hasher2=new HashMap<>();
                    hasher2.put(1,"Created");
                    hasher2.put(2,"Solved");
                    hasher2.put(3,"Not Solved");
                    complaintStatus.setValue(hasher2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            complainTypes=instance.child("complaint-types");
                            complainTypes.setValue(complaintTypeList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    notice_board=instance.child("notice-board");
                                    SimpleDateFormat dateFormatter=new SimpleDateFormat("dd-MM-yyyy");
                                    String date=dateFormatter.format(new Date());
                                    DateFormat format2=new SimpleDateFormat("EEEE");
                                    Date datex= null;
                                    try {
                                        datex = dateFormatter.parse(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    String day=format2.format(datex);
                                    List<Notices> noticesList=new ArrayList<>();
                                    noticesList.add(new Notices(date,day,"Congratulations! Your society is now a member of Lillie Mountains Family. Thank you for your purchase of Edifice.","Edifice Purchase","PURCHASE"));
                                    notice_board.setValue(noticesList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            noticeType=instance.child("notice-type");
                                            noticeType.setValue(complaintTypeList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    particularsREF=instance.child("particulars");
                                                    particularsREF.setValue(particularList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            DatabaseReference committe=instance.child("committee");
                                                            Committee c=new Committee("",emailID.getText().toString(),"",yourName.getText().toString(),"","Admin");
                                                            committe.push().setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {


                                                                    billfor=instance.child("billfor");
                                                                    List<String>billFor=new ArrayList<>();
                                                                    switch (btype.getCheckedRadioButtonId())
                                                                    {
                                                                        case R.id.quarterly:
                                                                            billFor.add("APR-JUN");
                                                                            billFor.add("JUL-SEP");
                                                                            billFor.add("OCT-DEC");
                                                                            billFor.add("JAN-MAR");
                                                                            billfor.setValue(billFor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    salutation=instance.child("salutations");
                                                                                    salutation.child("miss").setValue(new Salutations(1,"Miss")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            salutation.child("mr").setValue(new Salutations(0,"Mr.")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                    salutation.child("mrs").setValue(new Salutations(2,"Mrs")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            //TODO SOCIETY REGISTERD
                                                                                                            dialog.dismiss();
                                                                                                            Toast.makeText(SignUpActivity.this, "Soceity Registered", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
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
                                                                            billfor.setValue(billFor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    salutation=instance.child("salutations");
                                                                                    salutation.child("miss").setValue(new Salutations(1,"Miss")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            salutation.child("mr").setValue(new Salutations(0,"Mr.")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                    salutation.child("mrs").setValue(new Salutations(2,"Mrs")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            //TODO SOCIETY REGISTERD
                                                                                                            dialog.dismiss();
                                                                                                            Toast.makeText(SignUpActivity.this, "Soceity Registered", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                            break;
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
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
