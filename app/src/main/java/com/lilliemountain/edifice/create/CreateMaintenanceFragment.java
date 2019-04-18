package com.lilliemountain.edifice.create;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.POJO.maintenance.Header;
import com.lilliemountain.edifice.POJO.maintenance.Maintenance;
import com.lilliemountain.edifice.POJO.maintenance.Particulars;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.adapters.MaintenanceBillAdapter;
import com.lilliemountain.edifice.list.ResidentListFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateMaintenanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateMaintenanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateMaintenanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateMaintenanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateMaintenanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateMaintenanceFragment newInstance(String param1, String param2) {
        CreateMaintenanceFragment fragment = new CreateMaintenanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView payable,interestfine,pending,subtotal,totalbill,building,carpetarea,flat,housetype,pamount;
    RecyclerView recyclerView;
    Button date;
    String dateStr="";
    Spinner billfor,resident,particulars;
    List<Users> namesOfResidentsUsers=new ArrayList<>();
    List<String> namesOfResidents=new ArrayList<>();
    List<String> namesOfResidentsKey=new ArrayList<>();
    List<String> billForList=new ArrayList<>();
    List<Bill> billList=new ArrayList<>();
    List<Particulars> particularsList=new ArrayList<>();
    List<String> particularsListStr=new ArrayList<>();
    ArrayAdapter<String> namesAA,billsAA,particularAA;
    DatabaseReference instance,maintenance,users,particularz,bill4;
    Integer carpetareax=0;
    long subTotal=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_create_maintenance, container, false);
        onButtonPressed(CreateMaintenanceFragment.class.getSimpleName());
        payable=view.findViewById(R.id.payable);
        pamount=view.findViewById(R.id.pamount);
        interestfine=view.findViewById(R.id.interestfine);
        pending=view.findViewById(R.id.pending);
        subtotal=view.findViewById(R.id.subtotal);
        totalbill=view.findViewById(R.id.totalbill);
        building=view.findViewById(R.id.building);
        carpetarea=view.findViewById(R.id.carpetarea);
        flat=view.findViewById(R.id.flat);
        housetype=view.findViewById(R.id.housetype);
        date=view.findViewById(R.id.billDate);
        billfor=view.findViewById(R.id.billforSpinner);
        resident=view.findViewById(R.id.residentSpinner);
        particulars=view.findViewById(R.id.particularSpinner);
        recyclerView=view.findViewById(R.id.particularRec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        instance=database.getReference(getString(R.string.instance));
        maintenance=instance.child("maintenance");
        users=instance.child("users");
        particularz=instance.child("particulars");
        bill4=instance.child("billsfor");


        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dateStr=mDay + "-" + (mMonth + 1) + "-" + mYear;
        date.setText("Bill date : "+dateStr);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker();
            }
        });
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    Users u=child.getValue(Users.class);
                    namesOfResidentsUsers.add(u);
                    namesOfResidents.add(u.getName());
                    namesOfResidentsKey.add(child.getKey());
                }
                namesAA=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,namesOfResidents);
                resident.setAdapter(namesAA);
                resident.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        flat.setText("Flat:\n"+namesOfResidentsUsers.get(position).getFlat());
                        housetype.setText("House Type:\n"+namesOfResidentsUsers.get(position).getHousetype());
                        carpetarea.setText("Carpet Area:\n"+namesOfResidentsUsers.get(position).getCarpetarea()+"sq.ft.");
                        carpetareax=namesOfResidentsUsers.get(position).getCarpetarea();
                        building.setText("Building:\n"+namesOfResidentsUsers.get(position).getBuilding());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                particularz.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<Particulars>> t = new GenericTypeIndicator<List<Particulars>>() {};
                        particularsList = dataSnapshot.getValue(t);
                        for (Particulars particular:
                             particularsList) {
                            particularsListStr.add(particular.getName());
                        }
                        particularAA=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,particularsListStr);
                        particulars.setAdapter(particularAA);
                        particulars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int Amount= Integer.parseInt(String.valueOf(particularsList.get(position).getBaseprice()*carpetareax));
                                pamount.setText(Amount+"");
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        bill4.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                                billForList=dataSnapshot.getValue(t);
                                billsAA=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,billForList);
                                billfor.setAdapter(billsAA);
                                view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mbalist.add(new Bill(Integer.parseInt(String.valueOf(particularsList.get(particulars.getSelectedItemPosition()).getBaseprice()*carpetareax)),
                                                particularsList.get(particulars.getSelectedItemPosition()).getName()));
                                        MaintenanceBillAdapter maintenanceBillAdapter=new MaintenanceBillAdapter(mbalist);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setAdapter(maintenanceBillAdapter);
                                        subTotal=subTotal+particularsList.get(particulars.getSelectedItemPosition()).getBaseprice()*carpetareax;
                                        subtotal.setText(subTotal+"");
                                        totalbill.setText(subTotal+"");
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e( "Error at line 155: ",databaseError.toString() );
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e( "Error at line 162: ",databaseError.toString() );

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e( "Error at line 170: ",databaseError.toString() );
            }
        });
        final List<Maintenance> listyu=new ArrayList<>();
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Header header=new Header();
                Maintenance _maintenance=new Maintenance();
                Users users=namesOfResidentsUsers.get(resident.getSelectedItemPosition());

                header=new Header(billForList.get(billfor.getSelectedItemPosition()), users.getBuilding(),users.getCarpetarea(),dateStr,users.getFlat(),users.getFlat(),users.getName());
                _maintenance=new Maintenance(mbalist,header,header.getBillfor()+dateStr.split("-")[2],0,0,"Created",subTotal,subTotal);
                maintenance.child(namesOfResidentsKey.get(resident.getSelectedItemPosition())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<List<Maintenance>> t = new GenericTypeIndicator<List<Maintenance>>() {};
                        try {
                            listyu.addAll(dataSnapshot.getValue(t));
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                listyu.add(_maintenance);

                maintenance.child(namesOfResidentsKey.get(resident.getSelectedItemPosition())).setValue(listyu).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Maintenance created",Toast.LENGTH_LONG).show();
                        openFragment(CreateMaintenanceFragment.newInstance("",""));

                    }
                });
            }
        });
        return view;
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    List<Bill> mbalist=new ArrayList<>();
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    void getDatePicker()
    {

        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateStr=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        date.setText("Bill date : "+dateStr);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }
}
