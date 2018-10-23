package com.example.jassyap.first_try;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SurveyApp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    //declare sign out button
    Button sign_out;
    //declare sign up button
    Button user_sign_up;
    //declare sign in button
    Button user_sign_in;
    //declare sign in with google button
    SignInButton signin;
    TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_app);
        //calling this method to page redirecting to Sign_up
        signing_up();
        ////calling this method to page redirecting to Sign_in
        signing_in();

        //All FOR sign in with google
        signin = (SignInButton) findViewById(R.id.sign_in_button);
        sign_out= (Button) findViewById(R.id.sign_out);
        user_sign_in = (Button) findViewById(R.id.sign_in);
        sign_out.setVisibility(View.GONE);
        tvname = (TextView) findViewById(R.id.name);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                int i = v.getId();
                if(i==R.id.sign_in_button){


                }
                signOut();
            }
        });


        findViewById(R.id.sign_in_button).setOnClickListener(SurveyApp.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(SurveyApp.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }

    // method for clicking button
    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i==R.id.sign_in_button){

            signIn();

        }


    }
    //method for sign in with google
    public void signIn(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }
    //method with sign up only
    //page redirect to activity_sign_up class associated with activity_sign_up.xml
    public void signing_up(){
        user_sign_up = (Button) findViewById(R.id.sign_up);
        user_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent set_up = new Intent(SurveyApp.this, Sign_up.class);
                startActivity(set_up);
            }
        });
    }

    //method with sign in only
    //page redirect to activity_sign_in class associated with activity_sign_ip.xml
    public void signing_in(){
        user_sign_in = (Button) findViewById(R.id.sign_in);
        user_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent set_up2 = new Intent(SurveyApp.this, Sign_in.class);
                startActivity(set_up2);
            }
        });
    }



    public void after_signUp(){
        //start with blablabla

    }

    public void after_signIn(){
        //start with blablabla

    }

    //method for authentication and page redirecting of sign in with google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){

                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Toast.makeText(SurveyApp.this,""+credential.getProvider(),Toast.LENGTH_LONG).show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        String name = getdata();

                        if (task.isSuccessful()){
                            //sign_out.setVisibility(View.VISIBLE);
                            signin.setVisibility(View.GONE);
                            //tvname.setText("Welcome "+name);
                            Intent i = new Intent(SurveyApp.this, Main_load.class);
                            startActivity(i);

                        }else {
                            Toast.makeText(SurveyApp.this,"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signOut() {
        // Firebase sign out
        mAuth.signOut();


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //  Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        //  startActivity(intent);
                        Toast.makeText(SurveyApp.this, "Logout", Toast.LENGTH_SHORT).show();

                    }
                });
        sign_out.setVisibility(View.GONE);
        tvname.setText(null);
        signin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public String getdata(){
        String name = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getEmail();
            //  String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

        return name;

    }

}
