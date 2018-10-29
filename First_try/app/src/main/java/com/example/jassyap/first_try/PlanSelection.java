package com.example.jassyap.first_try;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlanSelection extends AppCompatActivity {
    private Button button, button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_selection);

        getSupportActionBar().setTitle("Subscription Plans");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFreePlan();
            }
        });

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPremiumPlan();
            }
        });
    }

    public void openFreePlan(){
        Intent intent = new Intent(PlanSelection.this, FreePlan.class);
        startActivity(intent);
    }

    public void openPremiumPlan(){
        Intent intent = new Intent(PlanSelection.this, PremiumPlan.class);
        startActivity(intent);
    }
}
