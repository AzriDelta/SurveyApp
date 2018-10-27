package com.example.jassyap.first_try;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Main_load extends AppCompatActivity {

    private HomeFragment homeFragment;
    private NotificationFragment notifcationFragment;
    private GenerateReportFragment generatereportFragment;
    private SurveyFragment surveyFragment;
    private AccountFragment accountFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(homeFragment);
                    return true;
                case R.id.navigation_survey:
                    setFragment(surveyFragment);
                    return true;
                case R.id.navigation_notifications:
                    setFragment(notifcationFragment);
                    return true;
                case R.id.navigation_account:
                    setFragment(accountFragment);
                    return true;
                case R.id.navigation_response:
                    setFragment(generatereportFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_load);

        homeFragment = new HomeFragment();
        surveyFragment = new SurveyFragment();
        notifcationFragment = new NotificationFragment();
        accountFragment = new AccountFragment();
        generatereportFragment = new GenerateReportFragment();

        setFragment(homeFragment);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.main_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setFragment (android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
}
