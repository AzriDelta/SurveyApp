package com.example.jassyap.first_try;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class NewSurvey extends AppCompatActivity {
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey);

        listView = (ListView)findViewById(R.id.Listview);
        String[] values = new String[] {"Edit Survey", "Send to Collect Response", "Analyze your Result"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position==0){
                    Intent myIntent = new Intent(view.getContext(), EditSurvey.class);
                    startActivityForResult(myIntent,0);
                }
                if(position==1){
                    Intent myIntent = new Intent(view.getContext(), SendSurvey.class);
                    startActivityForResult(myIntent,1);
                }
                if(position==2){
                    Intent myIntent = new Intent(view.getContext(), AnalyzeSurvey.class);
                    startActivityForResult(myIntent,2);
                }
            }
        });

        getSupportActionBar().setTitle("Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
