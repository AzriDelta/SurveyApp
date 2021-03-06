package com.example.jassyap.first_try;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GenerateReportFragment extends Fragment {

;
    private static final String TAG = "";
    private DatabaseReference myRef, questionnaireRef, questionRef;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private LineSeparator lineSeparator = new LineSeparator();
    private int number_survey_open = 0;
    private String qid;
    private ArrayMap<String, String> arrayMap = new ArrayMap<>();

    public GenerateReportFragment () {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generatereport, container, false);

        myRef = FirebaseDatabase.getInstance().getReference();
        questionnaireRef = FirebaseDatabase.getInstance().getReference().child("questionnaire");
        questionRef = FirebaseDatabase.getInstance().getReference().child("question");

        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

        final ListView listView = view.findViewById(R.id.lvSurveyTitleGenerate);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(),R.layout.generateresponse_info, R.id.generateresponse_surveyTitle,list);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                users userProfile = dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(users.class);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String username = userProfile.getName();

                //display list of survey that can be downloaded it's responses, which satisfies two conditions:
                //1. questionnaires that are created only by the creator
                //2. questionnaires that have responses only - must exist in 'response' node

                list.clear();

                //how to do:

                for (DataSnapshot ds_q : dataSnapshot.child("questionnaire").getChildren()) {


                    //1. check if the current user has surveys created under the username

                    if (username.matches(ds_q.child("creator_name").getValue().toString())){

                        //2. check if the surveys has response - compare qid from both questionnaire and response

                        for (DataSnapshot ds_r : dataSnapshot.child("response").getChildren()) {

                            if(ds_q.getKey().matches(ds_r.getKey())) {


                                questionnaire survey = ds_q.getValue(questionnaire.class);
                                list.add(survey.getTitle());

                                arrayMap.put(ds_q.child("title").getValue().toString(), ds_q.getKey());

                                Log.d(TAG, "QID: " + ds_q.getKey());
                                Log.d(TAG, "Title: " + ds_q.child("title").getValue().toString());


                            }


                        }


                    }

                }

                //clear the list to refresh listview, restart from 0, then fill it with new datas


                //listViewAdapter.clear();
                listViewAdapter.notifyDataSetChanged();
                listView.setAdapter(listViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

                //get the title of questionnaire
                String selected = ((TextView) view.findViewById(R.id.generateresponse_surveyTitle)).getText().toString();
                String qid_i = arrayMap.get(selected);
                Toast.makeText(getActivity(), "Generating " + selected + ".pdf", Toast.LENGTH_SHORT).show();
                checkPermission(qid_i);


            }
        });

        /*listView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    checkPermission();
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        }); */

        return view;
    }

    private void printReport(final String qid){


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //creating the document
                final Document document = new Document();

                Log.d(TAG, "QID: " + dataSnapshot.child("questionnaire").child(qid).getKey());
                String title = dataSnapshot.child("questionnaire").child(qid).child("title").getValue().toString();

                //1. set folder path to save the pdf and giving the filename based on the title of survey
                try {
                    setFolderPath(document, title);
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }

                //2. Open to write
                document.open();

                //3. add medadata - title, etc/
                addMetaData(document,  title);

                //4. add title of the document at the start of the page, using title of the survey
                try {
                    addTitlePage(document, title);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                //5. add content of the survey
                try {
                    addContent(document, dataSnapshot, qid);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }


                // finish the page
                document.close();
                Toast.makeText(getActivity(), "Done creating " + title + ".pdf", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkPermission(String qid) {
        // Permission is not granted,
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED) {
        //so let's ask permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        //already got the permission
        else {
            printReport(qid);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    //set folder path to save the pdf
    private void setFolderPath(final Document document, String title) throws FileNotFoundException, DocumentException {

        String dest = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        String targetPdf = dest + (title + ".pdf");
        PdfWriter.getInstance(document, new FileOutputStream(targetPdf));

    }

    private void addMetaData(final Document document, String title) {

        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
        document.addTitle(title);

        /*questionnaireRef.child(qid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                document.addTitle(dataSnapshot.child("title").getValue().toString());
                Log.d(TAG, "Title: " + dataSnapshot.child("title").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

    }

    private void addTitlePage(final Document document, String title) throws DocumentException {

        final Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 2);
        // Lets write a big header
        preface.add(new Paragraph(title, catFont));
        document.add(preface);
        //add a new line, a line separator and a new line
        document.add(new Paragraph(""));
        document.add(new Chunk(lineSeparator));
        document.add(new Paragraph(""));

        /*questionnaireRef.child(qid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                preface.add(new Paragraph(dataSnapshot.child("title").getValue().toString(), catFont));
                try {
                    document.add(preface);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                //Log.d(TAG, "Title: " + dataSnapshot.child("title").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */
    }

    private void addContent(Document document, DataSnapshot dataSnapshot, String qid) throws DocumentException {


        for (DataSnapshot ds : dataSnapshot.child("question").child(qid).getChildren()) { //point to question node and fetch all questions

            //1. first print a question from question node
            String question = ds.getValue().toString();
            document.add(new Paragraph(ds.getValue().toString(), subFont));
            Log.d(TAG, "Question: " + ds.getValue().toString());

            //2. then print all responses for that specific question

            for (DataSnapshot ds_r : dataSnapshot.child("response").child(qid).getChildren()) { //fetch all r1, r2, ..., rn

                for (DataSnapshot ds_r_a : ds_r.getChildren()) { //fetch all answers

                    String question_to_be_matched = ds_r_a.getKey();

                    if (question.equals(question_to_be_matched)) { //the key (question) matches with the question from the step 1., we want to print all answers for a question first
                        document.add(new Paragraph(ds_r_a.getValue().toString()));

                    }
                }
            }

            //4. We add one empty line after each question and responses
            Paragraph new_line = new Paragraph();
            addEmptyLine(new_line, 1);
            document.add(new_line);

        }

            /*for (DataSnapshot ds_1 : dataSnapshot.child("response").child(qid).getChildren()) { //point to response node and fetch all responses for that questionnaire
                for (DataSnapshot ds_1_1 : ds_1.getChildren()) { //fetch all r1, r2, r3, ..., rn
                    if (ds_1_1.getKey().matches(ds.getValue().toString())) {
                        document.add(new Paragraph(ds_1_1.getValue().toString()));
                        Log.d(TAG, "Answer: " + ds_1_1.getValue().toString());
                    }
               }
                //3. repeat back to step 2 until all responses have been entered
            } */

            //4. We add one empty line after each question and responses
            /*Paragraph new_line = new Paragraph();
            addEmptyLine(new_line, 1);
            document.add(new_line); */

            //5. Repeat step 1 until all questions and all responses for each question have been entered
        //}



    }

}
