package com.app.team103.airQ;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddSymptomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner symptom_SP, intensity_SP;
    private TextView intensity_TV;
    private Button addSymptom_B, backSymtoData_B;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);
        symptom_SP = (Spinner) findViewById(R.id.symptom_SP);
        intensity_SP = (Spinner) findViewById(R.id.intensity_SP);
        intensity_TV = (TextView) findViewById(R.id.intensity_TV);
        addSymptom_B = (Button) findViewById(R.id.addEvent_B);
        backSymtoData_B = (Button) findViewById(R.id.backEvetoData_B);
        // Listeners
        symptom_SP.setOnItemSelectedListener(this);
        intensity_SP.setOnItemSelectedListener(this);
        addSymptom_B.setOnClickListener(this);
        backSymtoData_B.setOnClickListener(this);
        // Hide intensity until symptom selected
        intensity_SP.setVisibility(View.INVISIBLE);
        intensity_TV.setVisibility(View.INVISIBLE);
        // Fill spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.symptom_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        symptom_SP.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.symptom_SP:
                Log.d("DEBUGGING", "Item position: " + position);
                if(position == 0) {
                    // "Choose one" selected
                    intensity_SP.setVisibility(View.INVISIBLE);
                    intensity_TV.setVisibility(View.INVISIBLE);
                    // Reset selection
                    intensity_SP.setSelection(0);
                }else{
                    // Symptom selected
                    // Regular symptom selected
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.symptom_values, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intensity_SP.setAdapter(adapter);
                    // Set visibility
                    intensity_SP.setVisibility(View.VISIBLE);
                    intensity_TV.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.intensity_SP:
                // Nothing
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /* Empty */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addEvent_B:
                if(symptom_SP.getSelectedItemPosition() != 0 && intensity_SP.getSelectedItemPosition() != 0){
                    // Valid input
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(InputDataActivity.DATA_NAME, getResources().getStringArray(R.array.symptom_names)[symptom_SP.getSelectedItemPosition()]);
                    returnIntent.putExtra(InputDataActivity.DATA_KEY, getResources().getStringArray(R.array.symptom_keys)[symptom_SP.getSelectedItemPosition()]);
                    returnIntent.putExtra(InputDataActivity.DATA_VALUE, Integer.parseInt(getResources().getStringArray(R.array.symptom_values)[intensity_SP.getSelectedItemPosition()]));
                    Log.d("DEBUGG", "Value: " + getResources().getIntArray(R.array.symptom_values)[intensity_SP.getSelectedItemPosition()]);
                    setResult(MainActivity.CODE_OK, returnIntent);
                    finish();
                }else{
                    // Invalid input
                    Toast.makeText(getApplicationContext(), "Please choose a symptom and intensity", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backEvetoData_B:
                setResult(MainActivity.CODE_NOK);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(MainActivity.CODE_NOK);
        finish();
        return true;
    }
}
