package com.lilliemountain.edifice.create;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Notices;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.list.NoticeListFragment;
import com.lilliemountain.edifice.list.ResidentListFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNoticeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNoticeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateNoticeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CreateNoticeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNoticeFragment newInstance(String param1,Notices notices,String pos) {
        CreateNoticeFragment fragment = new CreateNoticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, notices);
        args.putString(ARG_PARAM2+"pos", pos);
        fragment.setArguments(args);
        return fragment;
    }
    String position;
   public static CreateNoticeFragment newInstance(String param1) {
        CreateNoticeFragment fragment = new CreateNoticeFragment();
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
            notices = getArguments().getParcelable(ARG_PARAM2);
            position = getArguments().getString(ARG_PARAM2+"pos");
        }
    }
    EditText title,desc;
    Button daydate,submit;
    Spinner type;
    ArrayAdapter<String> noticeAA;
    List<String> noticetype=new ArrayList<>();
    ProgressBar progressBar;
    Notices notices;
    String date="";
    String day="";
    List<Notices> noticesArray=new ArrayList<>();
    DatabaseReference instance,notice_board;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_create_notice, container, false);
        title=view.findViewById(R.id.title);
        desc=view.findViewById(R.id.desc);
        daydate=view.findViewById(R.id.daydate);
        submit=view.findViewById(R.id.submit);
        type=view.findViewById(R.id.type);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        daydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }
        });
        noticetype.add("NOTICE");
        noticetype.add("CELEBRATION");
        noticetype.add("MAINTENANCE");
        noticetype.add("AGM");
        noticetype.add("MEETING");
        noticetype.add("COMPLAINT");
        noticeAA=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,noticetype);
        type.setAdapter(noticeAA);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        instance=database.getReference(getString(R.string.instance));
        notice_board=instance.child("notice-board");

        switch (mParam1)
        {
            case "create":
                notices=new Notices();

                notice_board.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<Notices>> t = new GenericTypeIndicator<List<Notices>>() {};
                        noticesArray = dataSnapshot.getValue(t);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(date.contains("-")&& day.length()>0)
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            notices.setDate(date);
                            notices.setDay(day);
                            notices.setNoticeTitle(title.getText().toString());
                            notices.setNoticeText(desc.getText().toString());
                            notices.setNoticeType(noticetype.get(type.getSelectedItemPosition()));
                            noticesArray.add(notices);
                            notice_board.setValue(noticesArray).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), notices.getNoticeTitle().substring(0,10)+"... added.", Toast.LENGTH_SHORT).show();
                                    openFragment(ResidentListFragment.newInstance("",""));
                                }
                            });
                        }
                        else{
                            Snackbar.make(v,"Please Select Date",Snackbar.LENGTH_LONG).show();
                        }

                    }
                });
                break;
            case "modify":
                title.setText(notices.getNoticeTitle());
                title.setEnabled(true);
                desc.setEnabled(true);
                desc.setText(notices.getNoticeText());
                daydate.setText(notices.getDate());
                date=notices.getDate();
                day=notices.getDay();
                type.setSelection(noticetype.indexOf(notices.getNoticeType()));

                notices=new Notices();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                DatabaseReference modifiyPos=notice_board.child(position);
                        if(date.contains("-")&& day.length()>0)
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            notices.setDate(date);
                            notices.setDay(day);
                            notices.setNoticeTitle(title.getText().toString());
                            notices.setNoticeText(desc.getText().toString());
                            notices.setNoticeType(noticetype.get(type.getSelectedItemPosition()));
                            modifiyPos.setValue(notices).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), notices.getNoticeTitle().substring(0,10)+"... modified.", Toast.LENGTH_SHORT).show();
                                    openFragment(NoticeListFragment.newInstance("",""));
                                }
                            });
                        }
                        else{
                            Snackbar.make(v,"Please Select Date",Snackbar.LENGTH_LONG).show();
                        }

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

    private void setDateTimeField() {
        final Calendar dateSelected = Calendar.getInstance();
        Calendar newCalendar = dateSelected;
        final SimpleDateFormat dateFormatter=new SimpleDateFormat("dd-MM-yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                date=dateFormatter.format(dateSelected.getTime());
                try {
                    Date datex=dateFormatter.parse(date);
                    daydate.setText(dateFormatter.format(dateSelected.getTime()));
                    DateFormat format2=new SimpleDateFormat("EEEE");
                    day=format2.format(datex);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
