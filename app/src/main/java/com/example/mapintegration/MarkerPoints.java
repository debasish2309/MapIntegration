package com.example.mapintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarkerPoints extends AppCompatActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMapClickListener {

    GoogleMap mMap;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_points);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.marker_map);
        mapFragment.getMapAsync(MarkerPoints.this);

        button = findViewById(R.id.button_car_anim);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarkerPoints.this,CarAnimation.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        final String url = "https://api.myjson.com/bins/nrjt5";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("!!!map",url + response);
                        try {
                            if(response.getString("status").equals("true")){
                                JSONArray jsonArray = response.getJSONArray("result");
                                for(int i = 0 ; i < jsonArray.length() ; i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(object.getDouble("lat"),object.getDouble("lng"))));

                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.597698,88.389767),12.0f));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
