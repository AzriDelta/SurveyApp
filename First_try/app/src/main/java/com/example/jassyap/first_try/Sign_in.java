package com.example.jassyap.first_try;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_in extends AppCompatActivity {
    private EditText editText2, editText4;
    private FirebaseAuth firebaseAuth;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText4 = (EditText)findViewById(R.id.editText4);
        firebaseAuth = FirebaseAuth.getInstance();
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();

            }
        });
    }

    public void userLogin(){

        (firebaseAuth.signInWithEmailAndPassword(editText2.getText().toString(), editText4.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Sign_in.this, "Login successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Sign_in.this, Sign_in.class);
                    startActivity(i);
                }else{
                    Log.e("Error", task.getException().toString());
                    Toast.makeText(Sign_in.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });




    }
}
