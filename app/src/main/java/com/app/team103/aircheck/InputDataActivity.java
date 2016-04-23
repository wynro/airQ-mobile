package com.app.team103.aircheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class InputDataActivity extends Activity implements View.OnClickListener{

    private static String INPUT_TYPE;
    private static final int RESULT_SYMPTOM = 0;
    private static final int RESULT_EVENT = 1;

    private Button addData_B;
    private ListView data_LV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        addData_B = (Button) findViewById(R.id.addData_B);
        data_LV = (ListView) findViewById(R.id.data_LV);
        addData_B.setOnClickListener(this);
        // Generate list of items
        ArrayList<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        // Load custom adapter
        data_LV.setAdapter(new ListViewAdapter(list, this));
        try{
            INPUT_TYPE = getIntent().getExtras().getString(MainActivity.INPUT_TYPE);
            Intent addData;
            if(INPUT_TYPE.equals(MainActivity.INPUT_SYMPTOM)) {
                // Add symptom
                addData_B.setText("Add symptom");
                addData = new Intent(this, AddSymptomActivity.class);
                startActivityForResult(addData, RESULT_SYMPTOM);
            }else{
                // Add event
                addData_B.setText("Add event");
                addData = new Intent(this, AddEventActivity.class);
                startActivityForResult(addData, RESULT_EVENT);
            }
        }catch(NullPointerException e){
            Log.e("NullPointerException", "onCreate InputDataActivity");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_SYMPTOM:
                /* TODO: do shit, c'mon */
                break;
            case RESULT_EVENT:
                /* TODO: do shit, c'mon */
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addData_B){
            Intent addData;
            switch (INPUT_TYPE){
                case MainActivity.INPUT_SYMPTOM:
                    addData = new Intent(this, AddSymptomActivity.class);
                    startActivityForResult(addData, RESULT_SYMPTOM);
                    break;
                case MainActivity.INPUT_EVENT:
                    addData = new Intent(this, AddEventActivity.class);
                    startActivityForResult(addData, RESULT_EVENT);
                    break;
            }
        }
    }
}
