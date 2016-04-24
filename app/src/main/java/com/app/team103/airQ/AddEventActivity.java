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

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner event_SP;
    private Button addEvent_B, backEvetoData_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        event_SP = (Spinner) findViewById(R.id.event_SP);
        addEvent_B = (Button) findViewById(R.id.addEvent_B);
        backEvetoData_B = (Button) findViewById(R.id.backEvetoData_B);
        // Listeners
        event_SP.setOnItemSelectedListener(this);
        addEvent_B.setOnClickListener(this);
        backEvetoData_B.setOnClickListener(this);
        // Fill spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_SP.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addEvent_B:
                if(event_SP.getSelectedItemPosition() != 0){
                    // Valid input
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(InputDataActivity.DATA_NAME, getResources().getStringArray(R.array.event_names)[event_SP.getSelectedItemPosition()]);
                    returnIntent.putExtra(InputDataActivity.DATA_KEY, getResources().getStringArray(R.array.event_keys)[event_SP.getSelectedItemPosition()]);
                    setResult(MainActivity.CODE_OK, returnIntent);
                    finish();
                }else{
                    // Invalid input
                    Toast.makeText(getApplicationContext(), "Please choose an event", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backEvetoData_B:
                setResult(MainActivity.CODE_NOK);
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /* TODO: Show lil' image below */
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(MainActivity.CODE_NOK);
        finish();
        return true;
    }
}
