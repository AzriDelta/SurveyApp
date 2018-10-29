package com.example.jassyap.first_try;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = NULL ;
    private DatabaseReference myRef, questionnaireRef;
    private NotificationManager mNotificationManager;
    private FirebaseDatabase database;
    private int number_survey_open = 0;
    private int number_survey_open_ref;
    private int firsttime = 0;


    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        final ListView listView = v.findViewById(R.id.rvSurveyTitle);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),R.layout.listview_surveylist,R.id.survey_title,list);

        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        questionnaireRef = myRef.child("questionnaire");

        mNotificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);

        Button button = (Button)v.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Create New Survey");
                alert.setMessage("Do you want to create a new survey?");
                alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), PlanSelection.class);
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });
        //commented - because no need to show the listview for open survey anymore
        //number_survey_open = 0;
        /*questionnaireRef.orderByChild("created_date").addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear the list to refresh listview, restart from 0, then fill it with new datas
                list.clear();
                number_survey_open = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //only get questionnaire with open status

                    if (("open").matches(ds.child("status").getValue().toString())) {
                        questionnaire survey = ds.getValue(questionnaire.class);
                        list.add(survey.getTitle());
                        number_survey_open++;
                    }
                }

                //listViewAdapter.clear();
                listViewAdapter.notifyDataSetChanged();
                listView.setAdapter(listViewAdapter);





                //notification bar pop up for new questionnaires
                //DatabaseReference myRef = database.getReference();
                if ((number_survey_open > number_survey_open_ref) & (firsttime != 0)) {
                myRef.child("questionnaire").addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        listViewAdapter.notifyDataSetChanged();
                        Notification notification = new NotificationCompat.Builder(getActivity())
                                .setContentTitle("New Survey")   //Set the title of Notification
                                .setContentText("You got a new survey to answer!.")    //Set the text for notification
                                .setSmallIcon(android.R.drawable.ic_menu_view)   //Set the icon
                                .build();

                        //Send the notification.
                        mNotificationManager.notify(1, notification);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                }); }

                else {
                    number_survey_open_ref = number_survey_open;
                    firsttime++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            });

        listView.setAdapter(listViewAdapter);

        Log.d(TAG, "Number of open survey: " + number_survey_open);
        Log.d(TAG, "Number of open survey referenced: " + number_survey_open_ref);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

                //get the title of questionnaire
                String selected = ((TextView) view.findViewById(R.id.survey_title)).getText().toString();
                Toast.makeText(getActivity(), "Opening " + selected, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), SurveyView.class);
                intent.putExtra("title", selected);
                startActivity(intent);

            }
        }); */

        return v;
    }
    }
