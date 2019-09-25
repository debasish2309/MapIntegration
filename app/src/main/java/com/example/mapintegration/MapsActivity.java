package com.example.mapintegration;

import androidx.fragment.app.FragmentActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mapintegration.directionhelpers.FetchURL;
import com.example.mapintegration.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback/*, TaskLoadedCallback*/ {

    private GoogleMap mMap;
    MarkerOptions place1,place2;
    Polyline currentPolyline;
    ArrayList<LatLng> latLongs  = new ArrayList<>();
    MarkerOptions movingMarker;
    SupportMapFragment mapFragment;

    List<LatLng> polyLinelist;
    private Marker marker;
    private float v;
    private double lat,lng;
    private Handler handler;
    private LatLng startPosition,endPosition;
    private int index,next;
    private Button btnGo;
    private String destination;
    private PolylineOptions polylineOptions,blackPolylineOptions;
    private Polyline blackPolyline,greyPolyline;
    private LatLng myLocation;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
          mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = findViewById(R.id.btn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,MarkerPoints.class);
                startActivity(intent);
            }
        });


    //    place1 = new MarkerOptions().position(new LatLng(40.74191,-74.00479)).title("Location1");
    //    place2 = new MarkerOptions().position(new LatLng(40.81210, -74.07241)).title("Location2");

    //    RequestPath();

        /*for(int i = 0 ; i < latLongs.size() ; i++){
            movingMarker = new MarkerOptions().position(new LatLng(latLongs.get(i).getLati(),latLongs.get(i).getLongi())).title("Location3");
        }*/

    //    String  url = getUrl(place1.getPosition(),place2.getPosition(),"driving");
    //    new FetchURL(MapsActivity.this).execute(url,"driving");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Log.d("mylog", "Added Markers");
    //    mMap.addMarker(place1);
    //    mMap.addMarker(place2);
    //    mMap.addMarker(movingMarker);


        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-37.422, -122.084);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        final LatLng source = new LatLng(40.74191,-74.00479);
        LatLng destination = new LatLng(40.8121,-74.07241);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.74191,-74.00479)));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(googleMap.getCameraPosition().target)
                        .zoom(12)
                        .bearing(30)
                        .tilt(45)
                        .build()));

        String requestUrl = null;
        try{
            /*requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"+
                    "mode=driving&"+
                    "transmit_routing_preference=less_driving&"+
                    "origin="+source.latitude+","+source.longitude+"&"+
                    "destination="+destination.latitude+","+destination.longitude+
                    "key=";
            Log.d("!!!url",requestUrl);*/
            /*url = "https://api.myjson.com/bins/11fjuh";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("status").equals("true")){
                                    JSONArray jsonArray = response.getJSONArray("response");
                                    for(int i = 0 ; i < jsonArray.length() ; i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Double latitute = jsonObject.getDouble("lat");
                                        Double longitute = jsonObject.getDouble("long");
                                        latLongs.add(new LatLng(latitute,longitute));
                                        polyLinelist.add(new LatLng(latitute,longitute));

                                        Log.d("!!!",latLongs.toString());
                                    }

                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng latLng:latLongs)
                                        builder.include(latLng);
                                    LatLngBounds bounds = builder.build();
                                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,2);
                                    mMap.animateCamera(mCameraUpdate);

                                    polylineOptions = new PolylineOptions();
                                    polylineOptions.color(Color.GRAY);
                                    polylineOptions.width(5);
                                    polylineOptions.startCap(new SquareCap());
                                    polylineOptions.endCap(new SquareCap());
                                    polylineOptions.jointType(JointType.ROUND);
                                    polylineOptions.addAll(polyLinelist);
                                    greyPolyline = mMap.addPolyline(polylineOptions);

                                    blackPolylineOptions = new PolylineOptions();
                                    blackPolylineOptions.color(Color.BLACK);
                                    blackPolylineOptions.width(5);
                                    blackPolylineOptions.startCap(new SquareCap());
                                    blackPolylineOptions.endCap(new SquareCap());
                                    blackPolylineOptions.jointType(JointType.ROUND);
                                    blackPolylineOptions.addAll(polyLinelist);
                                    blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                    mMap.addMarker(new MarkerOptions().position(polyLinelist.get(polyLinelist.size() - 1)));

                                    //Animator
                                    final ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0,100);
                                    polyLineAnimator.setDuration(2000);
                                    polyLineAnimator.setInterpolator(new LinearInterpolator());
                                    polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            List<LatLng> points = greyPolyline.getPoints();
                                            int percentvalue = (int) animation.getAnimatedValue();
                                            int size = points.size();
                                            int newPoints = (int) (size * (percentvalue / 100.0f));
                                            List<LatLng> p = points.subList(0,newPoints);
                                            blackPolyline.setPoints(p);
                                        }
                                    });
                                    polyLineAnimator.start();
                                    //change icon to cat BitmapDescriptorFactory.fromResources(R.drawable.car);
                                    marker = mMap.addMarker(new MarkerOptions().position(source).flat(true).icon(BitmapDescriptorFactory.defaultMarker()));


                                    //Car Moving
                                    handler = new Handler();
                                    index = -1;
                                    next = 1;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(index < polyLinelist.size()-1){
                                                index++;
                                                next = index + 1;
                                            }
                                            if(index < polyLinelist.size() - 1){
                                                startPosition = polyLinelist.get(index);
                                                endPosition = polyLinelist.get(next);
                                            }

                                            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
                                            valueAnimator.setDuration(2000);
                                            valueAnimator.setInterpolator(new LinearInterpolator());
                                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                @Override
                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                    v = animation.getAnimatedFraction();
                                                    lng = v*endPosition.longitude+(1-v)*startPosition.longitude;
                                                    lat = v*endPosition.latitude + (1-v)* startPosition.latitude;
                                                    LatLng newPos = new LatLng(lat,lng);
                                                    marker.setPosition(newPos);
                                                    marker.setAnchor(0.5f,0.5f);
                                                    marker.setRotation(getBearing(startPosition,newPos));
                                                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                                    .target(newPos)
                                                    .zoom(15.5f)
                                                    .build()));
                                                }
                                            });
                                            valueAnimator.start();
                                            handler.postDelayed(this,3000);

                                        }
                                    },3000);
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
            MySingleton.getInstance(this).addToRequestQueue(request);*/

            try{

                /*requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"+
                        "mode=driving&"+
                        "transmit_routing_preference=less_driving&"+
                        "origin="+source.latitude+","+source.longitude+"&"+
                        "destination="+destination.latitude+","+destination.longitude+
                        "key=";
                Log.d("!!!url",requestUrl);*/

            }catch(Exception e){

            }



        }catch(Exception e){
            e.printStackTrace();
        }


    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

   /* @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }*/

   public void RequestPath(){
       String url = "https://api.myjson.com/bins/11fjuh";

       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           if(response.getString("status").equals("true")){
                               JSONArray jsonArray = response.getJSONArray("response");
                               for(int i = 0 ; i < jsonArray.length() ; i++){
                                   JSONObject jsonObject = jsonArray.getJSONObject(i);
                                   Double latitute = jsonObject.getDouble("lat");
                                   Double longitute = jsonObject.getDouble("long");
                               //    latLongs.add(new LatLong(latitute,longitute));

                                   Log.d("!!!",latLongs.toString());
                               }
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
       MySingleton.getInstance(this).addToRequestQueue(request);

   }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
