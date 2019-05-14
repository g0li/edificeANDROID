package com.lilliemountain.edifice.create;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.list.ResidentListFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "key";

    // TODO: Rename and change types of parameters
    private String mParam1,mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param users
     * @return A new instance of fragment CreateUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateUserFragment newInstance(String param1, Users users, String key) {
        CreateUserFragment fragment = new CreateUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, key);
        args.putParcelable(ARG_PARAM1+"user", users);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(String param1) {
        CreateUserFragment fragment = new CreateUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            user = getArguments().getParcelable(ARG_PARAM1+"user");

        }
    }
    Spinner salutation,housetype;
    EditText name,flat,building,parkingslotno,carpetarea,vehiclemodel, email,mobileno,landline,password;
    RadioGroup parking,twfourwheeler;
    RadioButton yes,no,two,four;
    CheckBox gym,pool,club;
    String[] sal={"Mr.","Mrs.","Miss"};
    String[] ht={"Shop/ Gala","1RK","1BHK","2BHK","3BHK","Duplex",};
    Users user;
    ArrayAdapter<String> salAA,htype;
    DatabaseReference instance,users;
    ProgressBar progressBar3;
    List<Boolean> selectionFeature=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create_user, container, false);
        onButtonPressed(CreateUserFragment.class.getSimpleName());
        salutation=view.findViewById(R.id.salutation);
        housetype=view.findViewById(R.id.housetype);
        name=view.findViewById(R.id.name);
        flat=view.findViewById(R.id.flat);
        building=view.findViewById(R.id.building);
        carpetarea=view.findViewById(R.id.carpetarea);
        parkingslotno=view.findViewById(R.id.parkingslotno);
        vehiclemodel=view.findViewById(R.id.vehiclemodelno);
        email =view.findViewById(R.id.email);
        mobileno=view.findViewById(R.id.mobile);
        landline=view.findViewById(R.id.landline);
        twfourwheeler=view.findViewById(R.id.twofourwheeler);
        parking=view.findViewById(R.id.parking);
        password=view.findViewById(R.id.password);
        yes=view.findViewById(R.id.yes);
        no=view.findViewById(R.id.no);
        twfourwheeler=view.findViewById(R.id.twfourwheeler);
        two=view.findViewById(R.id.two);
        four=view.findViewById(R.id.four);
        gym=view.findViewById(R.id.gym);
        pool=view.findViewById(R.id.pool);
        club=view.findViewById(R.id.club);
        progressBar3=view.findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.GONE);
        salAA=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sal);
        htype=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ht);
        salutation.setAdapter(salAA);
        housetype.setAdapter(htype);
        switch (mParam1)
        {
            case "create":
                user=new Users();
                view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                if (!email.getText().toString().isEmpty()) {
                    if(!password.getText().toString().isEmpty()){
                        progressBar3.setVisibility(View.VISIBLE);
                        user.setName(name.getText().toString());
                        user.setFlat(flat.getText().toString());
                        user.setBuilding(building.getText().toString());

                        selectionFeature.add(gym.isChecked());
                        selectionFeature.add(pool.isChecked());
                        selectionFeature.add(club.isChecked());
                        if(gym.isChecked())
                        {
                            if(!extrafeatures.contains("Gym")){
                                extrafeatures=extrafeatures+"Gym,";
                            }
                        }
                        else {
                            if(extrafeatures.contains("Gym,")){
                                extrafeatures.replace("Gym,","");
                            }
                        }
                        if(pool.isChecked())
                        {
                            if(!extrafeatures.contains("Pool")){
                                extrafeatures=extrafeatures+"Pool,";
                            }
                        }
                        else {
                            if(extrafeatures.contains("Pool,")){
                                extrafeatures.replace("Pool,","");
                            }
                        }
                        if(club.isChecked())
                        {
                            if(!extrafeatures.contains("Club")){
                                extrafeatures=extrafeatures+"Club,";
                            }
                        }
                        else {
                            if(extrafeatures.contains("Club,")){
                                extrafeatures.replace("Club,","");
                            }
                        }
                        user.setExtrafeatures(extrafeatures);

                        if(parking.getCheckedRadioButtonId()==R.id.yes)
                            user.setParking("true");
                        else
                            user.setParking("false");

                        user.setSlotno(parkingslotno.getText().toString());
                        user.setVmodel(vehiclemodel.getText().toString());

                        if(twfourwheeler.getCheckedRadioButtonId()==R.id.two)
                            user.setTwofourwheel("2");
                        else
                            user.setTwofourwheel("4");

                        user.setEmailid(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setMobile(mobileno.getText().toString());
                        user.setLandline(landline.getText().toString());
                        user.setCarpetarea(Integer.valueOf(carpetarea.getText().toString()));
                        user.setHousetype(String.valueOf(housetype.getSelectedItem()));
                        user.setSelectionFeature(selectionFeature);
                        user.setSlotno(parkingslotno.getText().toString());
                        user.setSalutationid(salutation.getSelectedItemPosition());
                        /*User object created*/
                        /*************/
                        /************************************************/
                        /*************/
                        final FirebaseAuth auth= FirebaseAuth.getInstance();
                                auth.createUserWithEmailAndPassword(user.getEmailid(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                                        instance=database.getReference(getString(R.string.instance));
                                        users=instance.child("users");
                                        FirebaseUser firebaseUser = task.getResult().getUser();
                                        users.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar3.setVisibility(View.GONE);
                                                auth.signOut();
                                                auth.signInWithEmailAndPassword(getString(R.string.admin_email),getString(R.string.admin_password))
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                Toast.makeText(getContext(), user.getName()+" added.", Toast.LENGTH_SHORT).show();
                                                                openFragment(ResidentListFragment.newInstance("",""));
                                                            }

                                                        });
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                Snackbar.make(v,"Enter All Details Completely",Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                break;
            case "modify":
                name.setText(user.getName());
                flat.setText(user.getFlat());
                building.setText(user.getBuilding());
                extrafeatures=user.getExtrafeatures();
                parkingslotno.setText(user.getSlotno());
                salutation.setSelection(user.getSalutationid());

                pool.setChecked(extrafeatures.contains("Pool"));
                gym.setChecked(extrafeatures.contains("Gym"));
                club.setChecked(extrafeatures.contains("Club"));
                yes.setChecked(user.getParking().contains("true"));
                no.setChecked(user.getParking().contains("false"));
                parkingslotno.setText(user.getSlotno());
                vehiclemodel.setText(user.getVmodel());
                two.setChecked(user.getTwofourwheel().contains("2"));
                four.setChecked(user.getTwofourwheel().contains("4"));
                email.setText(user.getEmailid());
                mobileno.setText(user.getMobile());
                landline.setText(user.getLandline());
                password.setText(user.getPassword());
                carpetarea.setText(user.getCarpetarea()+"");
                housetype.setSelection(htype.getPosition(user.getHousetype()));
                user=new Users();
                view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.setName(name.getText().toString());
                        user.setFlat(flat.getText().toString());
                        user.setBuilding(building.getText().toString());
                        selectionFeature.clear();
                        selectionFeature.add(gym.isClickable());
                        selectionFeature.add(pool.isClickable());
                        selectionFeature.add(club.isClickable());
                        if(gym.isChecked())
                        {
                            if(!extrafeatures.contains("Gym")){
                                extrafeatures=extrafeatures+"Gym,";
                            }
                        }
                        else {
                            if(extrafeatures.contains("Gym,")){
                                extrafeatures.replace("Gym,","");
                            }
                        }
                        if(pool.isChecked())
                        {
                            if(!extrafeatures.contains("Pool")){
                                extrafeatures=extrafeatures+"Pool,";
                            }
                        }
                        else {
                            if(extrafeatures.contains("Pool,")){
                                extrafeatures.replace("Pool,","");
                            }
                        }
                        if(club.isChecked())
                        {
                            if(!extrafeatures.contains("Club")){
                                extrafeatures=extrafeatures+"Club,";
                            }
                        }
                        else {
                            if(extrafeatures.contains("Club,")){
                                extrafeatures.replace("Club,","");
                            }
                        }
                        user.setExtrafeatures(extrafeatures);

                        if(parking.getCheckedRadioButtonId()==R.id.yes)
                            user.setParking("true");
                        else
                            user.setParking("false");

                        user.setSlotno(parkingslotno.getText().toString());
                        user.setVmodel(vehiclemodel.getText().toString());

                        if(twfourwheeler.getCheckedRadioButtonId()==R.id.two)
                            user.setTwofourwheel("2");
                        else
                            user.setTwofourwheel("4");

                        user.setEmailid(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setMobile(mobileno.getText().toString());
                        user.setLandline(landline.getText().toString());
                        user.setSelectionFeature(selectionFeature);
                        user.setSlotno(parkingslotno.getText().toString());
                        user.setSalutationid(salutation.getSelectedItemPosition());
                        /*User object created*/
                        /*************/
                        /************************************************/
                        /*************/
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        instance=database.getReference(getString(R.string.instance));
                        users=instance.child("users");
                        users.child(mParam2).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), user.getName()+" updated.", Toast.LENGTH_SHORT).show();
                                openFragment(ResidentListFragment.newInstance("",""));
                            }
                        });

                    }
                });
                break;

        }
        return view;
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    String extrafeatures="";
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
