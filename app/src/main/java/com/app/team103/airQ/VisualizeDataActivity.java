package com.app.team103.airQ;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


public class VisualizeDataActivity extends Activity implements View.OnClickListener{

    private WebView mapView;
    private String[] URLs = {
            "https://balaber.cartodb.com/viz/c8b32812-09c6-11e6-8f78-0e31c9be1b51/embed_map",
            "https://balaber.cartodb.com/viz/4fbe044e-09b8-11e6-a496-0e674067d321/embed_map",
            ""};
    private Button visualizeEvents_B, visualizeAirQ_B, visualizeDiscordance_B;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_data);
        mapView = (WebView) findViewById(R.id.mapView);
        visualizeEvents_B = (Button) findViewById(R.id.visualizeEvents_B);
        visualizeAirQ_B = (Button) findViewById(R.id.visualizeAirQ_B);
        visualizeDiscordance_B = (Button) findViewById(R.id.visualizeDiscordance_B);
        visualizeEvents_B.setOnClickListener(this);
        visualizeAirQ_B.setOnClickListener(this);
        visualizeDiscordance_B.setOnClickListener(this);
        mapView.loadUrl(URLs[0]);
        mapView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.visualizeEvents_B:
                mapView.loadUrl(URLs[0]);
                break;
            case R.id.visualizeAirQ_B:
                mapView.loadUrl(URLs[1]);
                break;
            case R.id.visualizeDiscordance_B:
                mapView.loadUrl(URLs[2]);
                break;
        }
    }
}
