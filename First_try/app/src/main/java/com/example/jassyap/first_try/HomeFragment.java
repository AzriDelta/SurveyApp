package com.example.jassyap.first_try;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = NULL ;
    private RecyclerViewSurveyList adapter;
    private DatabaseReference myRef, questionnaireRef;
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
        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),R.layout.listview_surveylist,R.id.survey_title,list);

        //Fetch title from each questionnaire data to populate the ListView
        myRef = FirebaseDatabase.getInstance().getReference();
        questionnaireRef = myRef.child("questionnaire");

        questionnaireRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (("open").matches(ds.child("status").getValue().toString())) {
                            questionnaire survey = ds.getValue(questionnaire.class);
                            list.add(survey.getTitle().toString());
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
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

                //get the title of questionnaire
                String selected = ((TextView) view.findViewById(R.id.survey_title)).getText().toString();
                Toast.makeText(getActivity(), "Opening " + selected, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SurveyView.class);
                intent.putExtra("title", selected);
                startActivity(intent);
                //finish();

            }
        });

        return v;
    }

}
