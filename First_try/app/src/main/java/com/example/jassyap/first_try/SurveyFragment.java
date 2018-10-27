package com.example.jassyap.first_try;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyFragment extends Fragment {

    public SurveyFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_survey, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.lview);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),R.layout.survey_info,R.id.noti_surveyTitle,list);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users userProfile = dataSnapshot.getValue(users.class);
                final String username = userProfile.getName().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questionnaire");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        list.clear();

                        for (DataSnapshot ds: dataSnapshot.getChildren()){

                            if (username.matches(ds.child("creator_name").getValue().toString())) {
                                questionnaire survey = ds.getValue(questionnaire.class);
                                list.add(survey.getTitle());
                            }
                        }
                        listView.setAdapter(listViewAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //we have currently three items
                if(position==0){
                    Toast.makeText(getActivity(),"Item One Clicked",Toast.LENGTH_SHORT).show();
                }
                if(position==1){
                    Toast.makeText(getActivity(),"Item Two Clicked",Toast.LENGTH_SHORT).show();
                }
                if(position==2){
                    Toast.makeText(getActivity(),"Item Three Clicked",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
