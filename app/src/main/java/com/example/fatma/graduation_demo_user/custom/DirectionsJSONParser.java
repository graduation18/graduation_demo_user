package com.example.fatma.graduation_demo_user.custom;

import android.util.Log;

import com.example.fatma.graduation_demo_user.Models.near_by_model;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anupamchugh on 27/11/15.
 */

public class DirectionsJSONParser {

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;


        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l <list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }

    public String get_duration_distance(JSONObject jObject){
        String dist=null,durat=null;
        try {
            JSONArray routes=jObject.getJSONArray("routes");
            for (int i=0;i<routes.length();i++) {
                JSONObject route=routes.getJSONObject(i);
                JSONArray legs = route.getJSONArray("legs");
                for (int j=0;j<legs.length();j++){
                    JSONObject leg=legs.getJSONObject(j);
                    JSONObject distance=leg.getJSONObject("distance");
                    JSONObject duration=leg.getJSONObject("duration");
                    dist=distance.getString("text");
                    durat=duration.getString("text");
                }
            }
        } catch (JSONException e) {
            Log.w("errorrrr",e.getMessage());
        }
        return dist+","+durat;
    }

    public List<near_by_model>parse_near_by(JSONObject jsonObject){
        List<near_by_model>places=new ArrayList<>();
        try {
            JSONArray results=jsonObject.getJSONArray("results");
            for (int i=0;i<results.length();i++){
                JSONObject place=results.getJSONObject(i);
                String formatted_address=place.getString("formatted_address");
                JSONObject geometry=place.getJSONObject("geometry");
                JSONObject location=geometry.getJSONObject("location");
                double lat=location.getDouble("lat");
                double lng=location.getDouble("lng");
                String icon=place.getString("icon");
                String id="";
                if (place.has("id")) {
                    id = place.getString("id");
                }
                String name=place.getString("name");
                JSONObject opening_hours=new JSONObject();
                Boolean open_now=false;
                if (place.has("opening_hours")){

                    opening_hours=place.getJSONObject("opening_hours");
                    if (place.has("open_now")){
                        open_now=opening_hours.getBoolean("open_now");

                    }

                }

                String place_id=place.getString("place_id");
                double price_level=0;
                if (place.has("price_level")){
                    price_level=place.getDouble("price_level");
                }
                double rating=place.getDouble("rating");
                int user_ratings_total=place.getInt("user_ratings_total");
                JSONArray photos=new JSONArray();
                String image_url="";
                if (place.has("photos")) {
                    photos = place.getJSONArray("photos");
                    int height = photos.getJSONObject(0).getInt("height");
                    int width = photos.getJSONObject(0).getInt("width");
                    String photo_reference = photos.getJSONObject(0).getString("photo_reference");
                    image_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + width + "&maxheight=" + height + "&photoreference=" + photo_reference + "&key=AIzaSyDxy0ndkDYovy6TEo71pnhMhfopHbX4fpg";
                }
                places.add(new near_by_model(formatted_address,lat,lng,icon,id,name,open_now,place_id,price_level,rating,user_ratings_total,image_url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return places;
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List decodePoly(String encoded) {

        List poly = new ArrayList();
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