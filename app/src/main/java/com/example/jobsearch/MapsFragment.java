package com.example.jobsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MarkerOptions marker;

    private List<Job> jobs = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_maps2, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;

        LatLng kl = new LatLng(3.1390, 101.6869);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kl, 11f));

        // Show jobs if already loaded
        if (!jobs.isEmpty()) {
            showJobsOnMap();
        }

        googleMap.setOnMarkerClickListener(marker -> {
            Job job = (Job) marker.getTag();
            if (job != null) {
                // Later: open JobDetailActivity
            }
            return false;
        });
    }


    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        if (googleMap != null) {
            showJobsOnMap();
        }
    }

    private void showJobsOnMap() {
        new Thread(() -> {
            List<MarkerOptions> markers = new ArrayList<>();

            for (Job job : jobs) {
                LatLng latLng = LocationUtil.getLatLng(requireContext(), job.getLocation());
                if (latLng == null) continue;

                markers.add(new MarkerOptions()
                        .position(latLng)
                        .title(job.getTitle())
                        .snippet(job.getCompany()));
            }

            // Update map on UI thread
            requireActivity().runOnUiThread(() -> {
                for (MarkerOptions mo : markers) {
                    Marker marker = googleMap.addMarker(mo);
                    marker.setTag(jobs.get(markers.indexOf(mo)));
                }
            });
        }).start();
    }



}
