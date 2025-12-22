package com.example.sixthsenzM5.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sixthsenzM5.R;

public class JobsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_placeholder, container, false);

        // Optionally set text to confirm navigation works
        TextView textView = view.findViewById(R.id.placeholder_text);
        if (textView != null) {
            textView.setText("Job Listings Module (Search & Apply)");
        }

        return view;
    }
}
