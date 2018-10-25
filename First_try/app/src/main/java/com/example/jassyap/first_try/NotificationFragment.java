package com.example.jassyap.first_try;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private FirebaseDatabase database;
    private ArrayList<String> list;
    private ArrayAdapter<String> listViewAdapter;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        final ListView listView = view.findViewById(R.id.lv_noti_surveyTitle);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        list = new ArrayList<>();
        listViewAdapter = new ArrayAdapter<String>(getActivity(),R.layout.survey_title_view,R.id.noti_surveyTitle,list);

        //sort by created_date
        //not finished yet
        myRef.child("questionnaire").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear the list to refresh listview, restart from 0, then fill it with new datas
                list.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //only get questionnaire with open status

                    if (("open").matches(ds.child("status").getValue().toString())) {
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

        listView.setAdapter(listViewAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String selected = ((TextView) view.findViewById(R.id.noti_surveyTitle)).getText().toString();
                Toast.makeText(getActivity(), "You have clicked on " + selected, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), SurveyView.class);
                intent.putExtra("title", selected);
                startActivity(intent);

            }
        });

        return view;
    }

}
