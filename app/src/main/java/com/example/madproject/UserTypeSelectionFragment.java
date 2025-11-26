package com.example.madproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class UserTypeSelectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_type_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonEmployee = view.findViewById(R.id.button_employee);
        Button buttonEmployer = view.findViewById(R.id.button_employer);
        NavController navController = Navigation.findNavController(view);

        buttonEmployee.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userType", "employee");
            navController.navigate(R.id.action_userTypeSelectionFragment_to_signUpFragment, bundle);
        });

        buttonEmployer.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userType", "employer");
            navController.navigate(R.id.action_userTypeSelectionFragment_to_signUpFragment, bundle);
        });
    }
}

