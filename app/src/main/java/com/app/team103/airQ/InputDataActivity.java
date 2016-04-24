package com.app.team103.airQ;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InputDataActivity extends Activity implements View.OnClickListener{

    public static final String DATA_NAME = "name";
    public static final String DATA_KEY = "key";
    public static final String DATA_VALUE = "value";

    private static String INPUT_TYPE;
    private static final int RESULT_SYMPTOM = 0;
    private static final int RESULT_EVENT = 1;
    private static final String URLBase = "http://192.168.1.36";

    private Button submit_B, addData_B, backDattoMain_B;
    private ListView data_LV;
    private ArrayList<Pair<Pair<String, String>, Integer>> submitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        // Aquire
        submit_B = (Button) findViewById(R.id.submit_B);
        addData_B = (Button) findViewById(R.id.addData_B);
        backDattoMain_B = (Button) findViewById(R.id.backDattoMain_B);
        data_LV = (ListView) findViewById(R.id.data_LV);
        // Listeners
        submit_B.setOnClickListener(this);
        addData_B.setOnClickListener(this);
        backDattoMain_B.setOnClickListener(this);
        // Generate list of items
        submitList = new ArrayList<>();
        // Load custom adapter
        data_LV.setAdapter(new ListViewAdapter(submitList, this));
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
                if(resultCode == MainActivity.CODE_OK){
                    // Returned data
                    // Create Pair
                    Pair<Pair<String, String>, Integer> newData = new Pair<>(new Pair<>(data.getStringExtra(DATA_NAME),
                            data.getStringExtra(DATA_KEY)), data.getIntExtra(DATA_VALUE, 0));
                    // Input pair into ListView
                    submitList.add(newData);
                    data_LV.setAdapter(new ListViewAdapter(submitList, this));
                }
                break;
            case RESULT_EVENT:
                if(resultCode == MainActivity.CODE_OK){
                    /* TODO: do shit, c'mon */
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_B:
                new PostJSONData().execute(submitList);
                break;
            case R.id.addData_B:
                Intent addData;
                switch (INPUT_TYPE) {
                    case MainActivity.INPUT_SYMPTOM:
                        addData = new Intent(this, AddSymptomActivity.class);
                        startActivityForResult(addData, RESULT_SYMPTOM);
                        break;
                    case MainActivity.INPUT_EVENT:
                        addData = new Intent(this, AddEventActivity.class);
                        startActivityForResult(addData, RESULT_EVENT);
                        break;
                }
                break;
            case R.id.backDattoMain_B:
                finish();
                break;
        }
    }
    private class PostJSONData extends AsyncTask<ArrayList<Pair<Pair<String, String>, Integer>>, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(ArrayList<Pair<Pair<String, String>, Integer>>... params) {
            try{
                URL url;
                URLConnection urlConn;
                DataOutputStream printout;
                if(INPUT_TYPE.equals(MainActivity.INPUT_SYMPTOM)){
                    // Symptoms
                    url = new URL(URLBase + "/insert/symptoms");
                }else{
                    // Events
                    url = new URL(URLBase + "/insert/environment");
                }
                urlConn = url.openConnection();
                urlConn.setDoInput (true);
                urlConn.setDoOutput (true);
                urlConn.setUseCaches (false);
                urlConn.setRequestProperty("Content-Type","application/json");
                urlConn.connect();
                //Create JSONObject here
                JSONObject jsonParam = buildJSON(params[0]);
                // Send POST output.
                printout = new DataOutputStream(urlConn.getOutputStream());
                printout.writeChars(URLEncoder.encode(jsonParam.toString(),"UTF-8"));
                printout.flush ();
                printout.close ();
            }catch(MalformedURLException e) {
                Log.e("ERROR", "MalformedURLException sending POST JSON");
            }catch(IOException e){
                Log.e("ERROR", "IOException sending POST JSON");
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
        }

        private JSONObject buildJSON(ArrayList<Pair<Pair<String, String>, Integer>> data){
            JSONObject result = new JSONObject();
            try{
                for(Pair<Pair<String, String>, Integer> item: data) {
                    result.put(item.first.second, item.second);
                }
            }catch (JSONException e) {
                Log.e("ERROR", "JSONException building JSON in buildJSON method");
            }
            return result;
        }
    }
}
