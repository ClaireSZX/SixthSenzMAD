package com.example.jobsearch;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtil {

    public static LatLng getLatLng(Context context, String location) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses == null || addresses.isEmpty()) return null;

            Address address = addresses.get(0);
            return new LatLng(address.getLatitude(), address.getLongitude());

        } catch (IOException e) {
            return null;
        }
    }
}
