package com.example.jassyap.first_try;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PremiumSurvey extends AppCompatActivity {

    final int questionNum = 10;
    private ArrayList<String> questions = new ArrayList<String>();
    private EditText questionEdit;
    private Button save_btn;
    private ListView listView;

    private DatabaseReference databaseReference, myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_survey);

        getSupportActionBar().setTitle("Premium Plan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questions);
        questionEdit = (EditText) findViewById(R.id.questionText);
        save_btn = (Button) findViewById(R.id.save);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Intent intent = getIntent();

        String key = intent.getStringExtra(PremiumPlan.QUESTIONNAIRE_ID);

        databaseReference = FirebaseDatabase.getInstance().getReference("question");
        myRef = databaseReference.child(key);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(save_btn.isPressed()) {
                    if (questions.size() < questionNum) {
                        questions.add(questionEdit.getText().toString());
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        myRef.setValue(questions);
                    }
                }
            }
        });
    }
}
