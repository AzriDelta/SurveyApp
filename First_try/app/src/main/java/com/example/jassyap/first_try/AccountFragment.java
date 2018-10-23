package com.example.jassyap.first_try;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        final TextView profilename = view.findViewById(R.id.tvusername);
        final TextView profileemail = view.findViewById(R.id.tvemail);
        final TextView profilepassword = view.findViewById(R.id.tvpassword);
        final TextView profileage = view.findViewById(R.id.tvage);
        final TextView profilefaculty = view.findViewById(R.id.tvfaculty);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users userProfile = dataSnapshot.getValue(users.class);
                profilename.setText(userProfile.getName().toString());
                profileemail.setText(userProfile.getGmail().toString());
                profilepassword.setText(userProfile.getPassword().toString());
                profileage.setText(userProfile.getAge().toString());
                profilefaculty.setText(userProfile.getFaculty().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

}
