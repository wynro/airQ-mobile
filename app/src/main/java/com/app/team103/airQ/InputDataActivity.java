package com.app.team103.airQ;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InputDataActivity extends Activity implements View.OnClickListener {

    public static final String DATA_NAME = "name";
    public static final String DATA_KEY = "key";
    public static final String DATA_VALUE = "value";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "long";
    public static final String COORDINATES = "coords";

    private static String INPUT_TYPE;
    private static final int RESULT_SYMPTOM = 0;
    private static final int RESULT_EVENT = 1;
    private static final String URLBase = "http://192.168.1.36";
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 0;

    public static JSONObject locationLATLONG = new JSONObject();
    private Button submit_B, addData_B, backDattoMain_B;
    private ListView data_LV;
    private ArrayList<Pair<Pair<String, String>, Integer>> submitList;
    private LocationManager locationManager;

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
        try {
            INPUT_TYPE = getIntent().getExtras().getString(MainActivity.INPUT_TYPE);
            Intent addData;
            if (INPUT_TYPE.equals(MainActivity.INPUT_SYMPTOM)) {
                // Add symptom
                addData_B.setText("Add symptom");
                addData = new Intent(this, AddSymptomActivity.class);
                startActivityForResult(addData, RESULT_SYMPTOM);
            } else {
                // Add event
                addData_B.setText("Add event");
                addData = new Intent(this, AddEventActivity.class);
                startActivityForResult(addData, RESULT_EVENT);
            }
        } catch (NullPointerException e) {
            Log.e("NullPointerException", "onCreate InputDataActivity");
        }
        // LOCATION
        // Acquire a reference to the system Location Manager
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                try {
                    InputDataActivity.locationLATLONG.put(InputDataActivity.LATITUDE, location.getLatitude());
                    InputDataActivity.locationLATLONG.put(InputDataActivity.LONGITUDE, location.getLongitude());
                } catch (JSONException e) {
                    Log.e("ERROR", "Error getting update location");
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
            return;
        }
        ((LocationManager) this.getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        // Get last known location
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        try {
            locationLATLONG.put(LATITUDE, lastKnownLocation.getLatitude());
            locationLATLONG.put(LONGITUDE, lastKnownLocation.getLongitude());
            Log.d("DEBUG", "Location data, lat " + lastKnownLocation.getLatitude() + " long " + lastKnownLocation.getLongitude());
        } catch (JSONException e) {
            Log.e("ERROR", "Error getting last known location");
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
                    Pair<Pair<String, String>, Integer> newData = new Pair<>(new Pair<>(data.getStringExtra(DATA_NAME),
                            data.getStringExtra(DATA_KEY)), data.getIntExtra(DATA_VALUE, 1));
                    submitList.add(newData);
                    data_LV.setAdapter(new ListViewAdapter(submitList, this));
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private class PostJSONData extends AsyncTask<ArrayList<Pair<Pair<String, String>, Integer>>, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(ArrayList<Pair<Pair<String, String>, Integer>>... params) {
            boolean result = false;
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
                result = true;
            }catch(MalformedURLException e) {
                Log.e("ERROR", "MalformedURLException sending POST JSON");
            }catch(IOException e){
                Log.e("ERROR", "IOException sending POST JSON");
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getApplicationContext(), "Data successfully submitted!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Data could not be sent.", Toast.LENGTH_SHORT).show();
            }
        }

        private JSONObject buildJSON(ArrayList<Pair<Pair<String, String>, Integer>> data){
            JSONObject dataJSON = buildJSONBase();
            JSONObject result = new JSONObject();
            try{
                for(Pair<Pair<String, String>, Integer> item: data) {
                    result.put(item.first.second, item.second);
                }
                if(INPUT_TYPE.equals(MainActivity.INPUT_SYMPTOM)){
                    // Symptoms
                    result.put(MainActivity.INPUT_SYMPTOM, dataJSON);
                }else{
                    // Events
                    result.put(MainActivity.INPUT_EVENT, dataJSON);
                }
                // Put coordinates
                result.put(InputDataActivity.COORDINATES, locationLATLONG);
            }catch (JSONException e) {
                Log.e("ERROR", "JSONException building JSON in buildJSON method");
            }
            return result;
        }

        private JSONObject buildJSONBase(){
            JSONObject base = null;
            try {
                base = new JSONObject();
                if(INPUT_TYPE.equals(MainActivity.INPUT_SYMPTOM)){
                    base.put("cough", 0);
                    base.put("airLackness", 0);
                    base.put("wheezing", 0);
                    base.put("sneezing", 0);
                    base.put("obstruction", 0);
                    base.put("itchy", 0);
                    // API requires airQuality to be in [1..10]
                    base.put("airQuality", 1);
                }else{
                    base.put("fire", 0);
                    base.put("dustStorm", 0);
                    base.put("tornado", 0);
                    base.put("smokePlumes", 0);
                    base.put("earthquake", 0);
                    base.put("volcano", 0);
                }
            } catch (JSONException e) {
                Log.e("ERROR", "JSONException building baseJSON in buildJSON method");
            }
            return base;
        }
    }
}
