package com.example.jassyap.first_try;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FreePlan extends AppCompatActivity {
    private int questionID = 0;
    private EditText editText, editText1, editText2, editText3;
    private Button button;
    final int maxParticipant = 10;
    int participantNum;
    String creatorName;
    public static final String QUESTIONNAIRE_ID = "questionnaireId";

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_plan);

        button = (Button)findViewById(R.id.button);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        myRef = firebaseDatabase.getReference("users").child(firebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users User = dataSnapshot.getValue(users.class);
                creatorName = User.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = firebaseDatabase.getReference("questionnaire");

        editText = (EditText)findViewById(R.id.title);
        editText1 = (EditText)findViewById(R.id.minAge);
        editText2 = (EditText)findViewById(R.id.maxAge);
        editText3 = (EditText)findViewById(R.id.faculty);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText.getText().toString();

                String minAge = editText1.getText().toString();

                String maxAge = editText2.getText().toString();

                String faculty = editText3 .getText().toString();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String strDate = dateFormat.format(date).toString();

                String status = "Close";
                for(participantNum=0; participantNum<maxParticipant; participantNum++){
                    status = "Open";
                }

                String plan;
                if(maxParticipant <= 10){
                    plan = "Free";
                }

                String key = databaseReference.push().getKey();

                questionnaire qNaire = new questionnaire(key, creatorName, title, strDate, status, plan, minAge, maxAge, faculty);

                databaseReference.child(key).setValue(qNaire);

                Intent intent = new Intent(FreePlan.this, FreeSurvey.class);
                intent.putExtra(QUESTIONNAIRE_ID, key);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Free Plan Description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
