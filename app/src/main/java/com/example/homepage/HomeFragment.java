package com.example.homepage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.madproject.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_simple_placeholder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SharedPreferences prefs =
                requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        String userType = prefs.getString("user_type", "employee");

        NavController navController =
                NavHostFragment.findNavController(this);

        if ("employer".equals(userType)) {
            navController.navigate(R.id.employerHomeFragment);
        } else {
            navController.navigate(R.id.employeeHomeFragment);
        }
    }
}

