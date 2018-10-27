package com.example.jassyap.first_try;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SurveyExport extends AppCompatActivity {

    private Button generate;
    private FirebaseFunctions mFunctions;
    private static final String TAG = "";
    private DatabaseReference myRef, questionnaireRef, questionRef;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private LineSeparator lineSeparator = new LineSeparator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_export);

        generate = this.findViewById(R.id.generate);

        myRef = FirebaseDatabase.getInstance().getReference();
        questionnaireRef = FirebaseDatabase.getInstance().getReference().child("questionnaire");
        questionRef = FirebaseDatabase.getInstance().getReference().child("question");

        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

        generate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    checkPermission();
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
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
                Toast.makeText(getApplicationContext(), "Done creating " + title + ".pdf", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkPermission() throws IOException, DocumentException {
        // Permission is not granted,
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED) {
        //so let's ask permisison
            ActivityCompat.requestPermissions(SurveyExport.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        //already got the permission
        else {
            printReport("1");
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
                    Toast.makeText(SurveyExport.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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

        //DataSnapshot dataSnapshot_q = dataSnapshot;
        //DataSnapshot dataSnapshot_r = dataSnapshot;

        for (DataSnapshot ds : dataSnapshot.child("question").child(qid).getChildren()) { //point to question node and fetch all questions
            //first print the question
            document.add(new Paragraph(ds.getValue().toString(),subFont));
            Log.d(TAG, "Question: " + ds.getValue().toString());

            //then print all responses for that specific question

            for (DataSnapshot ds_1 : dataSnapshot.child("response").child(qid).getChildren()) { //point to response node and fetch all responses for each questions
                for (DataSnapshot ds_1_1 : ds_1.getChildren()) { //fetch all r1, r2, r3, ..., rn
                    if (ds_1_1.getKey().matches(ds.getKey())) {
                        document.add(new Paragraph(ds_1_1.getValue().toString()));
                        Log.d(TAG, "Answer: " + ds_1_1.getValue().toString());
                    }
                }
            }

            // We add one empty line after each question and responses
            Paragraph new_line = new Paragraph();
            addEmptyLine(new_line, 1);
            document.add(new_line);

        }



    }

}
