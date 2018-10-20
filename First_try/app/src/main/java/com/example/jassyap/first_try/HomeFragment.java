package com.example.jassyap.first_try;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = NULL ;
    private RecyclerViewSurveyList adapter;
    private DatabaseReference myRef, questionRef;
    private ArrayList<String> surveytitle = new ArrayList<>();
    private ListView listView;
    private LinearLayout linlay;
    private CardView cv;
    private ArrayList<String> test_surveytitle = new ArrayList<>();
    //private ArrayAdapter<String> listViewAdapter;
    //private ArrayList<String> list;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        final CardView cv = v.findViewById(R.id.submit);


        final ListView listView = v.findViewById(R.id.rvSurveyTitle);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),R.layout.recyclerview_surveylist,R.id.survey_title,list);

        //Fetch title from each questionnaire data to populate the ListView
        myRef = FirebaseDatabase.getInstance().getReference();
        questionRef = myRef.child("questionnaire");

        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                        questionnaire survey = ds.getValue(questionnaire.class);
                        list.add(survey.getTitle().toString());
                    }
                    listView.setAdapter(listViewAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

                //we have currently three items

              if(position==0){
                    Toast.makeText(getActivity(),"Item One Clicked",Toast.LENGTH_SHORT).show();
                }
                if(position==1){
                    Toast.makeText(getActivity(),"Item Two Clicked",Toast.LENGTH_SHORT).show();
                }
                if(position==2) {
                    Toast.makeText(getActivity(), "Item Three Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

}
