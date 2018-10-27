package com.example.jassyap.first_try;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class SurveyView extends AppCompatActivity {

    private static final String TAG = NULL ;
    private TextView surveyTitle;
    private String title, qid;
    private LinearLayout qnalayout;
    private View view;
    private Button submit;
    private int number_of_questions = 0;
    private int number_of_response = 0;
    private DatabaseReference myRef, questionnaireRef, questionRef, responseRef;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_view);
        qnalayout = findViewById(R.id.qna_layout);
        surveyTitle = findViewById(R.id.generate_surveyTitle);
        submit = findViewById(R.id.submit);

        this.title = getIntent().getStringExtra("title");
        Log.d(TAG, "Title obtained is: " + this.title);

        //database reference pointing to root of database
        myRef = FirebaseDatabase.getInstance().getReference();
        //point to questionnaire node
        questionnaireRef = myRef.child("questionnaire");

        //set the survey of title
        surveyTitle.setText(title);

        //find the Q_ID for question with the same title inside questionnaire node
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                //fetch qid from questionnaire node
                for (DataSnapshot ds : dataSnapshot.child("questionnaire").getChildren()) {
                    if (title.matches(ds.child("title").getValue(String.class))) {
                        Log.d(TAG, "Title is: " + ds.child("title").getValue());
                        Log.d(TAG, "Key is: " + ds.getKey());
                        qid = ds.getKey();
                    }
                }

                //fetch questions from question node, using qid from questionnaire node
                for (DataSnapshot ds : dataSnapshot.child("question/" + qid).getChildren()) {
                    //to check if we manage to fetch values
                    Log.d(TAG, "Key is: " + ds.getKey());
                    Log.d(TAG, "Value is: " + ds.getValue());

                    //check for regex, just pick questions only, other infos (i.e. creator, title) are not loaded into
                    //if (ds.getKey().matches("q[0-9]*-[0-9]*")) {

                    //generate new question and answer layouts for each QnA
                    //fetch survey's data to be put into

                    view = LayoutInflater.from(qnalayout.getContext()).inflate(R.layout.qna_view, null);
                    Log.d(TAG, "ID for qna_view layout: " + view.getId());
                    qnalayout.addView(view);


                    TextView question = view.findViewById(R.id.question);
                    TextView answer = view.findViewById(R.id.answer);

                    //generate new ID for question and answer textbar
                    question.setId(question.getId() + (number_of_questions + 1));
                    answer.setId(answer.getId() + (number_of_questions + 1));

                    //load the question from firebase into question Textbar
                    question.setText(ds.getValue(String.class));

                    Log.d(TAG, "ID for question text bar: " + question.getId());

                    number_of_questions++;

                }

                for (DataSnapshot ds : dataSnapshot.child("response/" + qid).getChildren()) {
                    number_of_response++;
                }

                Log.d(TAG, "Number of responses: " + number_of_response);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //point to 'responnse->qid' node under root node
        //responseRef = myRef.child("response").child(qid);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Number of questions: " + number_of_questions);

                //access each qna_view.xml that has been inflated, one by one
                for (int i=1; i<=number_of_questions; i++) {

                    //= view.findViewById(R.id.question);
                    //= view.findViewById(R.id.answer);

                    //LayoutInflater li=getLayoutInflater();
                    //vw=li.inflate(R.layout.frag, null);
                    //btn = (Button)vw.findViewById(R.id.btn);

                    //LinearLayout linlay = findViewById(R.id.qna_layout);
                    //setContentView(R.layout.qna_view);

                    TextView question = qnalayout.findViewById((R.id.question) + i);
                    EditText answer = qnalayout.findViewById((R.id.answer) + i);

                    //ensure that we got the correct question
                    Log.d(TAG, "Question: " + question.getText().toString());
                    Log.d(TAG, "Answer: " + answer.getText().toString());

                    myRef.child("response").child(qid).child("r"+(number_of_response+1)).child(question.getText().toString()).setValue(answer.getText().toString());
                }

                //thank you for answering the survey
                Toast.makeText(getBaseContext(), "Thank you for answering the survey", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), Main_load.class);
                startActivity(intent);
            }
        });


    }
}
