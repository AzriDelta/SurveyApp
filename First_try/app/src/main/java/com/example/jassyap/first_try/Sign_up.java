package com.example.jassyap.first_try;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity{

    private EditText editText2, editText4, editText5, editText6, editText7;
    private Button button;
    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        editText2 = (EditText)findViewById(R.id.editText2);
        editText4 = (EditText)findViewById(R.id.editText4);
        editText5 = (EditText)findViewById(R.id.editText5);
        editText6 = (EditText)findViewById(R.id.editText6);
        editText7 = (EditText)findViewById(R.id.editText7);
        firebaseAuth = FirebaseAuth.getInstance();
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSignUpInfo();

            }
        });


    }
    public void addSignUpInfo(){

        (firebaseAuth.createUserWithEmailAndPassword(editText6.getText().toString(), editText5.getText().toString())).addOnCompleteListener(Sign_up.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Sign_up.this, "Registration successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Sign_up.this, Sign_in.class);
                    startActivity(i);
                }else{
                    Log.e("Error", task.getException().toString());
                    Toast.makeText(Sign_up.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        String id = firebaseAuth.getCurrentUser().getUid();
        String name = editText2.getText().toString();
        String age = editText4.getText().toString();
        String password = editText5.getText().toString();
        String gmail = editText6.getText().toString();
        String faculty = editText7.getText().toString();

        if (!TextUtils.isEmpty(name) && (!TextUtils.isEmpty(gmail) && (!TextUtils.isEmpty(password) && (!TextUtils.isEmpty(age)) && (!TextUtils.isEmpty(faculty))))){

            users sign_up_info= new users(name, gmail, password,age,faculty);
            databaseReference.child(id).setValue(sign_up_info);
            editText2.setText("");
            editText4.setText("");
            editText5.setText("");
            editText6.setText("");
            editText7.setText("");



        }else{
            Toast.makeText(Sign_up.this, "Please type name, email, password", Toast.LENGTH_LONG).show();
        }
    }
}
