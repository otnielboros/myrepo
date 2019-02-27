package ro.yuhuu.backend.yubackend.service.implementations;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.IOException;

public class Geocoding {

    private static GeoApiContext context;

    public static LatLng get(String address){
        initializeContext();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                    address).await();
            return results[0].geometry.location;
        } catch (InterruptedException | IOException | ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double getDistance(String address1, String address2){
        initializeContext();
        try {
        DirectionsApiRequest request = DirectionsApi.getDirections(context,address1,address2);
        DirectionsResult result = request.await();
        if(result.routes.length!=0){
            Double distance = 0.0;
            for(DirectionsLeg leg:result.routes[0].legs){
                distance+=leg.distance.inMeters;
            }
            return distance;
        }
        } catch (InterruptedException | IOException | ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initializeContext(){
        if(context==null){
            context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyCKmU9tgi_LhsJSFFMboifgUaS4XZFmmpc")
                    .build();
        }
    }
}