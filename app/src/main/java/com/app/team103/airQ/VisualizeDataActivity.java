package com.app.team103.airQ;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class VisualizeDataActivity extends Activity {

    private WebView mapView;
    private String[] URLs = {
            "https://balaber.cartodb.com/viz/c8b32812-09c6-11e6-8f78-0e31c9be1b51/embed_map",
            "https://balaber.cartodb.com/viz/4fbe044e-09b8-11e6-a496-0e674067d321/embed_map"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_visualize_data);
        //mapView = (WebView) findViewById(R.id.mapView);
        WebView mapView = new WebView(this);
        //mapView.loadData(iframes[1], "text/html", null);
        mapView.loadUrl(URLs[0]);
        mapView.getSettings().setJavaScriptEnabled(true);
        setContentView(mapView);

    }
}
