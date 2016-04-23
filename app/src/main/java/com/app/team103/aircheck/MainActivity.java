package com.app.team103.aircheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String INPUT_TYPE = "INPUT_TYPE";
    public static final String INPUT_SYMPTOM = "symptom";
    public static final String INPUT_EVENT = "event";

    private Button inputData_B, visualizeData_B, symptomsData_B, eventsData_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Acquire elements
        inputData_B = (Button) findViewById(R.id.inputData_B);
        visualizeData_B = (Button) findViewById(R.id.visualizeData_B);
        symptomsData_B = (Button) findViewById(R.id.symptomsData_B);
        eventsData_B = (Button) findViewById(R.id.eventsData_B);
        // ClickListeners
        inputData_B.setOnClickListener(this);
        visualizeData_B.setOnClickListener(this);
        symptomsData_B.setOnClickListener(this);
        eventsData_B.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        visualizeData_B.setVisibility(View.VISIBLE);
        symptomsData_B.setVisibility(View.INVISIBLE);
        eventsData_B.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inputData_B:
                // Hide and show shit
                visualizeData_B.setVisibility(View.INVISIBLE);
                symptomsData_B.setVisibility(View.VISIBLE);
                eventsData_B.setVisibility(View.VISIBLE);
                break;
            case R.id.visualizeData_B:
                Intent visualizeDataAct = new Intent(this, VisualizeDataActivity.class);
                startActivity(visualizeDataAct);
                break;
            case R.id.symptomsData_B:
                Intent inputSymptomAct = new Intent(this, InputDataActivity.class);
                inputSymptomAct.putExtra(INPUT_TYPE, INPUT_SYMPTOM);
                startActivity(inputSymptomAct);
                break;
            case R.id.eventsData_B:
                Intent inputEventAct = new Intent(this, InputDataActivity.class);
                inputEventAct.putExtra(INPUT_TYPE, INPUT_EVENT);
                startActivity(inputEventAct);
                break;
        }
    }
}
